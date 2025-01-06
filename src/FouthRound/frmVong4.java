package FouthRound;

import Home.frmRank;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.media.*;
import javax.swing.*;

/**
 * 2180603884 NGUYEN CONG QUY
 */
public class frmVong4 extends javax.swing.JFrame {

    private static final int PORT = 12345;
    private static final String HOST = "25.33.107.197";
    private PrintWriter out;
    private BufferedReader in;
    private Socket socket;
    private String path;
    private MediaPlayer currentMediaPlayer;
    private String playerName;
    private int countdownTime = 40; // Thời gian đếm ngược (40 giây)
    private Timer countdownTimer; // Bộ hẹn giờ

    /**
     * Creates new form frmVong4
     */
    public frmVong4() {
        initComponents();
        initVideoPanel(panel_Video);
//        try {
//            this.socket = new Socket(HOST, PORT);
//        } catch (IOException ex) {
//            Logger.getLogger(frmVong4.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        connectToServer();
    }

    public frmVong4(String goiCauHoi, String playerName) {
        initComponents();
        try {
            this.socket = new Socket("localhost", 12345);
            this.playerName = playerName;
            connectToServer();
        } catch (IOException ex) {
            try {
                socket.close();
            } catch (IOException ex1) {
                Logger.getLogger(frmVong4.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(frmVong4.class.getName()).log(Level.SEVERE, null, ex);
        }
        lb_GoiCauHoi.setText(goiCauHoi);

    }

    public frmVong4(String goiCauHoi) {
        initComponents();
        try {
            this.socket = new Socket("localhost", 12345);

        } catch (IOException ex) {
            try {
                socket.close();
            } catch (IOException ex1) {
                Logger.getLogger(frmVong4.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(frmVong4.class.getName()).log(Level.SEVERE, null, ex);
        }
        lb_GoiCauHoi.setText(goiCauHoi);
        connectToServer();
    }

    private void connectToServer() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            sendToServer("DIEM@" + this.playerName);
            sendToServer("START_FOUTHROUND");

            // Tạo luồng đọc từ server
            new Thread(this::readMessagesFromServer).start();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Unable to connect to server: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    private void readMessagesFromServer() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                processServerMessage(message);

            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Connection lost: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void processServerMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            if (message.startsWith("Question")) {
                // Dừng bộ hẹn giờ nếu nó đang chạy
                if (countdownTimer != null && countdownTimer.isRunning()) {
                    countdownTimer.stop();
                }
                String[] parts = message.split("@", 3);
                if (parts.length == 3) {
                    path = parts[1];
                    lb_question.setText(parts[2]);
                    initVideoPanel(panel_Video); // Khởi tạo lại video panel với đường dẫn mới
                }
                // Đặt lại thời gian đếm ngược về 40 giây
                countdownTime = 40;
                // Khởi động lại đếm ngược
                startCountdown();
            } else if (message.startsWith("CORRECT")) {
                String[] parts = message.split("@", 3);
                if (parts.length == 3) {
                    if (this.playerName.equals(parts[1])) {
                        lb_Diem.setText(parts[2] + " điểm");
                    }
                }
                JOptionPane.showMessageDialog(this, "Câu trả lời đúng!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } else if (message.startsWith("INCORRECT")) {
                JOptionPane.showMessageDialog(this, "Câu trả lời sai!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                String[] parts = message.split("@", 3);
                if (parts.length == 3) {
                    if (this.playerName.equals(parts[1])) {
                        lb_Diem.setText(parts[2] + " điểm");
                    }
                }
            } else if (message.startsWith("EXPLANATION")) {
                String explanation = message.split("@", 2)[1];
                JOptionPane.showMessageDialog(this, "Giải thích: " + explanation, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } else if (message.startsWith("DONE")) {
                countdownTimer.stop();
                // gửi thông báo cho server đã hoàn thành vòng
                sendToServer("DONE@" + this.playerName);
                JOptionPane.showMessageDialog(this, "Chúc mừng " + playerName + " đã hoàn thành vòng thi, hãy đợi các người chơi khác hoàn thành!!!");

            } else if (message.startsWith("NEXTROUND")) {
                frmRank obj = new frmRank(this.playerName, "frmTrangChu");
                obj.setVisible(true);
                this.dispose();
            } else if (message.startsWith("TIMEOUT")) {
                JOptionPane.showMessageDialog(null, "Hết thời gian trả lời !!!");
            } else if (message.startsWith("DIEM")) {
                String[] parts = message.split("@", 3);
                if (parts.length == 3) {
                    if (this.playerName.equals(parts[1])) {
                        lb_Diem.setText(parts[2] + " điểm");
                    }
                }
            }
        });
    }

    // Hàm khởi động bộ đếm ngược
    private void startCountdown() {
        // Khởi động lại bộ đếm ngược từ 40 giây
        countdownTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (countdownTime > 0) {
                    countdownTime--; // Giảm thời gian còn lại
                    label_time.setText(countdownTime + "");
                } else {
                    countdownTimer.stop(); // Dừng bộ hẹn giờ khi hết thời gian
                    label_time.setText("0");
                    // Tiến hành hành động sau khi hết thời gian, ví dụ: chuyển sang câu hỏi tiếp theo
                }
            }
        });
        countdownTimer.start(); // Bắt đầu đếm ngược
    }

    private void sendToServer(String message) {
        if (out != null) {
            out.println(message);
            System.out.println("send to server: " + message);
        }
    }

    private void initVideoPanel(JPanel panel) {
        // Khởi tạo JFXPanel
        JFXPanel jfxPanel = new JFXPanel();
        jfxPanel.setPreferredSize(new Dimension(panel.getWidth(), panel.getHeight())); // Thiết lập kích thước cho JFXPanel
        panel.setLayout(new BorderLayout());
        panel.removeAll(); // Xóa các thành phần cũ
        panel.add(jfxPanel, BorderLayout.CENTER);
        panel.revalidate(); // Cập nhật giao diện
        panel.repaint();

        // Dừng MediaPlayer hiện tại nếu có
        if (currentMediaPlayer != null) {
            currentMediaPlayer.stop();
            currentMediaPlayer.dispose(); // Giải phóng tài nguyên
        }

        // Chạy trên JavaFX Application Thread
        Platform.runLater(() -> {
            try {
//                String videoPath = "file:///" + path; // Đảm bảo đúng đường dẫn video
                String videoPath = Paths.get(path).toAbsolutePath().toUri().toString();
                Media media = new Media(videoPath);

                if (media.getError() != null) {
                    System.err.println("Error loading media: " + media.getError().getMessage());
                    JOptionPane.showMessageDialog(panel, "Lỗi tải video: " + media.getError().getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Khởi tạo MediaPlayer và MediaView
                currentMediaPlayer = new MediaPlayer(media);
                MediaView mediaView = new MediaView(currentMediaPlayer);

                // Tạo StackPane để căn giữa MediaView
                javafx.scene.layout.StackPane stackPane = new javafx.scene.layout.StackPane();
                stackPane.getChildren().add(mediaView);

                // Thiết lập kích thước MediaView theo kích thước của panel
                mediaView.setFitWidth(panel.getWidth());
                mediaView.setFitHeight(panel.getHeight());

                // Tạo Scene với StackPane và thêm vào JFXPanel
                Scene scene = new Scene(stackPane);
                jfxPanel.setScene(scene);

                // Phát video
                currentMediaPlayer.play();

                // Dừng video khi kết thúc
                currentMediaPlayer.setOnEndOfMedia(() -> {
                    currentMediaPlayer.stop();
                    currentMediaPlayer.dispose(); // Giải phóng tài nguyên
                });

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(panel, "Không thể phát video. Vui lòng kiểm tra đường dẫn!");
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        panel_Video = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txt_TraLoi = new javax.swing.JTextArea();
        btn_Submit = new javax.swing.JButton();
        btn_Next = new javax.swing.JButton();
        lb_GoiCauHoi = new javax.swing.JLabel();
        lb_Diem = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        jSeparator6 = new javax.swing.JSeparator();
        jSeparator7 = new javax.swing.JSeparator();
        lb_question = new javax.swing.JLabel();
        label_time = new RoundLabel("123");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("DejaVu Serif", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Về đích");
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        panel_Video.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        javax.swing.GroupLayout panel_VideoLayout = new javax.swing.GroupLayout(panel_Video);
        panel_Video.setLayout(panel_VideoLayout);
        panel_VideoLayout.setHorizontalGroup(
            panel_VideoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 798, Short.MAX_VALUE)
        );
        panel_VideoLayout.setVerticalGroup(
            panel_VideoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 418, Short.MAX_VALUE)
        );

        txt_TraLoi.setColumns(20);
        txt_TraLoi.setFont(new java.awt.Font("DejaVu Serif Condensed", 2, 13)); // NOI18N
        txt_TraLoi.setRows(5);
        txt_TraLoi.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        txt_TraLoi.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        jScrollPane1.setViewportView(txt_TraLoi);

        btn_Submit.setBackground(new java.awt.Color(153, 255, 153));
        btn_Submit.setFont(new java.awt.Font("DejaVu Serif", 0, 14)); // NOI18N
        btn_Submit.setText("Trả lời");
        btn_Submit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_Submit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_SubmitActionPerformed(evt);
            }
        });

        btn_Next.setBackground(new java.awt.Color(255, 153, 153));
        btn_Next.setFont(new java.awt.Font("DejaVu Serif", 0, 14)); // NOI18N
        btn_Next.setText("Bỏ qua");
        btn_Next.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_Next.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_NextActionPerformed(evt);
            }
        });

        lb_GoiCauHoi.setFont(new java.awt.Font("DejaVu Serif", 3, 14)); // NOI18N
        lb_GoiCauHoi.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb_GoiCauHoi.setText("Gói câu hỏi 90 điểm");

        lb_Diem.setFont(new java.awt.Font("DejaVu Serif", 3, 14)); // NOI18N
        lb_Diem.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb_Diem.setText("180 điểm");

        jSeparator5.setForeground(new java.awt.Color(153, 153, 153));

        jSeparator6.setForeground(new java.awt.Color(153, 153, 153));

        jSeparator7.setForeground(new java.awt.Color(153, 153, 153));

        lb_question.setFont(new java.awt.Font("DejaVu Serif", 1, 13)); // NOI18N
        lb_question.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb_question.setText("Câu hỏi");

        label_time.setFont(new java.awt.Font("DejaVu Serif", 1, 14)); // NOI18N
        label_time.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_time.setText("40");
        label_time.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 697, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btn_Next, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btn_Submit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lb_question, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lb_GoiCauHoi)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(4, 4, 4)
                                        .addComponent(lb_Diem))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(86, 86, 86)
                                        .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(label_time, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(panel_Video, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_Video, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lb_question)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lb_GoiCauHoi, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lb_Diem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(label_time, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, Short.MAX_VALUE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btn_Next, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(btn_Submit, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btn_SubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SubmitActionPerformed
        // Lấy nội dung câu trả lời từ ô nhập liệu
        String answer = txt_TraLoi.getText().trim();
        int diemso = 0;
        String diem = lb_GoiCauHoi.getText();
        if (diem.contains("90")) {
            diemso = 30;
        } else if (diem.contains("60")) {
            diemso = 20;
        } else if (diem.contains("30")) {
            diemso = 10;
        }
        // Kiểm tra câu trả lời có rỗng không
        if (answer.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập câu trả lời!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Gửi câu trả lời đến server
        sendToServer("ANSWER@" + this.playerName + "@" + diemso + "@" + answer);
        // Xóa nội dung ô nhập liệu sau khi gửi
        txt_TraLoi.setText("");
    }//GEN-LAST:event_btn_SubmitActionPerformed

    private void btn_NextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_NextActionPerformed
        // Lấy nội dung câu trả lời từ ô nhập liệu
        String answer = "NEXTQUESTION";

        // Gửi câu trả lời đến server
        sendToServer("ANSWER@" + answer);

        // Xóa nội dung ô nhập liệu sau khi gửi
        txt_TraLoi.setText("");
    }//GEN-LAST:event_btn_NextActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frmVong4.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmVong4.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmVong4.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmVong4.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmVong4().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Next;
    private javax.swing.JButton btn_Submit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JLabel label_time;
    private javax.swing.JLabel lb_Diem;
    private javax.swing.JLabel lb_GoiCauHoi;
    private javax.swing.JLabel lb_question;
    private javax.swing.JPanel panel_Video;
    private javax.swing.JTextArea txt_TraLoi;
    // End of variables declaration//GEN-END:variables
}
