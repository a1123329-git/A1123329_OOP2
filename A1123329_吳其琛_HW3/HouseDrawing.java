import java.awt.*;
import javax.swing.*;

public class HouseDrawing extends JPanel {

    public HouseDrawing() {
        setPreferredSize(new Dimension(500, 400));   // 畫布 500 x 400
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 1. 天空與草地
        g.setColor(new Color(135, 206, 235));        // 天藍色
        g.fillRect(0, 0, 500, 300);
        g.setColor(new Color(76, 175, 80));          // 綠色
        g.fillRect(0, 300, 500, 100);

        // 2. 太陽：黃色正圓 + 8 條光芒
        int sunX = 420, sunY = 70, r = 40;           // 圓心與半徑
        g.setColor(Color.YELLOW);
        g.fillOval(sunX - r, sunY - r, r * 2, r * 2);
        g.setColor(new Color(230, 190, 0));
        for (int i = 0; i < 8; i++) {
            double angle = Math.toRadians(i * 45);   // 每 45 度一條
            int x1 = sunX + (int) (Math.cos(angle) * (r + 10));
            int y1 = sunY + (int) (Math.sin(angle) * (r + 10));
            int x2 = sunX + (int) (Math.cos(angle) * (r + 30));
            int y2 = sunY + (int) (Math.sin(angle) * (r + 30));
            g.drawLine(x1, y1, x2, y2);
        }

        // 3. 雲朵：3 個重疊的白色橢圓
        g.setColor(Color.WHITE);
        g.fillOval(60, 50, 80, 45);
        g.fillOval(100, 35, 70, 50);
        g.fillOval(140, 50, 80, 45);

        // 4. 房屋主體：fillRect + drawRect
        g.setColor(new Color(255, 228, 181));        // 米色
        g.fillRect(150, 180, 200, 150);
        g.setColor(Color.BLACK);
        g.drawRect(150, 180, 200, 150);

        // 5. 屋頂：只用 drawLine 畫三條線
        g.setColor(Color.RED);
        g.drawLine(130, 180, 250, 95);               // 左斜邊
        g.drawLine(250, 95, 370, 180);               // 右斜邊
        g.drawLine(130, 180, 370, 180);              // 底邊

        // 7. 窗戶：2 扇，fillRect + drawRect + 十字窗框
        drawWindow(g, 170, 210);
        drawWindow(g, 280, 210);

        // 6. 門：fillRoundRect + 小 fillOval 門把
        g.setColor(new Color(139, 69, 19));          // 咖啡色
        g.fillRoundRect(225, 255, 50, 75, 15, 15);
        g.setColor(Color.YELLOW);
        g.fillOval(262, 290, 8, 8);

        // 8. 簽名（請改成自己的學號與姓名）
        g.setColor(Color.BLACK);
        g.setFont(new Font("Dialog", Font.PLAIN, 14));
        g.drawString("A1123329 吳其琛", 10, 390);
    }

    // 畫一扇窗：白底 + 黑框 + 十字窗框
    private void drawWindow(Graphics g, int x, int y) {
        int size = 50;
        g.setColor(Color.WHITE);
        g.fillRect(x, y, size, size);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, size, size);
        g.drawLine(x + size / 2, y, x + size / 2, y + size);   // 直線
        g.drawLine(x, y + size / 2, x + size, y + size / 2);   // 橫線
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("小房子風景畫");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new HouseDrawing());
            frame.pack();                            // 依畫布大小調整視窗
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);
            frame.setVisible(true);
        });
    }
}