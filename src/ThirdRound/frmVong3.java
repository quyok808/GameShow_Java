/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ThirdRound;

import FouthRound.frmVong4;
import Home.frmRank;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 *
 * PHAN VU BANG
 */
public class frmVong3 extends javax.swing.JFrame {

    private static final int PORT = 12345;
    private static final String HOST = "25.33.107.197";
    private String playerName;
    private PrintWriter out;
    private BufferedReader in;
    private Socket socket;
    private int countdownTime = 30; // Thời gian đếm ngược (40 giây)
    private Timer countdownTimer; // Bộ hẹn giờ

    /**
     * Creates new form frmVong3
     */
    public frmVong3() {
        initComponents();
        lb_HinhANh.setIcon(ResizeImage(String.valueOf("../test2/src/RoundQuestions/ThirdRound/q6.jpg")));
    }

    public frmVong3(String playerName) {
        initComponents();
        try {
            this.socket = new Socket(HOST, PORT);
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
    }

    private void connectToServer() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            sendToServer("DIEM@" + this.playerName);
            sendToServer("START_THIRDROUND");

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
                System.out.println("Vong 3 Receive: " + message);
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
                    String path = parts[1];
                    String question = parts[2];
                    lb_CauHoi.setText(question);

                    // Hiển thị hình ảnh từ đường dẫn path
                    try {
                        if (path != null) {
                            lb_HinhANh.setIcon(ResizeImage(String.valueOf(path)));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(this, "Không thể hiển thị hình ảnh: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
                // Đặt lại thời gian đếm ngược về 40 giây
                countdownTime = 30;
                // Khởi động lại đếm ngược
                startCountdown();
            } else if (message.startsWith("CORRECT")) {
                String[] parts = message.split("@", 3);
                if (parts.length == 3) {
                    if (this.playerName.equals(parts[1])) {
                        lb_info.setText("Thí sinh: " + this.playerName + " - " + parts[2] + " điểm");
                    }
                }
                JOptionPane.showMessageDialog(this, "Câu trả lời đúng!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } else if (message.startsWith("INCORRECT")) {
                JOptionPane.showMessageDialog(this, "Câu trả lời sai!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                String[] parts = message.split("@", 3);
                if (parts.length == 3) {
                    if (this.playerName.equals(parts[1])) {
                        lb_info.setText("Thí sinh: " + this.playerName + " - " + parts[2] + " điểm");
                    }
                }
            } else if (message.startsWith("EXPLANATION")) {
                String explanation = message.split("@", 2)[1];
                JOptionPane.showMessageDialog(this, "Giải thích: " + explanation, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } else if (message.startsWith("DONE")) {
                countdownTimer.stop();
                btn_Submit.setEnabled(false);
                txt_TraLoi.setEditable(false);
                // gửi thông báo cho server đã hoàn thành vòng
                 sendToServer("DONE@" + this.playerName);
                JOptionPane.showMessageDialog(this, "Chúc mừng " + playerName + " đã hoàn thành vòng thi, hãy đợi các người chơi khác hoàn thành!!!");
                
            }else if(message.startsWith("NEXTROUND"))
             {
                frmRank obj = new frmRank(this.playerName, "frmVong4");
                obj.setVisible(true);
                this.dispose();
            } else if (message.startsWith("TIMEOUT")) {
                System.out.println("Time out");
            } else if (message.startsWith("DIEM")) {
                String[] parts = message.split("@", 3);
                if (parts.length == 3) {
                    if (this.playerName.equals(parts[1])) {
                        lb_info.setText("Thí sinh: " + this.playerName + " - " + parts[2] + " điểm");
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
                    lb_timer.setText(countdownTime + "");
                } else {
                    countdownTimer.stop(); // Dừng bộ hẹn giờ khi hết thời gian
                    lb_timer.setText("0");
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

    public ImageIcon ResizeImage(String ImagePath) {
        ImageIcon MyImage = new ImageIcon(ImagePath);
        Image img = MyImage.getImage();
        Image newImg = img.getScaledInstance(lb_HinhANh.getWidth(), lb_HinhANh.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon image = new ImageIcon(newImg);
        return image;
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
        lb_info = new javax.swing.JLabel();
        btn_Submit = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txt_TraLoi = new javax.swing.JTextArea();
        lb_CauHoi = new javax.swing.JLabel();
        lb_timer = new javax.swing.JLabel();
        lb_HinhANh = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("DejaVu Serif", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Nhìn hình đoán chữ");

        lb_info.setFont(new java.awt.Font("DejaVu Serif", 3, 14)); // NOI18N
        lb_info.setText("Thí sinh: Nguyễn Văn A -  120 điểm");

        btn_Submit.setBackground(new java.awt.Color(153, 255, 153));
        btn_Submit.setFont(new java.awt.Font("DejaVu Serif", 0, 14)); // NOI18N
        btn_Submit.setText("Trả lời");
        btn_Submit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_Submit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_SubmitActionPerformed(evt);
            }
        });

        txt_TraLoi.setColumns(20);
        txt_TraLoi.setFont(new java.awt.Font("DejaVu Serif Condensed", 2, 13)); // NOI18N
        txt_TraLoi.setRows(5);
        txt_TraLoi.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        txt_TraLoi.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        jScrollPane1.setViewportView(txt_TraLoi);

        lb_CauHoi.setFont(new java.awt.Font("DejaVu Serif", 1, 14)); // NOI18N
        lb_CauHoi.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb_CauHoi.setText("Câu hỏi");

        lb_timer.setFont(new java.awt.Font("DejaVu Serif", 1, 36)); // NOI18N
        lb_timer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb_timer.setText("30");

        lb_HinhANh.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb_HinhANh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/RoundQuestions/ThirdRound/q2.jpg"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lb_HinhANh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lb_info, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 697, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btn_Submit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lb_timer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(lb_CauHoi, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lb_info)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lb_HinhANh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lb_CauHoi)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lb_timer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_Submit, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_SubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SubmitActionPerformed
        // Lấy nội dung câu trả lời từ ô nhập liệu
        String answer = txt_TraLoi.getText().trim();
        int diemso = 0;

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
            java.util.logging.Logger.getLogger(frmVong3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmVong3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmVong3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmVong3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmVong3().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Submit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lb_CauHoi;
    private javax.swing.JLabel lb_HinhANh;
    private javax.swing.JLabel lb_info;
    private javax.swing.JLabel lb_timer;
    private javax.swing.JTextArea txt_TraLoi;
    // End of variables declaration//GEN-END:variables
}
