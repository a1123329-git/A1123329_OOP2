import java.awt.*;
import java.util.Random;
import javax.swing.*;
import javax.swing.border.*;

/**
 * DnD Dice Roller
 * - 選擇骰子種類 (d4, d6, d8, d10, d12, d20, d100)、顆數、加值
 * - 擲骰動畫：數字快速跳動約 1 秒後停下
 * - 依結果高低變色，高點數會有「放大彈跳」效果
 * - 右側顯示最近 10 次擲骰紀錄
 */
public class dice2 extends JFrame {

    public dice2() {
        super("DnD Dice Roller");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(new DicePanel());
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new dice2().setVisible(true));
    }
}

class DicePanel extends JPanel {

    // ===== 主題顏色 (Dark fantasy) =====
    static final Color BG        = new Color(28, 24, 34);    // 深色背景
    static final Color PANEL_BG  = new Color(42, 36, 50);    // 面板背景
    static final Color GOLD      = new Color(212, 175, 55);  // 金色
    static final Color PARCHMENT = new Color(240, 228, 200); // 羊皮紙色

    // ===== DnD 標準骰子 =====
    static final int[] SIDES = {4, 6, 8, 10, 12, 20, 100};
    static final int HISTORY_SIZE = 10;
    static final int BASE_FONT = 90;

    private final JComboBox<String> dieBox;
    private final JSpinner countSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));
    private final JSpinner modSpinner   = new JSpinner(new SpinnerNumberModel(0, -20, 20, 1));
    private final JButton rollButton    = new JButton("ROLL");

    private final JLabel resultLabel    = new JLabel("--", SwingConstants.CENTER);
    private final JLabel detailLabel    = new JLabel(" ", SwingConstants.CENTER);
    private final DefaultListModel<String> historyModel = new DefaultListModel<>();

    private final Random random = new Random();
    private Timer rollTimer;
    private Timer popTimer;

    DicePanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(BG);
        setBorder(new EmptyBorder(12, 12, 12, 12));
        setPreferredSize(new Dimension(640, 380));

        // ----- NORTH：控制列 -----
        String[] names = new String[SIDES.length];
        for (int i = 0; i < SIDES.length; i++) names[i] = "d" + SIDES[i];
        dieBox = new JComboBox<>(names);
        dieBox.setSelectedItem("d20");

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 4));
        controls.setBackground(PANEL_BG);
        controls.setBorder(new LineBorder(GOLD, 1));
        controls.add(goldLabel("Dice:"));
        controls.add(dieBox);
        controls.add(goldLabel("Count:"));
        controls.add(countSpinner);
        controls.add(goldLabel("Modifier:"));
        controls.add(modSpinner);

        rollButton.setFont(new Font("Serif", Font.BOLD, 16));
        rollButton.setBackground(GOLD);
        rollButton.setForeground(BG);
        rollButton.setFocusPainted(false);
        controls.add(rollButton);
        add(controls, BorderLayout.NORTH);

        // ----- CENTER：結果顯示 -----
        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(PANEL_BG);
        center.setBorder(new LineBorder(GOLD, 2));

        resultLabel.setFont(new Font("Serif", Font.BOLD, BASE_FONT));
        resultLabel.setForeground(PARCHMENT);
        detailLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
        detailLabel.setForeground(PARCHMENT);
        detailLabel.setBorder(new EmptyBorder(0, 8, 12, 8));

        center.add(resultLabel, BorderLayout.CENTER);
        center.add(detailLabel, BorderLayout.SOUTH);
        add(center, BorderLayout.CENTER);

        // ----- EAST：歷史紀錄 -----
        JList<String> historyList = new JList<>(historyModel);
        historyList.setBackground(PANEL_BG);
        historyList.setForeground(PARCHMENT);
        historyList.setSelectionBackground(GOLD);
        historyList.setSelectionForeground(BG);
        historyList.setFont(new Font("Monospaced", Font.PLAIN, 13));

        JScrollPane scroll = new JScrollPane(historyList);
        scroll.setPreferredSize(new Dimension(200, 0));
        TitledBorder title = new TitledBorder(new LineBorder(GOLD, 2), "History (last 10)");
        title.setTitleColor(GOLD);
        title.setTitleFont(new Font("Serif", Font.BOLD, 14));
        scroll.setBorder(title);
        scroll.setBackground(PANEL_BG);
        scroll.getViewport().setBackground(PANEL_BG);
        add(scroll, BorderLayout.EAST);

        rollButton.addActionListener(e -> startRoll());
    }

    private JLabel goldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(GOLD);
        label.setFont(new Font("Serif", Font.BOLD, 14));
        return label;
    }

    // ===== 擲骰：先算好結果，再播放動畫 =====
    void startRoll() {
        int sides = SIDES[dieBox.getSelectedIndex()];
        int count = (Integer) countSpinner.getValue();
        int mod   = (Integer) modSpinner.getValue();

        int[] rolls = new int[count];
        int sum = 0;
        for (int i = 0; i < count; i++) {
            rolls[i] = random.nextInt(sides) + 1;   // 1 ~ sides
            sum += rolls[i];
        }
        final int diceSum = sum;

        rollButton.setEnabled(false);
        if (popTimer != null) popTimer.stop();
        resultLabel.setFont(new Font("Serif", Font.BOLD, BASE_FONT));
        resultLabel.setForeground(PARCHMENT);
        detailLabel.setText("Rolling...");

        // 動畫：每 50ms 顯示一個隨機數字，共 20 次 (約 1 秒)
        final int[] ticks = {0};
        rollTimer = new Timer(50, e -> {
            ticks[0]++;
            int fake = count + random.nextInt(count * (sides - 1) + 1) + mod;
            resultLabel.setText(String.valueOf(fake));
            if (ticks[0] >= 20) {
                rollTimer.stop();
                showResult(sides, count, mod, rolls, diceSum);
            }
        });
        rollTimer.start();
    }

    // ===== 顯示最終結果 =====
    void showResult(int sides, int count, int mod, int[] rolls, int diceSum) {
        int total = diceSum + mod;
        String notation = count + "d" + sides + modText(mod).trim().replace(" ", "");  // 例如 2d6+3

        // ratio：0 = 最低點數，1 = 最高點數 (不含加值)
        double ratio = (sides == 1) ? 1.0
                : (double) (diceSum - count) / (count * (sides - 1));

        resultLabel.setText(String.valueOf(total));
        resultLabel.setForeground(colorFor(ratio));
        detailLabel.setText(notation + " :  " + rollsText(rolls) + modText(mod) + " = " + total);

        // 加入歷史紀錄 (最新的在最上面，最多 10 筆)
        historyModel.add(0, String.format("%-8s = %d", notation, total));
        if (historyModel.size() > HISTORY_SIZE) {
            historyModel.remove(historyModel.size() - 1);
        }

        if (ratio >= 0.75) pop();   // 高點數：放大彈跳
        rollButton.setEnabled(true);
    }

    // 依結果比例漸變顏色：暗紅 → 金色 → 亮綠
    static Color colorFor(double ratio) {
        Color low  = new Color(150, 70, 60);
        Color mid  = new Color(225, 190, 80);
        Color high = new Color(110, 235, 120);
        if (ratio < 0.5) return blend(low, mid, ratio / 0.5);
        return blend(mid, high, (ratio - 0.5) / 0.5);
    }

    static Color blend(Color a, Color b, double t) {
        int r  = (int) (a.getRed()   + (b.getRed()   - a.getRed())   * t);
        int g  = (int) (a.getGreen() + (b.getGreen() - a.getGreen()) * t);
        int bl = (int) (a.getBlue()  + (b.getBlue()  - a.getBlue())  * t);
        return new Color(r, g, bl);
    }

    // 「彈跳」效果：字體放大再縮回
    private void pop() {
        final int[] step = {0};
        final int steps = 16;
        popTimer = new Timer(15, e -> {
            step[0]++;
            int size = BASE_FONT + (int) (30 * Math.sin(Math.PI * step[0] / steps));
            resultLabel.setFont(new Font("Serif", Font.BOLD, size));
            if (step[0] >= steps) popTimer.stop();
        });
        popTimer.start();
    }

    private static String modText(int mod) {
        if (mod > 0) return " +" + mod;
        if (mod < 0) return " -" + (-mod);
        return "";
    }

    private static String rollsText(int[] rolls) {
        if (rolls.length > 10) return "(" + rolls.length + " dice)";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < rolls.length; i++) {
            if (i > 0) sb.append(", ");
            sb.append(rolls[i]);
        }
        return sb.append("]").toString();
    }

    // 測試用：直接設定元件值
    void setSelection(int dieIndex, int count, int mod) {
        dieBox.setSelectedIndex(dieIndex);
        countSpinner.setValue(count);
        modSpinner.setValue(mod);
    }
}