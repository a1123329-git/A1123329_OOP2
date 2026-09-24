import java.awt.*;
import java.util.Random;
import javax.swing.*;

public class DiceSimulator extends JFrame {

    private final JLabel statsLabel = new JLabel("已擲 0 次，總和 0，平均 0.00", SwingConstants.CENTER);
    private final JLabel diceLabel = new JLabel("-", SwingConstants.CENTER);
    private final JButton rollButton = new JButton("擲骰子");

    private final Random random = new Random();
    private int count = 0;   // number of rolls
    private int sum = 0;     // total of all rolls

    public DiceSimulator() {
        // Window title, size, close behavior, centered on screen
        setTitle("骰子模擬器");
        setSize(400, 320);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        // Top: roll statistics
        statsLabel.setFont(new Font("Dialog", Font.PLAIN, 16));
        statsLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(statsLabel, BorderLayout.NORTH);

        // Center: current dice value, 60pt font
        diceLabel.setFont(new Font("Dialog", Font.BOLD, 60));
        add(diceLabel, BorderLayout.CENTER);

        // Bottom: roll button
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(rollButton);
        add(bottomPanel, BorderLayout.SOUTH);

        rollButton.addActionListener(e -> rollDice());
    }

    private void rollDice() {
        int value = random.nextInt(6) + 1;   // random number 1–6

        count++;
        sum += value;
        double average = (double) sum / count;

        diceLabel.setText(String.valueOf(value));

        // 6 = green, 1 = red, otherwise black
        if (value == 6) {
            diceLabel.setForeground(new Color(0, 150, 0)); // darker green, easier to read
        } else if (value == 1) {
            diceLabel.setForeground(Color.RED);
        } else {
            diceLabel.setForeground(Color.BLACK);
        }

        statsLabel.setText(String.format("已擲 %d 次，總和 %d，平均 %.2f", count, sum, average));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DiceSimulator().setVisible(true));
    }
}