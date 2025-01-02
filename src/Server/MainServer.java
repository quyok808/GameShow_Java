package Server;

/**
 * 2180603884 - 2180609157 - 2180608439 - NGUYEN CONG QUY - NGUYEN THI HONG VI -
 * NGUYEN MINH TRI - NGUYEN XUAN HUY - PHAN VU BANG
 */
import ApplicationDbContext.*;
import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.swing.JOptionPane;

public class MainServer {

    private static final int PORT = 12345;
    private static final List<ClientHandler> clients = new ArrayList<>();
    private static final Queue<ClientHandler> waitingQueue = new ConcurrentLinkedQueue<>();
    private static final List<String[]> questions_round1 = new ArrayList<>();
    private static final List<String[]> questions_round2 = new ArrayList<>();
    private static final List<String[]> questions_round3 = new ArrayList<>();
    private static final List<String[]> questions_round4 = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server is running on port " + PORT + "...");

            // Chấp nhận kết nối từ client
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected!");
                ClientHandler clientHandler = new ClientHandler(socket);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (java.net.BindException ex) {
            JOptionPane.showMessageDialog(null, "Server đã khởi động từ trước !!!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private static int loadQuestionsFromFile(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|"); // Tách dữ liệu theo dấu "|"
                if (parts.length == 3) {
                    String videoPath = parts[0];
                    String question = parts[1].substring(2); // Bỏ qua "Q." ở đầu câu hỏi
                    String answer = parts[2].substring(5); // Bỏ qua "Ans." ở đầu câu trả lời
                    System.out.println("Video Path: " + videoPath);
                    System.out.println("Question: " + question);
                    System.out.println("Answer: " + answer);
                    return 1;
                } else {
                    System.out.println("Invalid data format in line: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private synchronized static void updateWaitingRoom() {
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

        if (waitingQueue.size() >= 4) { // Chờ đủ 4 người chơi
            new Thread(() -> {
                try {
                    // Đếm ngược 10 giây
                    for (int i = 10; i > 0; i--) {
                        broadcastWaitingRoom("Game starts in " + i + " seconds...");
                        Thread.sleep(1000);
                    }

                    // Lấy 4 người chơi từ phòng chờ
                    List<ClientHandler> players = new ArrayList<>();
                    for (int i = 0; i < 4; i++) {
                        players.add(waitingQueue.poll());
                    }

                    // Kiểm tra xem có đủ 4 người chơi không
                    if (players.size() < 4) {
                        // Nếu không đủ người chơi, thông báo lỗi và kết thúc trò chơi
                        for (ClientHandler player : players) {
                            player.sendMessage("Not enough players, game cannot start.");
                        }
                        return;
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

    private static void sendQuestion(ClientHandler client, String[] questionData) throws IOException {
        client.sendMessage("Question:" + questionData[0]);
        client.sendMessage(questionData[1]);
    }

    private static void evaluateAnswer(ClientHandler client, String answer, String correctAnswer) {
        if (answer != null && answer.contains(correctAnswer)) {
            //Lưu điểm
            client.increaseScore();
            client.sendMessage("Correct!");
        } else {
            client.sendMessage("Wrong! The correct answer is " + correctAnswer + ".");
        }
    }

    private static void startGame(List<ClientHandler> players) {
        new Thread(() -> {
            // Trò chơi bắt đầu
            // Ví dụ ở đây có thể chỉ đơn giản là thông báo rằng trò chơi đã bắt đầu mà không cần câu hỏi
            for (ClientHandler player : players) {
                player.sendMessage("Game started! Good luck!");
            }
        }).start();
    }

    private static void sendQuestionsForRound(List<ClientHandler> players, List<String[]> questions) throws InterruptedException, IOException {
        for (int i = 0; i < questions.size(); i++) {
            String[] questionData = questions.get(i); // Lấy câu hỏi hiện tại

            // Gửi câu hỏi cho tất cả các player
            for (ClientHandler player : players) {
                sendQuestion(player, questionData); // Gửi câu hỏi
            }

            // Đợi nhận câu trả lời từ tất cả người chơi
            for (ClientHandler player : players) {
                String answer = player.in.readLine(); // Đọc câu trả lời từ client
                evaluateAnswer(player, answer, questionData[2]); // Đánh giá câu trả lời
            }

            // Nghỉ 2 giây trước khi gửi câu hỏi tiếp theo
            Thread.sleep(2000);
        }
    }

    private static void Round4() {
        new Thread(() -> {
            try {
                List<ClientHandler> players = new ArrayList<>(waitingQueue); // Lấy danh sách người chơi từ hàng đợi
                if (players.size() < 4) {
                    for (ClientHandler player : players) {
                        player.sendMessage("Not enough players for Round 4. Please wait.");
                    }
                    return;
                }

                // Giới hạn số lượng người chơi là 4
                players = players.subList(0, 4);
                for (ClientHandler player : players) {
                    waitingQueue.remove(player);
                }

                // Bắt đầu vòng 4
                for (ClientHandler player : players) {
                    player.sendMessage("Round 4 is starting!");
                }
                System.out.println("Questions in Round 4: " + questions_round4);

                // Gửi các câu hỏi vòng 4
                sendQuestionsForRound(players, questions_round4);

                // Kết thúc vòng chơi
                for (ClientHandler player : players) {
                    player.sendMessage("Round 4 has ended. Your final score: " + player.getScore());
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    // Phương thức giả để load câu hỏi dựa trên gói câu hỏi
    private static String loadQuestions(String goiCauHoi) {
        // Giả sử đây là logic để load câu hỏi từ cơ sở dữ liệu hoặc nguồn dữ liệu
        if (goiCauHoi.equals("GOI 1")) {
            int kq = loadQuestionsFromFile("../test2/src/RoundQuestions/FouthRound/GOI1/Questions.txt");
            if (kq == 1) {
                return "1"; // Thành công
            }
        } else if (goiCauHoi.equals("GOI 2")) {
            // Load câu hỏi cho gói 2
            return "1"; // Thành công
        } else if (goiCauHoi.equals("GOI 3")) {
            // Load câu hỏi cho gói 3
            return "1"; // Thành công
        }
        return "0"; // Lỗi khi load câu hỏi

    }

//============================================================================================================
    // Lớp xử lý client
    private static class ClientHandler implements Runnable {

        private final Socket socket;
        private final PrintWriter out;
        private final BufferedReader in;
        private String playerName;
        private int score = 0; // Điểm số của người chơi

        public void sendMessage(String message) {
            out.println(message);
        }

        public String getPlayerName() {
            return playerName;
        }

        public void setPlayerName(String playerName) {
            this.playerName = playerName;
        }

        public int getScore() {
            return score;
        }

        public void increaseScore() {
            score += 1; // Tăng điểm
        }

        public String readAnswer() throws IOException {
            return in.readLine(); // Đọc câu trả lời từ client
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
                System.out.println("Client disconnected: " + e.getMessage());
            } finally {
                cleanup();
            }
        }

        private void cleanup() {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("Error closing socket: " + e.getMessage());
            }

            // Loại bỏ client khỏi hàng đợi chờ và danh sách client
            synchronized (waitingQueue) {
                waitingQueue.remove(this);
            }
            synchronized (clients) {
                clients.remove(this);
            }

            // Cập nhật lại phòng chờ
            MainServer.updateWaitingRoom();
            System.out.println("Client has been left.");
        }

        private void handleClient() throws IOException {
            try {
                String command;
                while ((command = in.readLine()) != null) { // Đảm bảo không có null khi đọc lệnh
                    if (command.trim().isEmpty()) {
                        continue; // Bỏ qua lệnh rỗng
                    }
                    System.out.println("Received command: " + command); // In ra log để kiểm tra lệnh nhận được

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
                            out.println("Hello, " + this.playerName + "!");
                        }
                    } else if ("Pair".equalsIgnoreCase(command)) {
                        pairClients(this);
                    } else if ("Unpair".equalsIgnoreCase(command)) {
                        cleanup();
                    } else if (command.equals("GOI 1") || command.equals("GOI 2") || command.equals("GOI 3")) {
                        // Xử lý gói câu hỏi
                        System.out.println("Player chose " + command);
                        // Thực hiện logic để load câu hỏi và gửi về client
                        String result = loadQuestions(command);
                        out.println("LOADING " + result);
                    } else if (command.startsWith("START_FOUTHROUND")) {
                        Round4();
                    } else {
                        out.println("UNKNOWN_COMMAND: " + command);
                    }
                }
            } catch (IOException e) {
                System.out.println("Connection error: " + e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println("Error closing socket: " + e.getMessage());
                }
            }
        }

        private static void handleRegister(String username, String password, PrintWriter out) {
            try {
                // Kết nối tới cơ sở dữ liệu
                DBAccess access = new DBAccess();
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

        private static void handleLogin(String username, String password, PrintWriter out) {
            try {
                DBAccess access = new DBAccess();
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
}
