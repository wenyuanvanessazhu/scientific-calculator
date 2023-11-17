package com.company;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;


public class Calculate {
    // this label is used for check whether there is a percentage
    private static int label;

    // In this file, i first trans the input expression into midexperssion
    // then calculate the reslut based on the above.
    public String getResult(String str){
        return getAfterResult(transMidToAfter(getMidExpression(str)));
    }

    //
    public List<String> getMidExpression(String str){
        List<String> list = new ArrayList<>();
        for(int i = 0 ; i < str.length() ; i ++){
            char strTemp = str.charAt(i);
            if("(+-×÷^!cst)".indexOf(strTemp) >= 0){
                // use index to find if it is a calculator
                list.add(strTemp + "");
                // is percentage symbol
            }else if(str.equals("%")){
                label = 1;
                // otherwise, the numbers
            }else if("1234567890".indexOf(strTemp) >= 0){
                String str1 = strTemp + "";
                // use a loop to achieve the number all considered at once.
                while(i + 1 < str.length() && "1234567980.".indexOf(str.charAt(i + 1)) >= 0){
                    str1 += str.charAt(i + 1);
                    i++;
                }
                list.add(str1);
            }
        }
        return list;
    }

    // Infix expression to postfix expression conversion.
    public List<String> transMidToAfter(List<String> list){
        // we need stack to do this.
        Stack<String> stack = new Stack<>();
        List<String> res = new ArrayList<>();
        if (!list.isEmpty()){
            // when list is not empty, then iterate it in one by one
            // to check the symbol in one by one
            for (String str : list) {
                if ("0123456789".indexOf(str.charAt(0)) >= 0) {
                    // number is stored in str
                    res.add(str);
                } else if (str.equals("(")) {
                    // if it is left bracket, just put it in stack
                    stack.push(str);
                }else if (str.equals(")")) {
                    // right bracket, should check whether a matching item existed.
                    while (!stack.empty() && !stack.peek().equals("(")) {
                        res.add(stack.pop());
                    }
                    stack.pop();
                }else if (isOperator(str)) {
                    // if is an operator, should first check the priorty first and then put them in stack.
                    if (!stack.empty() && isOperator(str) && isOperator(stack.peek()) &&
                            getOutStanding(str) <= getOutStanding(stack.peek())) {
                        while (!stack.empty() && isOperator(str) && isOperator(stack.peek()) &&
                                getOutStanding(str) <= getOutStanding(stack.peek())) {
                            res.add(stack.peek());
                            stack.pop();
                        }
                    }
                    stack.push(str);
                }
            }
        }
        // All items in stack should be popped.
        while(!stack.empty()){
            res.add(stack.peek());
            stack.pop();
        }
        return res;
    }


    // get the priority of the str.
    private int getOutStanding(String string){

        if(string.equals("+") || string.equals("-")){
            return 1;
        }else if(string.equals("×") || string.equals("÷")){
            return 2;
        }else if(string.equals("^")){
            return 3;
        }else{
            return 4;
        }
    }

    // check whether the str is an operator.
    private boolean isOperator(String string){

        if(string.equals("+") || string.equals("-") ||string.equals("×") ||string.equals("÷") ||string.equals("^")
                ||string.equals("t") ||string.equals("c") ||string.equals("s") || string.equals("!")){
            return true;
        }else return false;
    }


    // To check whether there is a wrong expression in mathmatical.
    public int wrongExpression(List<String> list){
        int temp = 0;
        if(list.size() == 0){
            // no input
            temp = 1;
            // only one symbol operator
        }else if(list.size() == 1){
            if(list.get(0).equals("(") || list.get(0).equals(")") || isOperator(list.get(0)) || list.get(0).equals("t")
                    || list.get(0).equals("c") || list.get(0).equals("s")){
                temp = 2;
            }
        }else{
            // more than one char, should pay attention to the logic.
            for(int i = 0 ; i < list.size() ; i ++){
                String str = list.get(i);
                int isNumber = 0;
                if(! "+-×÷^!sct()".contains(str)){
                    // when the symbol is not a operator, which means it should be a number with maybe point.
                    // so it must can be parsed into number, otherwise the expression is wrong.
                    try {
                        Double.parseDouble(str);
                        isNumber = 1;
                    }catch (NumberFormatException e){
                        temp = 3;
                    }
                }
                // No number can be sequenced to a number
                if(isNumber == 1 && i < str.length() - 1 && ! "+-×÷^!sct()".contains(list.get(i + 1))){
                    temp = 4;
                }

                int cnt = 0;
                for(int j = 0 ; j < str.length() ; j ++){
                    // hadle the point.
                    if(str.charAt(j) == '.'){
                        cnt ++;
                    }
                }

                if(cnt == 1 && (str.indexOf('.') == 0 || str.indexOf(".") == str.length()- 1)){
                    // double cannot check the situation that the point in the tail of the number
                    temp = 5;
                }

                if(i == 0 && !("sct1234567890(".indexOf(str.charAt(0)) >= 0)){
                    // the head of sting list is not a number or left bracket.
                    temp = 6;
                }

                if("+-×÷^".contains(str) && ((i - 1 >= 0 && !("1234567890)".indexOf(list.get(i - 1).charAt(0)) >= 0))
                        || (i + 1 < list.size() && !("1234567890(".indexOf(list.get(i + 1).charAt(0)) >= 0)))){
                    // operator must be embraced by number in 2 sides.
                    temp = 7;
                }

                if(str.equals("!") && !(i == list.size() - 1 || "+-×÷^)".indexOf(list.get(i + 1).charAt(0)) >= 0)){
                    // the str after ! must be a number or other may operator
                    temp = 8;
                }

                if(str.equals("(") && (i == list.size() - 1 || !("0123465789sct()".indexOf(list.get(i + 1).charAt(0)) >= 0))){
                    // what inner the bracket must be number and trangle funtion, otherwise wrong.
                    temp = 9;
                }

                if(str.equals(")") && (i+1 < list.size() && !(("+-×÷^)").indexOf(list.get(i + 1).charAt(0)) >= 0))){
                    // fight must be followd by a logical operator
                    temp = 10;

                }

                if("sct".contains(str) && i+1 < list.size() && !("0123456789(".indexOf(list.get(i + 1).charAt(0)) >= 0)){
                    // must be a number after the COS, SIN, TAN
                    temp = 11;
                }

                if(i == list.size() - 1 && !("0123456789!)".indexOf(str.charAt(0)) >= 0)){
                    // the last str must be a number!
                    temp = 12;
                }

            }
        }
        return temp;
    }



    // this function is used to get the calculation result!
    public String getAfterResult(List<String> list){
        // initialize the stack and res str.
        String res = "";
        Stack<String> stack = new Stack<>();
        // iterate all list
        for(String string : list){
            // jugde whether the first is a number, push it in to stack.
            if("0123456789".indexOf(string.charAt(0)) >= 0){
                stack.push(string);
            }else{
                // this double is used as res storage
                Double resTemp = 0.0;
                // Binomial operator, getting two data from behind
                if("+-×÷^".indexOf(string) >= 0){
                    // No enough number, wrong!
                    if(stack.size() < 2){
                        return null;
                    }
                    Double num1 = Double.parseDouble(stack.pop());
                    Double num2 = Double.parseDouble(stack.pop());
                    switch(string){
                        case "+":
                            resTemp = num1 + num2;
                            break;
                        case "-":
                            resTemp = num2 - num1;
                            break;
                        case "×":
                            resTemp = num1 * num2;
                            break;
                        case "÷":
                            resTemp = num2 / num1;
                            break;
                        case "^":
                            resTemp = Math.pow(num2,num1);
                    }
                }else if("tcs!".contains(string)){
                    // Single operator, getting one data from behind
                    if (stack.size() < 1){
                        return null;
                    }
                    Double num1 = Double.parseDouble(stack.pop());
                    switch (string) {
                        case "t" -> resTemp = Math.tan(num1);
                        case "c" -> resTemp = Math.cos(num1);
                        case "s" -> resTemp = Math.sin(num1);
                        case "!" -> {
                            if (num1 == 0 || num1 == 1) {
                                resTemp = 1.0;
                            } else if (num1 % 1 == 0 && num1 > 1) {
                                resTemp = 1.0;
                                for (int i = 0; i < num1; i++) {
                                    resTemp *= num1 - i;
                                }
                            } else {
                                return "Mathmatical Number";
                            }
                        }
                    }
                }
                stack.push(String.valueOf(resTemp));
            }
        }
        // There is not just one number in the final stack. Error reported
        if(stack.size() != 1){
            return "Wrong in expression！";
        }
        res = stack.pop();
        // limit the output format in 6.
        DecimalFormat df = new DecimalFormat("#.######");
        return df.format(Double.parseDouble(res));
    }

    public static void main(String[] args) {
        Calculate calculate = new Calculate();
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        System.out.println(calculate.getResult(input));
    }
}
