import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import javax.swing.*;

public class homework_2 extends JFrame implements ActionListener {

    String[] types = {"Length", "Weight", "Temperature"};

    String[][] units = {
        {"Meters", "Centimeters", "Inches", "Feet"},       
        {"Kilograms", "Grams", "Pounds", "Ounces"},        
        {"Celsius", "Fahrenheit", "Kelvin"}                
    };

    
    double[][] factors = {
        {1, 0.01, 0.0254, 0.3048},                 
        {1, 0.001, 0.45359237, 0.028349523125}     
    };

    JComboBox<String> typeBox = new JComboBox<>(types);
    JTextField inputField = new JTextField(10);
    JTextField outputField = new JTextField(10);
    JComboBox<String> fromBox = new JComboBox<>(units[0]);
    JComboBox<String> toBox = new JComboBox<>(units[0]);
    JButton convertBtn = new JButton("Convert");

    DecimalFormat df = new DecimalFormat("#.####"); 

    public homework_2() {
        setTitle("homework_2");
        setSize(480, 280);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

    
        JPanel north = new JPanel();
        north.add(new JLabel("Conversion type:"));
        north.add(typeBox);
        add(north, BorderLayout.NORTH);

    
        JPanel center = new JPanel(new GridLayout(1, 2, 20, 0));
        center.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));

        JPanel left = new JPanel(new GridLayout(2, 1, 5, 5));
        left.setBorder(BorderFactory.createTitledBorder("From"));
        left.add(inputField);
        left.add(fromBox);

        JPanel right = new JPanel(new GridLayout(2, 1, 5, 5));
        right.setBorder(BorderFactory.createTitledBorder("To"));
        outputField.setEditable(false);          
        right.add(outputField);
        right.add(toBox);

        center.add(left);
        center.add(right);
        add(center, BorderLayout.CENTER);


        JPanel south = new JPanel();
        south.add(convertBtn);
        add(south, BorderLayout.SOUTH);


        typeBox.addActionListener(this);
        convertBtn.addActionListener(this);

        toBox.setSelectedIndex(1);                
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == typeBox) {           
            updateUnits();
        } else if (e.getSource() == convertBtn) { 
            convert();
        }
    }

    void updateUnits() {
        int t = typeBox.getSelectedIndex();
        fromBox.setModel(new DefaultComboBoxModel<>(units[t]));
        toBox.setModel(new DefaultComboBoxModel<>(units[t]));
        toBox.setSelectedIndex(1);
        outputField.setText("");
    }

    void convert() {
        try {
            double value = Double.parseDouble(inputField.getText().trim());
            int t = typeBox.getSelectedIndex();
            int from = fromBox.getSelectedIndex();
            int to = toBox.getSelectedIndex();
            double result;

            if (t == 2) {                          
                result = convertTemp(value, from, to);
            } else {                               
                result = value * factors[t][from] / factors[t][to];
            }
            outputField.setText(df.format(result));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    double convertTemp(double v, int from, int to) {
        double c;
        if (from == 0)      c = v;                      
        else if (from == 1) c = (v - 32) * 5 / 9;        
        else                c = v - 273.15;              

        if (to == 0)      return c;
        else if (to == 1) return c * 9 / 5 + 32;
        else              return c + 273.15;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new homework_2().setVisible(true));
    }
}