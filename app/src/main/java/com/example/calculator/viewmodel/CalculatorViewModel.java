package com.example.calculator.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.calculator.models.CalculatorModel;

import java.text.DecimalFormat;

public class CalculatorViewModel extends ViewModel {
    private final MutableLiveData<String> operation = new MutableLiveData<>("0");
    private final MutableLiveData<String> result = new MutableLiveData<>("");
    private String currentInput = "";
    private boolean isNewInput = false;
    private double num1 = 0;
    private double num2 = 0;
    private char operator = ' ';
    private final CalculatorModel calculator = new CalculatorModel();

    public LiveData<String> getOperation() {
        return operation;
    }

    public LiveData<String> getResult() {
        return result;
    }


    public void onDigit(String digit) {
        if (isNewInput) {
            currentInput = digit;
            isNewInput = false;
        } else {
            currentInput += digit;
        }
        operation.setValue(currentInput);
    }

    public void OnOperator(char newOperator){
        try {

            if (operator != ' ') {
                currentInput = "";
                operator = newOperator;
                num2 = 0;
                calculator.setNum2(num2);
                Log.d("CalculatorViewModel", "if Operator: "+
                        operator +" "+ newOperator +" "+ num1 + " "+ num2 + " / "+ calculator.getNum2());

                updateDisplay(num1, operator);
                Log.d("CalculatorViewModel", "if Operator: "+
                        operator +" "+ newOperator +" "+ num1 + " "+ num2 + " / "+ calculator.getNum2());
            }
            else {
                num1 = Double.parseDouble(currentInput);
                num2 = 0;
                calculator.setNum2(num2);
                operator = newOperator;
                currentInput = "";
                isNewInput = false;
                calculator.setNum1(num1);
                updateDisplay(num1, operator);
                Log.d("CalculatorViewModel", "else Operator: "+
                        operator +" "+ newOperator +" "+ num1 + " "+ num2 + " / "+ calculator.getNum2());
            }
        }catch (NumberFormatException e) {}

    }
    public void onOperator(char newOperator) {
        try {
            if (operator != ' ') {
                num2 = Double.parseDouble(currentInput);
                calculator.setNum2(num2);
                calculator.setOperator(operator);
                double total = calculator.calculate();
                updateDisplay(total, operator);
                // **Important: Update the operator for the next calculation**
                operator = newOperator;
                num1 = total; // Update num1 with the result for the next calculation
                num2 = total;
                isNewInput = true; // Indicate that a new input is expected
                Log.d("CalculatorViewModel", "Operator 2: "+
                        operator +" "+ newOperator +" "+ num1 + " "+ num2);
                result.setValue(formatNumber(num1) + " " + operator); // Update operation display
                operation.setValue(formatNumber(num1)); // Update display to show num1
            }
            else {
                num1 = Double.parseDouble(currentInput);
                operator = newOperator;
                currentInput = "";
                isNewInput = false;
                calculator.setNum1(num1);
                updateDisplay(num1, operator);
            }
            Log.d("CalculatorViewModel", "Operator: "+
                    operator +" "+ newOperator +" "+ num1 + " "+ num2 + " / "+ calculator.getNum2());
        } catch (NumberFormatException e) {
            Log.e("CalculatorViewModel", "Invalid input: " + currentInput, e);
            // Handle invalid input, e.g., display an error message
        }
    }

    private void updateDisplay(double value, char operator) {
        result.setValue(formatNumber(value) + " " + operator);
        operation.setValue(formatNumber(value));
    }
    public void onEquals() {
        if (!currentInput.isEmpty()) {
            num2 = Double.parseDouble(currentInput); // Set num2 to the current input
            calculator.setNum1(num1);
            calculator.setNum2(num2);
            calculator.setOperator(operator);
            double total = calculator.calculate();
            result.setValue(formatNumber(num1) + " " + operator + " " + formatNumber(num2) + " = " + formatNumber(total)); // Update operation display
            num1 = total; // Update num1 with the result
            calculator.setResult(total);
            operation.setValue(formatNumber(total)); // Update display to show result
            Log.d("CalculatorViewModel", "Equeal Operator: "+
                    operator  +" "+ num1 + " "+ num2 + " / "+ calculator.getNum2());
        }
    }

    public void onClear() {
        calculator.clear();
        currentInput = "";
        isNewInput = false;
        operation.setValue("0");
        result.setValue("");
        num1 = 0;
        num2 = 0;
        operator = ' ';
    }

    public void onDot() {
        if (!currentInput.contains(".")) {
            currentInput += ".";
        }
        operation.setValue(currentInput);
    }

    public void onPlusMinus() {
        if (currentInput.startsWith("-")) {
            currentInput = currentInput.substring(1);
            operation.setValue(currentInput);
        } else {
            currentInput = "-" + currentInput;
            operation.setValue(currentInput);
        }
    }

    public void onBackspace() {
        if (!currentInput.isEmpty()) {
            currentInput = currentInput.substring(0, currentInput.length() - 1);
            operation.setValue(currentInput);
        }
    }

    private String formatNumber(double number) {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(number);
    }
}
