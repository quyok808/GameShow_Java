/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FouthRound;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import javax.swing.*;

/**
 * 2180603884
 * NGUYEN CONG QUY
 */

/** Description
 * This class for custom label to round label
 */
public class RoundLabel extends JLabel {
    
    public RoundLabel(String text) {
        super(text);
        setOpaque(false);  // Không sử dụng nền mặc định của JLabel
        setPreferredSize(new Dimension(50, 50));  // Kích thước hình tròn (bạn có thể thay đổi kích thước)
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Kiểm tra nếu label có văn bản
        if (getText() != null && !getText().isEmpty()) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Vẽ hình tròn với nền màu xanh
            g2d.setColor(Color.CYAN);
            g2d.fill(new Ellipse2D.Double(0, 0, getWidth(), getHeight()));  // Vẽ hình tròn với kích thước của JLabel
            
            // Vẽ văn bản lên hình tròn
            g2d.setColor(Color.BLACK);
            super.paintComponent(g);  // Vẽ văn bản lên JLabel
        } else {
            super.paintComponent(g);
        }
    }
}
