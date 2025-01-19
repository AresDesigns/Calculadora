package com.example.calculator.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.calculator.models.CalculatorModel;

public class CalculatorViewModel extends ViewModel {
    private CalculatorModel calculator;
    private MutableLiveData<String> display;
    private String currentInput;
    private boolean isNewInput;

    public CalculatorViewModel() {
        calculator = new CalculatorModel();
        display = new MutableLiveData<>();
        currentInput = "";
        isNewInput = true;
        display.setValue("0");
    }

    public LiveData<String> getDisplay() {
        return display;
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
            calculator.setNum1(Double.parseDouble(currentInput));
            calculator.setOperator(op);
            currentInput = "";
            isNewInput = true;
        }
    }

    public void onEquals() {
        if (!currentInput.isEmpty()) {
            calculator.setNum2(Double.parseDouble(currentInput));
            double result = calculator.calculate();
            display.setValue(Double.isNaN(result) ? "Error" : String.valueOf(result));
            currentInput = String.valueOf(result);
            isNewInput = true;
        }
    }

    public void onClear() {
        calculator.clear();
        currentInput = "";
        isNewInput = true;
        display.setValue("0");
    }
}