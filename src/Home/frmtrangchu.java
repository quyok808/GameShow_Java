package Home;

import Identity.frmLogin;
import javax.swing.JOptionPane;

/**
 * 2180609157
 * NGUYEN THI HONG VI
 */
public class frmtrangchu extends javax.swing.JFrame {

    private String playerName;

    /**
     * Creates new form frmtrangchu
     *
     * @param playerName
     */
    public frmtrangchu(String playerName) {
        this.playerName = playerName;
        initComponents();
        if (playerName.equals("Khách")){
            btn_Logout.setVisible(false);
        }
        else {
            btn_Logout.setVisible(true);
        }
        lbtennguoichoi.setText("Xin chào " + playerName);
        setLocationRelativeTo(null);
        updateLoginButtonVisibility();
    }

    public frmtrangchu() {
        this("Khách");
    }

    private void updateLoginButtonVisibility() {
        if (playerName != null && !playerName.equals("Khách")) {
            btn_Login.setVisible(false); // Ẩn nút Đăng nhập nếu đã đăng nhập
            btn_Logout.setVisible(true);
        } else {
            btn_Login.setVisible(true); // Hiển thị nút Đăng nhập nếu là khách
            btn_Logout.setVisible(false);   
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btn_startgame = new javax.swing.JButton();
        bangxephang = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lbtennguoichoi = new javax.swing.JLabel();
        btn_Login = new javax.swing.JButton();
        btn_Logout = new javax.swing.JButton();
        btn_history = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(new java.awt.Dimension(800, 600));

        btn_startgame.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_startgame.setText("Start game");
        btn_startgame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_startgameActionPerformed(evt);
            }
        });

        bangxephang.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        bangxephang.setMaximumSize(new java.awt.Dimension(500, 450));
        bangxephang.setPreferredSize(new java.awt.Dimension(500, 490));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("BẢNG XẾP HẠNG");

        javax.swing.GroupLayout bangxephangLayout = new javax.swing.GroupLayout(bangxephang);
        bangxephang.setLayout(bangxephangLayout);
        bangxephangLayout.setHorizontalGroup(
            bangxephangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bangxephangLayout.createSequentialGroup()
                .addContainerGap(184, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(165, 165, 165))
        );
        bangxephangLayout.setVerticalGroup(
            bangxephangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bangxephangLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(457, Short.MAX_VALUE))
        );

        lbtennguoichoi.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbtennguoichoi.setText("Tennguoidangnhap");

        btn_Login.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_Login.setText("Đăng nhập");
        btn_Login.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_LoginActionPerformed(evt);
            }
        });

        btn_Logout.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_Logout.setText("Đăng xuất");
        btn_Logout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_LogoutActionPerformed(evt);
            }
        });

        btn_history.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_history.setText("Lịch sử đấu");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbtennguoichoi, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_Login, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_Logout)
                        .addGap(18, 18, 18))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btn_startgame, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btn_history, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addComponent(bangxephang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(16, 16, 16))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_Logout, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lbtennguoichoi)
                        .addComponent(btn_Login, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bangxephang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btn_startgame, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btn_history, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btn_startgameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_startgameActionPerformed
        if (playerName == null || playerName.equals("Khách")) {
            JOptionPane.showMessageDialog(this, "Vui lòng đăng nhập trước khi bắt đầu trò chơi!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        //frmClient obj = new frmClient(playerName);
        frmphongcho obj = new frmphongcho(playerName);
        obj.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btn_startgameActionPerformed

    private void btn_LoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_LoginActionPerformed
        frmLogin obj = new frmLogin();
        obj.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btn_LoginActionPerformed

    private void btn_LogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_LogoutActionPerformed
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn đăng xuất?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            this.playerName = "Khách";
            // Mở lại form đăng nhập
            lbtennguoichoi.setText("Xin chào " + playerName);
            updateLoginButtonVisibility();
        }
    }//GEN-LAST:event_btn_LogoutActionPerformed

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
            java.util.logging.Logger.getLogger(frmtrangchu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmtrangchu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmtrangchu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmtrangchu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmtrangchu().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bangxephang;
    private javax.swing.JButton btn_Login;
    private javax.swing.JButton btn_Logout;
    private javax.swing.JButton btn_history;
    private javax.swing.JButton btn_startgame;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lbtennguoichoi;
    // End of variables declaration//GEN-END:variables
}
