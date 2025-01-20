package com.example.calculator.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.calculator.models.CalculatorModel;

import java.text.DecimalFormat;

public class CalculatorViewModel extends ViewModel {
    private final MutableLiveData<String> display = new MutableLiveData<>("0");
    private final MutableLiveData<String> operation = new MutableLiveData<>("");
    private String currentInput = "";
    private boolean isNewInput = true;
    private double num1 = 0;
    private double num2 = 0;
    private char operator = ' ';
    private CalculatorModel calculator = new CalculatorModel();

    public LiveData<String> getDisplay() {
        return display;
    }

    public LiveData<String> getOperation() {
        return operation;
    }



    public void onDigit(String digit) {
        if (isNewInput) {
            currentInput = digit;
            isNewInput = false;
        } else {
            currentInput += digit;
        }
        display.setValue(currentInput);
    }

    public void onOperator(char op) {
        if (!currentInput.isEmpty()) {
            if (operator != ' ') {
                onEquals();
            }
            num1 = Double.parseDouble(currentInput);
            calculator.setNum1(num1);
            calculator.setOperator(op);
            currentInput = "";
            isNewInput = true;
            operator = op;
            operation.setValue(formatNumber(num1) + op);
            display.setValue(formatNumber(num1));
            currentInput = formatNumber(num1);
            isNewInput = true;
        }
    }
    public void onEquals() {

   if (!currentInput.isEmpty()) {
        try {
            num2 = Double.parseDouble(currentInput);
            calculator.setNum2(num2);
            double result = calculator.calculate();
            display.setValue(formatNumber(result));
            currentInput = formatNumber(result);
            isNewInput = true;
            operation.setValue(formatNumber(result) + " = " + formatNumber(num1) + operator + formatNumber(num2));
            num1 = result;
            num2 = 0;
            Log.d("CalculatorViewModel", "onEquals: " + result + " = " + num1 + operator + num2);
        }catch (NumberFormatException e){
            display.setValue("Error");
            currentInput = "";
            isNewInput = true;
            num1 = 0;
            num2 = 0;
        }
        }
   }
    public void onClear() {
        calculator.clear();
        currentInput = "";
        isNewInput = true;
        display.setValue("0");
        operation.setValue("");
        num1 = 0;
        num2 = 0;
        operator = ' ';
    }

    public void onDot() {
        if (!currentInput.contains(".")) {
            currentInput += ".";
        }
        display.setValue(currentInput);
    }

    public void onBackspace() {
        if (!currentInput.isEmpty()) {
            currentInput = currentInput.substring(0, currentInput.length() - 1);
            display.setValue(currentInput);
        }
    }

    private String formatNumber(double number) {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(number);
    }
    }
