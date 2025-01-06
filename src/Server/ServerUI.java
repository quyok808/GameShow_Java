/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server;

/**
 *
 * @author Le Cuong
 */
import ApplicationDbContext.*;
import Models.CauHoi;
import Models.ListScoreBoard;
import Models.QLUser;
import Models.ScoreBoard;
import Models.User;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import java.time.LocalDateTime;

import javax.swing.SwingUtilities;

public class ServerUI extends JFrame {

    private static final int PORT = 12345;
    private static final List<ClientHandler> clients = new ArrayList<>();
    private static final Queue<ClientHandler> waitingQueue = new ConcurrentLinkedQueue<>();
    private static final List<CauHoi> questions_round1 = new ArrayList<>();
    private static final List<CauHoi> questions_round2 = new ArrayList<>();
    private static final List<CauHoi> questions_round3 = new ArrayList<>();
    private static final List<CauHoi> questions_round4_GOI1 = new ArrayList<>();
    private static final List<CauHoi> questions_round4_GOI2 = new ArrayList<>();
    private static final List<CauHoi> questions_round4_GOI3 = new ArrayList<>();
    private static final List<CauHoi> goiCauHoiChoosed = new ArrayList<>();
    private static QLUser ql;
    private static ListScoreBoard diem;
    private static List<ScoreBoard> currentscoreBoard = new ArrayList<>();
    private static DBAccess access = new DBAccess();

    private final JTextArea serverLog;
    private final DefaultListModel<String> clientListModel;
    private final JList<String> clientList;
    private JButton startButton;
    private JButton stopButton;
    private ServerSocket serverSocket;
    private boolean isRunning = false;
    private Thread serverThread;

    public ServerUI() {
        setTitle("Simple Quiz Server");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(5, 5));
        mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5)); // Add margin
        setContentPane(mainPanel);

        // Server log area
        serverLog = new JTextArea();
        serverLog.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(serverLog);
        logScrollPane.setBorder(new TitledBorder("Server Log"));
        mainPanel.add(logScrollPane, BorderLayout.CENTER);

        // Client list
        clientListModel = new DefaultListModel<>();
        clientList = new JList<>(clientListModel);
        JScrollPane clientScrollPane = new JScrollPane(clientList);
        clientScrollPane.setPreferredSize(new Dimension(200, 0)); // Set preferred width
        clientScrollPane.setBorder(new TitledBorder("Connected Clients"));
        mainPanel.add(clientScrollPane, BorderLayout.EAST);

        //Panel buttons
        JPanel buttonPanel = new JPanel();
        startButton = new JButton("Start Server");
        stopButton = new JButton("Stop Server");
        stopButton.setEnabled(false);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startServer();
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopServer();
            }
        });

        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void startServer() {
        if (isRunning) {
            return;
        }

        startButton.setEnabled(false);
        stopButton.setEnabled(true);

        isRunning = true;
        appendToLog("Server is starting...");

        serverThread = new Thread(() -> {
            try {
                serverSocket = new ServerSocket(PORT);
                appendToLog("Server is running on port " + PORT + "...");

                ql = new QLUser();
                diem = new ListScoreBoard();
                loadQuestionsFromFile("../test2/src/RoundQuestions/FirstRound/Questions.txt", questions_round1);
                loadQuestionsFromFile("../test2/src/RoundQuestions/SecondRound/Questions.txt", questions_round2);
                loadQuestionsFromFile("../test2/src/RoundQuestions/ThirdRound/Questions.txt", "QuestionRound3");
                //load question round 1,2,3
                loadQuestionsFromFile("F:/IT/1-DA_LTMMT/Code_Temp/test2/src/RoundQuestions/FouthRound/GOI1/Questions.txt", "GOI1");
                loadQuestionsFromFile("F:/IT/1-DA_LTMMT/Code_Temp/test2/src/RoundQuestions/FouthRound/GOI2/Questions.txt", "GOI2");
                loadQuestionsFromFile("F:/IT/1-DA_LTMMT/Code_Temp/test2/src/RoundQuestions/FouthRound/GOI3/Questions.txt", "GOI3");
                // Chấp nhận kết nối từ client
                while (isRunning) {
                    Socket socket = serverSocket.accept();
                    appendToLog("New client connected!");
                    ClientHandler clientHandler = new ClientHandler(socket);
                    clients.add(clientHandler);
                    new Thread(clientHandler).start();

                }
            } catch (java.net.BindException ex) {
                JOptionPane.showMessageDialog(null, "Server đã khởi động từ trước !!!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                appendToLog("Error starting server: " + ex.getMessage());
            } finally {
                try {
                    if (serverSocket != null) {
                        serverSocket.close();
                    }
                    appendToLog("Server has been stop");

                    SwingUtilities.invokeLater(() -> {
                        startButton.setEnabled(true);
                        stopButton.setEnabled(false);
                    });
                } catch (IOException e) {
                    appendToLog("Error closing socket server: " + e.getMessage());
                }
            }
        });
        serverThread.start();
    }

    private void stopServer() {
        if (!isRunning) {
            return;
        }

        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        isRunning = false;
        if (serverThread != null) {
            serverThread.interrupt();
        }

    }

    private void loadQuestionsFromFile(String fileName, List<CauHoi> questionround) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|"); // Tách dữ liệu theo dấu "|"
                if (parts.length == 3) {
                    String question = parts[0];
                    String answer = parts[1];
                    String correctAnswer = parts[2];

                    CauHoi newCauHoi = new CauHoi("", question, answer, correctAnswer);
                    questionround.add(newCauHoi);
                } else {
                    appendToLog("Invalid data format in line: " + line);
                }
            }
            appendToLog("Questions loaded: " + questionround.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadQuestionsFromFile(String fileName, String GoiCauHoi) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|"); // Tách dữ liệu theo dấu "|"
                if (parts.length == 3) {
                    String videoPath = parts[0];
                    String question = parts[1]; // Bỏ qua "Q." ở đầu câu hỏi
                    String correctAnswer = parts[2]; // Bỏ qua "Ans." ở đầu câu trả lời

                    CauHoi newCauHoi = new CauHoi(videoPath, question, "", correctAnswer);
                    switch (GoiCauHoi) {
                        case "GOI1":
                            questions_round4_GOI1.add(newCauHoi);
                            break;
                        case "GOI2":
                            questions_round4_GOI2.add(newCauHoi);
                            break;
                        case "GOI3":
                            questions_round4_GOI3.add(newCauHoi);
                            break;
                        case "QuestionRound3":
                            questions_round3.add(newCauHoi);
                            break;
                        default:

                            throw new AssertionError();
                    }
                } else {
                    appendToLog("Invalid data format in line: " + line);
                }
            }
            switch (GoiCauHoi) {
                case "GOI1":
                    appendToLog("Questions GOI1 loaded: " + questions_round4_GOI1.size());
                    break;
                case "GOI2":
                    appendToLog("Questions GOI2 loaded: " + questions_round4_GOI2.size());
                    break;
                case "GOI3":
                    appendToLog("Questions GOI3 loaded: " + questions_round4_GOI3.size());
                    break;
                case "QuestionRound3":
                    appendToLog("Questions round 3 loaded: " + questions_round3.size());
                    break;
                default:
                    throw new AssertionError();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static synchronized void updateWaitingRoom() {
        StringBuilder waitingRoomStatus = new StringBuilder("Waiting for players:\n");
        for (ClientHandler client : waitingQueue) {
            waitingRoomStatus.append(client.getPlayerName()).append("\n");
        }
        broadcastWaitingRoom(waitingRoomStatus.toString().trim());
    }

    private static void broadcastWaitingRoom(String message) {
        for (ClientHandler client : waitingQueue) {
            client.sendMessage(message);
        }
    }

    synchronized static void pairClients(ClientHandler client) {
        waitingQueue.add(client);
        updateWaitingRoom();

        if (waitingQueue.size() >= 2) { // Chờ đủ 4 người chơi
            new Thread(() -> {
                try {
                    // Đếm ngược 10 giây
                    for (int i = 10; i > 0; i--) {
                        broadcastWaitingRoom("Game starts in " + i + " seconds...");
                        Thread.sleep(1000);
                    }

                    // Lấy 4 người chơi từ phòng chờ
                    List<ClientHandler> players = new ArrayList<>();
                    for (int i = 0; i < 2; i++) {
                        players.add(waitingQueue.poll());
                    }

                    // Xác nhận tất cả người chơi đã ghép cặp
                    for (ClientHandler player : players) {
                        player.sendMessage("Game starting! Players in this game:");
                        for (ClientHandler teammate : players) {
                            if (player != teammate) {
                                player.sendMessage("- " + teammate.getPlayerName());
                            }

                        }
                    }

                    // Bắt đầu trò chơi với 4 người chơi
                    startGame(players);
                    updateWaitingRoom(); // Cập nhật phòng chờ sau khi ghép cặp
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    private static void startGame(List<ClientHandler> players) {
        new Thread(() -> {
            // Trò chơi bắt đầu
            // Ví dụ ở đây có thể chỉ đơn giản là thông báo rằng trò chơi đã bắt đầu mà không cần câu hỏi
            for (ClientHandler player : players) {
                User user = ql.TimKiem(player.getPlayerName());
                int id = LocalDateTime.now().getNano();
                ScoreBoard newRecord = new ScoreBoard(id, user.getId(), user.getUsername(), 0);
                diem.addNewPlayer(newRecord);
                currentscoreBoard.add(newRecord);
                player.sendMessage("Game started! Good luck!");
            }
        }).start();
    }

//============================================================================================================
    // Lớp xử lý client
    private class ClientHandler implements Runnable {

        private final Socket socket;
        private final PrintWriter out;
        private final BufferedReader in;
        private String playerName;
        private int score_per_Question = 0;

        public void sendMessage(String message) {
            out.println(message);
        }

        public String getPlayerName() {
            return playerName;
        }

        public void setPlayerName(String playerName) {
            this.playerName = playerName;
            SwingUtilities.invokeLater(() -> {
                clientListModel.addElement(playerName);
            });
        }

        public String readAnswer() throws IOException {
            return in.readLine(); // Đọc câu trả lời từ client
        }

        public int getScore_per_Question() {
            return score_per_Question;
        }

        public void setScore_per_Question(int score_per_Question) {
            this.score_per_Question = score_per_Question;
        }

        public ClientHandler(Socket socket) throws IOException {
            this.socket = socket;
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(socket.getOutputStream(), true);
        }

        @Override
        public void run() {
            try {
                handleClient();
            } catch (IOException e) {
                appendToLog("Client disconnected: " + e.getMessage());
            } finally {
                cleanup();
            }
        }

        private void cleanup() {
            try {
                socket.close();
            } catch (IOException e) {
                appendToLog("Error closing socket: " + e.getMessage());
            }

            // Loại bỏ client khỏi hàng đợi chờ và danh sách client
            synchronized (waitingQueue) {
                waitingQueue.remove(this);
            }
            synchronized (clients) {
                clients.remove(this);
            }
            SwingUtilities.invokeLater(() -> {
                clientListModel.removeElement(playerName);
            });
            // Cập nhật lại phòng chờ
            updateWaitingRoom();
            appendToLog("Client has been left.");
        }

        private void handleClient() throws IOException {
            try {
                String command;
                while ((command = in.readLine()) != null) { // Đảm bảo không có null khi đọc lệnh
                    if (command.trim().isEmpty()) {
                        continue; // Bỏ qua lệnh rỗng
                    }
                    appendToLog("Received command: " + command); // In ra log để kiểm tra lệnh nhận được

                    if (command.startsWith("LOGIN")) {
                        String[] parts = command.split(" ", 3);
                        if (parts.length == 3) {
                            String username = parts[1];
                            String password = parts[2];
                            handleLogin(username, password, out);
                        } else {
                            out.println("LOGIN_FAILED: Sai định dạng.");
                        }
                    } else if (command.startsWith("REGISTER")) {
                        String[] parts = command.split(" ", 3);
                        if (parts.length == 3) {
                            String username = parts[1];
                            String password = parts[2];
                            handleRegister(username, password, out);
                        } else {
                            out.println("REGISTER_FAILED: Sai định dạng.");
                        }
                    } else if (command.startsWith("PLAYER_NAME")) {
                        String[] parts = command.split(" ", 2);
                        if (parts.length == 2) {
                            String username = parts[1];
                            // Đọc tên người chơi
                            this.playerName = username;
                            setPlayerName(playerName);
                            out.println("Hello, " + this.playerName + "!");
                        }
                    } else if ("Pair".equalsIgnoreCase(command)) {
                        pairClients(this);
                    } else if ("Unpair".equalsIgnoreCase(command)) {
                        cleanup();
                    } else if (command.startsWith("GOI1")) {
                        goiCauHoiChoosed.clear();
                        setScore_per_Question(10);
                        for (var item : questions_round4_GOI1) {
                            goiCauHoiChoosed.add(item);
                        }
                    } else if (command.startsWith("GOI2")) {
                        goiCauHoiChoosed.clear();
                        setScore_per_Question(20);
                        for (var item : questions_round4_GOI2) {
                            goiCauHoiChoosed.add(item);
                        }
                    } else if (command.startsWith("GOI3")) {
                        goiCauHoiChoosed.clear();
                        setScore_per_Question(30);
                        for (var item : questions_round4_GOI3) {
                            goiCauHoiChoosed.add(item);
                        }
                    } else if (command.startsWith("START_FOUTHROUND")) {
                        for (var user : currentscoreBoard){
                            user.setAnswered(false);
                        }
                        // Duyệt qua tất cả các câu hỏi
                        for (var item : goiCauHoiChoosed) {
                            try {
                                // Gửi câu hỏi tới client
                                String question = "Question@" + item.getLinkVideo() + "@" + item.getCauHoi();
                                out.println(question);
                                appendToLog("Sent: " + question);

                                // Đặt thời gian chờ phản hồi từ client
                                socket.setSoTimeout(40000); // 40 giây  

                                String answer = in.readLine();

                                // Kiểm tra câu trả lời từ client
                                if (answer == null) {
                                    appendToLog("No answer from client.");
                                    out.println("NO_ANSWER");
                                } else if (answer.trim().isEmpty()) {
                                    appendToLog("null answer.");
                                    out.println("INVALID_ANSWER");
                                } else {
                                    appendToLog("Received answer: " + answer);
                                    out.println("ANSWER_RECEIVED");
                                    // Xử lý logic với câu trả lời (nếu cần)
                                    processAnswer1(answer.substring(7), item);
                                }

                            } catch (SocketTimeoutException e) {
                                appendToLog("time out");
                                out.println("TIMEOUT");
                                // Chuyển sang câu hỏi tiếp theo
                            } catch (IOException e) {
                                appendToLog("error: " + e.getMessage());
                                out.println("ERROR");
                            }
                        }
                        socket.setSoTimeout(0);
                        // Khi đã hết câu hỏi, gửi "DONE" và hiển thị thông báo kết thúc
                        out.println("DONE");
                        appendToLog("All questions completed");
                    } else if (command.startsWith("START_SECONDROUND")) {
                        for (var user : currentscoreBoard){
                            user.setAnswered(false);
                        }
                        for (var item : questions_round2) {
                            try {
                                String question = "Question@" + item.getCauHoi() + "@" + item.getDapAn();
                                out.println(question);
                                appendToLog("Sent: " + question);// Đặt thời gian chờ phản hồi từ client
                                socket.setSoTimeout(10000); // 40 giây  

                                String answer = in.readLine();

                                // Kiểm tra câu trả lời từ client
                                if (answer == null) {
                                    appendToLog("No answer from client.");
                                    out.println("NO_ANSWER");
                                } else if (answer.trim().isEmpty()) {
                                    appendToLog("null answer.");
                                    out.println("INVALID_ANSWER");
                                } else {
                                    appendToLog("Received answer: " + answer);
                                    out.println("ANSWER_RECEIVED");
                                    // Xử lý logic với câu trả lời (nếu cần)
                                    processAnswer1(answer.substring(7), item);
                                }
                            } catch (SocketTimeoutException e) {
                                appendToLog("time out");
                                out.println("TIMEOUT");
                                // Chuyển sang câu hỏi tiếp theo
                                continue;
                            } catch (IOException e) {
                                appendToLog("error: " + e.getMessage());
                                out.println("ERROR");
                            }
                        }
                        socket.setSoTimeout(0);
                        // Khi đã hết câu hỏi, gửi "DONE" và hiển thị thông báo kết thúc
                        out.println("DONE");
                        appendToLog("All questions completed");
                    } else if (command.startsWith("START_FIRSTROUND")) {
                        for (var item : questions_round1) {
                            try {
                                String question = "Question@" + item.getCauHoi() + "@" + item.getDapAn();
                                out.println(question);
                                appendToLog("Sent: " + question);// Đặt thời gian chờ phản hồi từ client
                                socket.setSoTimeout(10000); // 40 giây  
                                String answer = in.readLine();

                                // Kiểm tra câu trả lời từ client
                                if (answer == null) {
                                    appendToLog("No answer from client.");
                                    out.println("NO_ANSWER");
                                } else if (answer.trim().isEmpty()) {
                                    appendToLog("null answer.");
                                    out.println("INVALID_ANSWER");
                                } else {
                                    appendToLog("Received answer: " + answer);
                                    out.println("ANSWER_RECEIVED");
                                    // Xử lý logic với câu trả lời (nếu cần)
                                    processAnswer(answer.substring(7), item);
                                }
                            } catch (SocketTimeoutException e) {
                                appendToLog("time out");
                                out.println("TIMEOUT");
                                // Chuyển sang câu hỏi tiếp theo
                                continue;
                            } catch (IOException e) {
                                appendToLog("error: " + e.getMessage());
                                out.println("ERROR");
                            }
                        }
                        socket.setSoTimeout(0);
                        // Khi đã hết câu hỏi, gửi "DONE" và hiển thị thông báo kết thúc
                        out.println("DONE");
                        appendToLog("All questions completed");
                    } else if (command.startsWith("START_THIRDROUND")) {
                        // Duyệt qua tất cả các câu hỏi
                        for (var user : currentscoreBoard){
                            user.setAnswered(false);
                        }
                        for (var item : questions_round3) {
                            try {
                                // Gửi câu hỏi tới client
                                String question = "Question@" + item.getLinkVideo() + "@" + item.getCauHoi();
                                out.println(question);
                                appendToLog("Sent: " + question);

                                // Đặt thời gian chờ phản hồi từ client
                                socket.setSoTimeout(30000); // 40 giây  

                                String answer = in.readLine();

                                // Kiểm tra câu trả lời từ client
                                if (answer == null) {
                                    appendToLog("No answer from client.");
                                    out.println("NO_ANSWER");
                                } else if (answer.trim().isEmpty()) {
                                    appendToLog("null answer.");
                                    out.println("INVALID_ANSWER");
                                } else {
                                    appendToLog("Received answer: " + answer);
                                    out.println("ANSWER_RECEIVED");
                                    // Xử lý logic với câu trả lời (nếu cần)
                                    processAnswer1(answer.substring(7), item);
                                }

                            } catch (SocketTimeoutException e) {
                                appendToLog("time out");
                                out.println("TIMEOUT");
                                // Chuyển sang câu hỏi tiếp theo
                            } catch (IOException e) {
                                appendToLog("error: " + e.getMessage());
                                out.println("ERROR");
                            }
                        }
                        socket.setSoTimeout(0);
                        // Khi đã hết câu hỏi, gửi "DONE" và hiển thị thông báo kết thúc
                        out.println("DONE");
                        appendToLog("All questions completed");
                    } else if (command.startsWith("DONE")) {
                        String[] parts = command.split("@", 2);
                        if (parts.length == 2) {
                            String playerName = parts[1];
                            markPlayerAsAnswered(playerName);
                            checkAndSendNextRound(); // Hàm mới để kiểm tra và gửi NEXTROUND
                        }
                    } else if (command.startsWith("DIEM")) {
                        String[] parts = command.split("@", 2);
                        if (parts.length == 2) {
                            String username = parts[1];
                            // Đọc tên người chơi
                            for (var user : currentscoreBoard) {
                                if (user.getUsername().equals(username)) {
                                    out.println("DIEM@" + user.getUsername() + "@" + user.getScore());
                                    break;
                                }
                            }
                        }
                    } else if (command.equals("RANK")) {
                        String rank = "RANK@" + diem.getTop4();
                        out.println(rank);
                    } else if (command.equals("RANKLOCAL")) {
                        StringBuilder rank = new StringBuilder();
                        // Sắp xếp danh sách giảm dần theo điểm
                        rank.append("RANK@");
                        currentscoreBoard.sort((s1, s2) -> Integer.compare(s2.getScore(), s1.getScore()));
                        // In danh sách đã sắp xếp
                        for (ScoreBoard sb : currentscoreBoard) {
                            rank.append(sb.getUsername()).append("@");
                        }
                        appendToLog(rank.toString());
                        out.println(rank.toString());
                    } else if (command.startsWith("ALLDONE")) {
                        currentscoreBoard.clear();
                    } else {
                        out.println("UNKNOWN_COMMAND: " + command);
                    }
                }
            } catch (IOException e) {
                appendToLog("Connection error: " + e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    appendToLog("Error closing socket: " + e.getMessage());
                }
            }
        }

        private void markPlayerAsAnswered(String playerName) {
            for (ScoreBoard sb : currentscoreBoard) {
                if (sb.getUsername().equals(playerName)) {
                    sb.setAnswered(true);
                    break;
                }
            }
        }

        private void checkAndSendNextRound() {
            boolean allAnswered = true;
            for (ScoreBoard sb : currentscoreBoard) {
                if (!sb.isAnswered()) {
                    allAnswered = false;
                    break;
                }
            }
            if (allAnswered) {
//                // Gửi NEXTROUND cho tất cả client
                for (ClientHandler client : clients) {
                    client.sendMessage("NEXTROUND");
                }
//                    out.println("NEXTROUND");
            }
        }

        private void processAnswer1(String answer, CauHoi question) {
            // Giả sử bạn kiểm tra xem câu trả lời có đúng hay không
            String[] parts = answer.split("@", 3);
            if (parts.length == 3) {
                String playerNames = parts[0];
                int diemso = Integer.parseInt(parts[1]);
                String answerPlayer = parts[2];

                if (answerPlayer.equalsIgnoreCase(question.getDapAnDung()) || answerPlayer.contains(question.getDapAnDung())) {
                    for (var item : currentscoreBoard) {
                        if (item.getUsername().equals(playerNames)) {
                            int d = item.getScore() + diemso;
                            item.setScore(d);
                            diem.updateScore(item.getId(), item.getScore());
                            out.println("CORRECT@" + playerNames + "@" + d);

                            for (var diem : currentscoreBoard) {
                                appendToLog(diem.getId() + " - " + diem.getScore());
                            }
                            break;
                        }
                    }
                } else if (answer.equals("NEXTQUESTION")) {
                    appendToLog("Next request");
                } else {
                    appendToLog("Incorrect: " + answer);
                    for (var item : currentscoreBoard) {
                        if (item.getUsername().equals(playerNames)) {
                            int d = item.getScore() - (diemso / 2);
                            item.setScore(d);
                            diem.updateScore(item.getId(), item.getScore());
                            out.println("INCORRECT@" + playerNames + "@" + d);
                            for (var diem : currentscoreBoard) {
                                appendToLog(diem.getId() + " - " + diem.getScore());
                            }
                            break;
                        }
                    }
                }
            }
        }

        private void processAnswer(String answer, CauHoi question) {
            // Giả sử bạn kiểm tra xem câu trả lời có đúng hay không
            String[] parts = answer.split("@", 3);
            if (parts.length == 3) {
                String playerNames = parts[0];
                int diemso = Integer.parseInt(parts[1]);
                String answerPlayer = parts[2];

                if (answerPlayer.equalsIgnoreCase(question.getDapAnDung()) || answerPlayer.contains(question.getDapAnDung())) {
                    for (var item : currentscoreBoard) {
                        if (item.getUsername().equals(playerNames)) {
                            int d = item.getScore() + diemso;
                            item.setScore(d);
                            diem.updateScore(item.getId(), item.getScore());
                            out.println("CORRECT@" + playerNames + "@" + d);

                            for (var diem : currentscoreBoard) {
                                appendToLog(diem.getId() + " - " + diem.getScore());
                            }
                            break;
                        }
                    }
                } else if (answer.equals("NEXTQUESTION")) {
                    appendToLog("Next request");
                } else {
                    appendToLog("Incorrect: " + answer);
                    out.println("INCORRECT");
                }
            }
        }

        private void handleRegister(String username, String password, PrintWriter out) {
            try {
                // Kết nối tới cơ sở dữ liệu
                var rs = access.Query("SELECT * FROM users WHERE username='" + username + "'");

                if (rs.next()) {
                    // Người dùng đã tồn tại
                    out.println("REGISTER_FAILED: Người dùng đã tồn tại !!!.");
                } else {
                    // Mã hóa mật khẩu
                    String hashedPassword = PasswordUtils.hashPassword(password);

                    // Thêm vào database
                    int result = access.RegisterUser("INSERT INTO users (id, username, password) VALUES('"
                            + LocalDateTime.now().getNano() + "','" + username + "','" + hashedPassword + "')");

                    if (result == 1) {
                        out.println("REGISTER_SUCCESS");
                    } else {
                        out.println("REGISTER_FAILED: Lỗi khi kết nối đến server.");
                    }
                }
            } catch (Exception e) {
                out.println("REGISTER_FAILED: " + e.getMessage());
            }
        }

        private void handleLogin(String username, String password, PrintWriter out) {
            try {

                String hashedPassword = PasswordUtils.hashPassword(password);

                var rs = access.Query("SELECT * FROM users WHERE username='" + username + "' AND password='" + hashedPassword + "'");

                if (rs.next()) {
                    out.println("LOGIN_SUCCESS");
                } else {
                    out.println("LOGIN_FAILED: Sai tên đăng nhập hoặc mật khẩu.");
                }
            } catch (Exception e) {
                out.println("LOGIN_FAILED: " + e.getMessage());
            }
        }
    }

    private void appendToLog(String message) {
        SwingUtilities.invokeLater(() -> {
            serverLog.append(LocalDateTime.now() + ": " + message + "\n");
            serverLog.setCaretPosition(serverLog.getDocument().getLength()); // Scroll to bottom
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ServerUI::new);
    }
}
