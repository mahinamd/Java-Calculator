import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;

public class Calculator extends JFrame {
    private final JTextField textField;
    private double firstNumber;
    private String operator;
    private boolean isOperatorClicked;
    private final StringBuilder calculationHistory;
    private final ArrayList<String> savedCalculations;
    private int calculationsCounter;

    public Calculator() {
        setTitle("Calculator");
        setSize(450, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(204, 255, 255));

        textField = new JTextField();
        textField.setEditable(false);
        textField.setFont(new Font("Arial", Font.BOLD, 65));
        add(textField, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 4, 5, 5));

        String[] buttonLabels = {
                "AC", "C", "M", "%",
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", ".", "=", "+",
        };

        calculationHistory = new StringBuilder();
        savedCalculations = new ArrayList<>();
        calculationsCounter = 0;

        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.addActionListener(new ButtonClickListener());
            button.setFont(new Font("Arial", Font.BOLD, 15));
            buttonPanel.add(button);
        }

        add(buttonPanel, BorderLayout.CENTER);
    }

    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String buttonLabel = ((JButton) e.getSource()).getText();
            switch (buttonLabel) {
                case "AC" -> clearAll();
                case "C" -> backspace();
                case "M" -> showSavedCalculations();
                case "%" -> calculatePercentage();
                case "=" -> calculateResult();
                default -> handleNumberOrOperator(buttonLabel);
            }
        }
    }

    private void clearAll() {
        textField.setText("");
        firstNumber = 0;
        operator = "";
        isOperatorClicked = false;
        calculationHistory.setLength(0);
    }

    private void backspace() {
        String text = textField.getText();
        if (!text.isEmpty()) {
            textField.setText(text.substring(0, text.length() - 1));
        }
    }

    private void showSavedCalculations() {
        StringBuilder sb = new StringBuilder();
        for (String calculation : savedCalculations) {
            sb.append(calculation).append("\n");
        }
        String message = sb.toString();
        if (message.isEmpty()) {
            message = "Nothing Here!!";
        }
        JOptionPane.showMessageDialog(null, message, "Saved Calculations", JOptionPane.INFORMATION_MESSAGE);
    }

    private void calculatePercentage() {
        String text = textField.getText();
        if (!text.isEmpty()) {
            double value = Double.parseDouble(text);
            double percentage = value / 100.0;
            textField.setText(String.valueOf(percentage));
        }
    }

    private void calculateResult() {
        String text = textField.getText();
        if (!text.isEmpty() && isOperatorClicked) {
            double secondNumber = Double.parseDouble(text);
            double result = 0;
            switch (operator) {
                case "+" -> result = firstNumber + secondNumber;
                case "-" -> result = firstNumber - secondNumber;
                case "*" -> result = firstNumber * secondNumber;
                case "/" -> {
                    if (secondNumber != 0) {
                        result = firstNumber / secondNumber;
                    } else {
                        JOptionPane.showMessageDialog(null, "Error: Division by zero");
                        clearAll();
                        return;
                    }
                }
            }
            textField.setText(String.valueOf(result));
            calculationHistory.append(result).append("\n");
            saveCalculation(String.valueOf(result));
            isOperatorClicked = false;
        }
    }

    private void handleNumberOrOperator(String label) {
        if (label.matches("[0-9.]")) {
            textField.setText(textField.getText() + label);
        } else if (label.matches("[+\\-*/]")) {
            if (!isOperatorClicked) {
                firstNumber = Double.parseDouble(textField.getText());
                operator = label;
                isOperatorClicked = true;
                textField.setText("");
            }
        }
    }

    private void saveCalculation(String calculation) {
        if (calculationsCounter >= 10) {
            savedCalculations.remove(0); // Remove the oldest calculation
        } else {
            calculationsCounter++;
        }
        savedCalculations.add(calculation);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Calculator calculator = new Calculator();
            calculator.setVisible(true);
        });
    }
}
