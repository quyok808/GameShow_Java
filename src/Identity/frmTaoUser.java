package Identity;

import java.awt.event.KeyEvent;
import java.io.*;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 * 2180609157
 * NGUYEN THI HONG VI
 */
public class frmTaoUser extends javax.swing.JFrame {

    private boolean showPassword = false;

    /**
     * Creates new form frmTaoUser
     */
    public frmTaoUser() {
        initComponents();
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
        jLabel2 = new javax.swing.JLabel();
        txtTen = new javax.swing.JTextField();
        btnThem = new javax.swing.JButton();
        btnHuy = new javax.swing.JButton();
        txtMatKhau = new javax.swing.JPasswordField();
        btn_ShowPass = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("DejaVu Serif", 0, 12)); // NOI18N
        jLabel1.setText("Tên");

        jLabel2.setFont(new java.awt.Font("DejaVu Serif", 0, 12)); // NOI18N
        jLabel2.setText("Mật Khẩu");

        txtTen.setFont(new java.awt.Font("DejaVu Serif", 0, 12)); // NOI18N

        btnThem.setFont(new java.awt.Font("DejaVu Serif", 1, 14)); // NOI18N
        btnThem.setText("Đăng Ký");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        btnHuy.setFont(new java.awt.Font("DejaVu Serif", 1, 14)); // NOI18N
        btnHuy.setText("Huỷ");
        btnHuy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHuyActionPerformed(evt);
            }
        });

        txtMatKhau.setFont(new java.awt.Font("DejaVu Serif", 0, 12)); // NOI18N
        txtMatKhau.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        txtMatKhau.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtMatKhauKeyPressed(evt);
            }
        });

        btn_ShowPass.setFont(new java.awt.Font("DejaVu Serif", 2, 12)); // NOI18N
        btn_ShowPass.setText("Show Password");
        btn_ShowPass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ShowPassActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtMatKhau, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_ShowPass, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(txtTen))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGap(70, 70, 70)
                        .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnHuy, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(56, 56, 56))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtTen, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtMatKhau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_ShowPass))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHuy, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        handleRegister();
    }//GEN-LAST:event_btnThemActionPerformed

    private void handleRegister() {
        try {
            String ten = txtTen.getText();
            String matKhau = txtMatKhau.getText();

            // Kiểm tra username và password không rỗng
            if (ten.isEmpty() || matKhau.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
                return;
            }

            // Kết nối tới server
            try (Socket socket = new Socket("127.0.0.1", 12345); PrintWriter out = new PrintWriter(socket.getOutputStream(), true); BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                // Gửi lệnh đăng ký tới server
                String command = "REGISTER " + ten + " " + matKhau;
                out.println(command);

                // Nhận phản hồi từ server
                String response = in.readLine();
                if ("REGISTER_SUCCESS".equals(response)) {
                    JOptionPane.showMessageDialog(this, "Thêm User Thành Công !!!");
                    frmLogin obj = new frmLogin();
                    obj.setVisible(true);
                    this.dispose();
                } else if (response.startsWith("REGISTER_FAILED")) {
                    JOptionPane.showMessageDialog(this, response.substring(17)); // Hiển thị thông báo lỗi
                } else {
                    JOptionPane.showMessageDialog(this, "Phản hồi không xác định từ server!");
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Không thể kết nói đến server. Hãy liên hệ Admin để xử lý !!! ", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }

    private void btnHuyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHuyActionPerformed
        frmLogin obj = new frmLogin();
        obj.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnHuyActionPerformed

    private void btn_ShowPassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ShowPassActionPerformed
        showPassword = !showPassword;

        if (showPassword) {
            txtMatKhau.setEchoChar('\u0000');
            btn_ShowPass.setText("Hide Password");
        } else {
            txtMatKhau.setEchoChar('\u2022');
            btn_ShowPass.setText("Show Password");
        }
    }//GEN-LAST:event_btn_ShowPassActionPerformed

    private void txtMatKhauKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMatKhauKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            handleRegister();
        }
    }//GEN-LAST:event_txtMatKhauKeyPressed

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
            java.util.logging.Logger.getLogger(frmTaoUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmTaoUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmTaoUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmTaoUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new frmTaoUser().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnHuy;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btn_ShowPass;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPasswordField txtMatKhau;
    private javax.swing.JTextField txtTen;
    // End of variables declaration//GEN-END:variables
}
