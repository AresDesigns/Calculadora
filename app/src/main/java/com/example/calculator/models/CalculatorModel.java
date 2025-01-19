package com.example.calculator.models;

public class CalculatorModel {
    private double num1;
    private double num2;
    private char operator;

    public CalculatorModel(){
        this.num1 = 0;
        this.num2 = 0;
        this.operator = ' ';
    }

    public double getNum1() {
        return num1;
    }

    public void setNum1(double num1) {
        this.num1 = num1;
    }

    public double getNum2() {
        return num2;
    }

    public void setNum2(double num2) {
        this.num2 = num2;
    }

    public char getOperator() {
        return operator;
    }

    public void setOperator(char operator) {
        this.operator = operator;
    }

    public void clear(){
        this.num1 = 0;
        this.num2 = 0;
        this.operator = ' ';
    }

    public double calculate() {
        switch (operator) {
            case '+':
                return num1 + num2;
            case '-':
                return num1 - num2;
            case '*':
                return num1 * num2;
            case '/':
                if (num2 != 0.0) {
                    return num1 / num2;
                } else {
                    return Double.NaN;
                }
            case '%':
                return num1 * (num2 / 100.0);
            default:
                return Double.NaN;
        }
    }

}
