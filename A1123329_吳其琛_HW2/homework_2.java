// homework_2, 單位換算器
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import javax.swing.*;

public class homework_2 extends JFrame implements ActionListener {

    // 換算類型
    String[] types = {"Length", "Weight", "Temperature"};

    // 每種類型對應的單位
    String[][] units = {
        {"Meters", "Centimeters", "Inches", "Feet"},       // 長度
        {"Kilograms", "Grams", "Pounds", "Ounces"},        // 重量
        {"Celsius", "Fahrenheit", "Kelvin"}                // 溫度
    };

    // 長度、重量的換算係數：1 單位 = 多少「基準單位」(公尺 / 公斤)
    double[][] factors = {
        {1, 0.01, 0.0254, 0.3048},                 // m, cm, in, ft → 公尺
        {1, 0.001, 0.45359237, 0.028349523125}     // kg, g, lb, oz → 公斤
    };

    JComboBox<String> typeBox = new JComboBox<>(types);
    JTextField inputField = new JTextField(10);
    JTextField outputField = new JTextField(10);
    JComboBox<String> fromBox = new JComboBox<>(units[0]);
    JComboBox<String> toBox = new JComboBox<>(units[0]);
    JButton convertBtn = new JButton("Convert");

    DecimalFormat df = new DecimalFormat("#.####");   // 最多顯示 4 位小數

    public homework_2() {
        setTitle("homework_2");
        setSize(480, 280);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // NORTH：選擇換算類型
        JPanel north = new JPanel();
        north.add(new JLabel("Conversion type:"));
        north.add(typeBox);
        add(north, BorderLayout.NORTH);

        // CENTER：左右兩欄，各有一個 JTextField + 一個 JComboBox
        JPanel center = new JPanel(new GridLayout(1, 2, 20, 0));
        center.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));

        JPanel left = new JPanel(new GridLayout(2, 1, 5, 5));
        left.setBorder(BorderFactory.createTitledBorder("From"));
        left.add(inputField);
        left.add(fromBox);

        JPanel right = new JPanel(new GridLayout(2, 1, 5, 5));
        right.setBorder(BorderFactory.createTitledBorder("To"));
        outputField.setEditable(false);           // 結果欄只顯示、不輸入
        right.add(outputField);
        right.add(toBox);

        center.add(left);
        center.add(right);
        add(center, BorderLayout.CENTER);

        // SOUTH：換算按鈕
        JPanel south = new JPanel();
        south.add(convertBtn);
        add(south, BorderLayout.SOUTH);

        // 註冊事件
        typeBox.addActionListener(this);
        convertBtn.addActionListener(this);

        toBox.setSelectedIndex(1);                // 預設目標單位選第二個
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == typeBox) {           // 切換換算類型
            updateUnits();
        } else if (e.getSource() == convertBtn) { // 按下 Convert
            convert();
        }
    }

    // 依照換算類型，更新兩個單位下拉選單的選項
    void updateUnits() {
        int t = typeBox.getSelectedIndex();
        fromBox.setModel(new DefaultComboBoxModel<>(units[t]));
        toBox.setModel(new DefaultComboBoxModel<>(units[t]));
        toBox.setSelectedIndex(1);
        outputField.setText("");
    }

    // 執行換算，結果顯示在第二個 JTextField
    void convert() {
        try {
            double value = Double.parseDouble(inputField.getText().trim());
            int t = typeBox.getSelectedIndex();
            int from = fromBox.getSelectedIndex();
            int to = toBox.getSelectedIndex();
            double result;

            if (t == 2) {                          // 溫度要用公式
                result = convertTemp(value, from, to);
            } else {                               // 長度、重量：先換成基準單位，再換成目標單位
                result = value * factors[t][from] / factors[t][to];
            }
            outputField.setText(df.format(result));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 溫度換算：先換成攝氏，再換成目標單位
    double convertTemp(double v, int from, int to) {
        double c;
        if (from == 0)      c = v;                       // Celsius
        else if (from == 1) c = (v - 32) * 5 / 9;        // Fahrenheit → Celsius
        else                c = v - 273.15;              // Kelvin → Celsius

        if (to == 0)      return c;
        else if (to == 1) return c * 9 / 5 + 32;         // Celsius → Fahrenheit
        else              return c + 273.15;             // Celsius → Kelvin
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new homework_2().setVisible(true));
    }
}