package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.List;
import java.util.regex.Pattern;

public class CalculatorFrame extends JFrame {

    // initialize the calculator frame
    private String[] strings = {
        "(",")","x!","x^y","+", "sin","7","8","9","-", "cos","4","5","6","×", "tan","1","2","3","÷", "%","0",".","AC","="
    };

    private Font defaultFont = new Font(Font.DIALOG, Font.PLAIN, 48);
    private JTextField jTextField = new JTextField("0",30);
    private JButton[] jButton = new JButton[25];
    private String strForCal = "";

    // the constructor
    public CalculatorFrame() {

        //first of all, create a jPanel to get all items in one
        JPanel OuterPanel = new JPanel();
        jTextFieldinit();
        OuterPanel.add(jTextField);
        JButton[] jButtons = initButton();

        for (JButton jButton : jButtons) {
            OuterPanel.add(jButton);
        }
        addActionListener();
        OuterPanel.setLayout(null);
        this.add(OuterPanel);
        this.setTitle("Scientific Calculator");
        this.setSize(750,640);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public JButton[] initButton(){
        // initialize the button group
        int XButton = 150, YButton = 100, temp = 0;
        // User reguler repression to match the string

        for(int i = 0 ; i < strings.length ; i ++){
                // declare the button and put the number in them
                jButton[temp] = new JButton(strings[temp]);
                jButton[temp].setText(strings[temp]);
                temp ++;
        }

        // set the layout of the jbutton array with different color in equal and AC
        temp = 0;
        for(int i = 0 ; i < 5 ; i ++){
            for(int j = 0 ; j < 5 ; j ++){
                if(temp >= 23){
                    jButton[temp].setBackground(new Color(235,180,100));
                }else{
                    jButton[temp].setBackground(Color.WHITE);
                }
                jButton[temp].setFont(defaultFont);
                jButton[temp].setBounds(j * XButton, YButton * (i+1), XButton, YButton);
                jButton[temp].setBorderPainted(false);
                temp ++;
            }
        }
        return jButton;
    }

    // get the input box and setting its looking
    public void jTextFieldinit(){
        jTextField.setHorizontalAlignment(SwingConstants.RIGHT);
        jTextField.setFont(defaultFont);
        jTextField.setBounds(0,0,700,95);
        jTextField.setBorder(null);
        jTextField.setEditable(false);
    }


    // this function gathered all the listener function on the buttons.
    public void addActionListener() {
        for (JButton jButton1 : jButton) {
            if (jButton1.getText().equals("AC")) {
                addACActionListener(jButton1);
            } else if (jButton1.getText().equals("x^y")) {
                addPowerActionListener(jButton1);
            } else if (jButton1.getText().equals("x!")) {
                addFactorialActionListener(jButton1);
            } else if (jButton1.getText().equals("cos") || jButton1.getText().equals("sin") || jButton1.getText().equals("tan")) {
                addTrigFunctionActionListener(jButton1, jButton1.getText());
            } else if (jButton1.getText().equals("=")) {
                addEqualActionListener(jButton1);
            } else {
                addDefaultActionListener(jButton1);
            }
        }
    }

    // AC button to initilize the input box
    private void addACActionListener(JButton jButton) {
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                jTextField.setText("");
                strForCal = "";
            }
        });
    }

    private void addPowerActionListener(JButton jButton) {
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                jTextField.setText(jTextField.getText() + "^");
                strForCal += "^";
            }
        });
    }

    private void addFactorialActionListener(JButton jButton) {
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                jTextField.setText(jTextField.getText() + "!");
                strForCal += "!";
            }
        });
    }

    private void addTrigFunctionActionListener(JButton jButton, String trigFunction) {
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                jTextField.setText(jTextField.getText() + trigFunction);
                strForCal += trigFunction.substring(0, 1);
            }
        });
    }

    private void addEqualActionListener(JButton jButton) {
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                handleEqualButtonClick();
            }
        });
    }

    private void addDefaultActionListener(JButton jButton) {
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                handleDefaultButtonClick(jButton.getText());
            }
        });
    }

    private void handleEqualButtonClick() {
        // Logic for handling "=" button click
        System.out.println(strForCal);
        Calculate calculate = new Calculate();
        List<String> list = calculate.getMidExpression(strForCal);
        if (calculate.wrongExpression(list) >= 1) {
            jTextField.setText("Wrong");
        } else {
            String strRes = calculate.getResult(strForCal);
            if (strRes.matches("[\u2E80-\uFE4F]+")) {
                jTextField.setText(strRes);
            } else if (!strRes.matches("[\u4e00-\u9fa5]") && Double.parseDouble(strRes) % 1 == 0) {
                jTextField.setText(String.valueOf((int) Double.parseDouble(strRes)));
            } else if (!strRes.matches("[\u4e00-\u9fa5]") && !(Double.parseDouble(strRes) % 1 == 0)) {
                DecimalFormat df = new DecimalFormat("#.#######");
                jTextField.setText(df.format(Double.parseDouble(strRes)));
            } else {
                jTextField.setText(strRes);
            }
        }
    }

    private void handleDefaultButtonClick(String buttonText) {
        // Logic for handling default button click
        if (jTextField.getText().equals("0") && !buttonText.equals(".")) {
            jTextField.setText(buttonText);
            strForCal += buttonText;
        } else {
            jTextField.setText(jTextField.getText() + buttonText);
            strForCal += buttonText;
        }
    }


}
