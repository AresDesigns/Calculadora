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
    private boolean isEqualPressed=false;
    private double num1 = 0;
    private double num2 = 0;
    private String num3 = "";
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

            if(isEqualPressed){

                num1 = calculator.getResult();
                num2 = 0;
                calculator.setNum1(num1);
                calculator.setNum2(num2);
                operator = newOperator;
                currentInput = "";
                isNewInput = false;
                updateDisplay(num1, operator); Log.d("CalculatorViewModel", "if Operator3: "+
                        operator +" "+ newOperator +" "+ num1 + " "+ num2 + " / "+ calculator.getNum2());
                isEqualPressed=false;

            }
            else if (operator != ' ') {
                num3 = currentInput;
                currentInput = "";
                operator = newOperator;
                Log.d("CalculatorViewModel", "if Operator1: "+
                        operator +" "+ newOperator +" "+ num3 + " "+ num1 + " "+ num2 + " / "+ calculator.getNum1()+" num3: " +num3) ;
                if(num1 != calculator.getNum1()){
                    calculator.setNum1(num1);
                }
                updateDisplay(num1, operator);
                isNewInput = true;
                Log.d("CalculatorViewModel", "if Operator2: "+
                        operator +" "+ newOperator +" "+ num1 + " "+ num2 + " / "+ calculator.getNum2());
                if(num1 != calculator.getNum1()){
                    calculator.setNum1(num1);
                }
                if (!num3.isEmpty()){
                    Log.v("CalculatorViewModel", "isEmpty1: " +operator +" "+ newOperator +" "+ num1 + " "+ num2 + " / "+ calculator.getNum2()+" num3: " +num3);
                    Log.v("CalculatorViewModel", "isEmpty2: " +operator +" "+ newOperator +" "+ num1 + " "+ num2 + " / "+ calculator.getNum1()+" num3: " +num3);
                    onOperator(operator,num3);
                }
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
    public void onOperator(char newOperator,String  aux) {
        try {
            Log.d("CalculatorViewModel", "onOperator1: "+ aux);
            if (!aux.isEmpty()) {
                num2 = Double.parseDouble(aux);
                calculator.setNum2(num2);
                calculator.setOperator(operator);
                double total = calculator.calculate();
                updateDisplay(total, operator);
                // **Important: Update the operator for the next calculation**
                operator = newOperator;
                num1 = total; // Update num1 with the result for the next calculation
                calculator.setResult(total);
                num2 = 0;
                calculator.setNum2(num2);
                calculator.setNum1(num1);
                num3 = "";
                isNewInput = false; // Indicate that a new input is expected
                Log.d("CalculatorViewModel", "Operator 2: "+
                        operator +" "+ newOperator +" "+ num1 + " "+ num2);
                result.setValue(formatNumber(num1) + " " + operator); // Update operation display
                operation.setValue(formatNumber(num1)); // Update display to show num1

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
        isEqualPressed=true;
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
            Log.d("CalculatorViewModel", "Equeal 1 Operator: "+
                    operator  +" "+ num1 + " "+ num2 + " / "+ calculator.getNum2()+ " current "+currentInput);
        }
        else {
            num2 = num1;// Set num2 to the current input
            currentInput = ""+num2;
            calculator.setNum2(num2);
            calculator.setOperator(operator);
            double total = calculator.calculate();
            result.setValue(formatNumber(num1) + " " + operator + " " + formatNumber(num2) + " = " + formatNumber(total)); // Update operation display
            num1 = total; // Update num1 with the result
            calculator.setResult(total);
            operation.setValue(formatNumber(total)); // Update display to show result
            Log.d("CalculatorViewModel", "Equeal 2 Operator: "+
                    operator  +" "+ num1 + " "+ num2 + " / "+ calculator.getNum2());

        }
    }

    public void onClear() {
        calculator.clear();
        currentInput = "";
        isNewInput = false;
        isEqualPressed=false;
        operation.setValue("0");
        result.setValue("");
        num1 = 0;
        num2 = 0;
        calculator.setResult(0);
        calculator.setNum1(0);
        calculator.setNum2(0);
        calculator.setOperator(' ');
        operator = ' ';
    }

    public void onDot() {
        if (!currentInput.contains(".")) {
            currentInput += ".";
        }
        operation.setValue(currentInput);
    }

    public void onPlusMinus() {
Log.d("CalculatorViewModel","onPlusMinus: "+currentInput)   ;

    if(currentInput.isEmpty()){
        currentInput=""+calculator.getResult();
    }
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
