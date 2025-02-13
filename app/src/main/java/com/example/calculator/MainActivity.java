package com.example.calculator;

import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.calculator.viewmodel.CalculatorViewModel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private CalculatorViewModel viewModel;
    private TextView displayTextView, displayTextView2;
    private Button button0, button1, button2, button3, button4, button5,
            button6, button7, button8, button9, buttonDot,
            buttonEquals, buttonPlus, buttonMinus, buttonMultiply,
            buttonDivide, buttonClear, buttonPercent ,buttonDelete, buttonPlusMinus;
    private String history = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViewModel();
        initializeViewModel();
        initializeUIElements();
        observeViewModel();
        setupButtonListeners();
    }

    private void initializeViewModel() {
        viewModel = new ViewModelProvider(this).get(CalculatorViewModel.class);
    }

    private void initializeUIElements() {
        displayTextView = findViewById(R.id.textView_operation);
        displayTextView2 = findViewById(R.id.textView_result);

        button0 = findViewById(R.id.button_zero);
        button1 = findViewById(R.id.button_one);
        button2 = findViewById(R.id.button_two);
        button3 = findViewById(R.id.button_three);
        button4 = findViewById(R.id.button_four);
        button5 = findViewById(R.id.button_five);
        button6 = findViewById(R.id.button_six);
        button7 = findViewById(R.id.button_seven);
        button8 = findViewById(R.id.button_eight);
        button9 = findViewById(R.id.button_nine);
        buttonDot = findViewById(R.id.button_dot);
        buttonEquals = findViewById(R.id.button_equal);
        buttonPlus = findViewById(R.id.button_sum);
        buttonMinus = findViewById(R.id.button_subtract);
        buttonMultiply = findViewById(R.id.button_multiply);
        buttonDivide = findViewById(R.id.button_divide);
        buttonClear = findViewById(R.id.button_clear);
        buttonDelete = findViewById(R.id.button_delete);
        buttonPlusMinus = findViewById(R.id.button_plus_minus);
        buttonPercent = findViewById(R.id.button_percent);
    }

    private void observeViewModel() {
        viewModel.getOperation().observe(this, this::updateOperation);
        viewModel.getResult().observe(this, this::updateResult);
    }

    private void updateOperation(String operation) {
        if (displayTextView != null) {
            displayTextView.setText(operation);
        } else {
            Log.e("MyClass", "displayTextView is null");
        }
    }
    private void updateResult(String result) {

        if (displayTextView2 != null) {
            Pattern pattern = Pattern.compile("[+\\-*/%]");
            Matcher matcher = pattern.matcher(result);
            if (matcher.find()) {
                String operator = matcher.group();
                SpannableString spannableString = new SpannableString(result);
                int start = result.indexOf(operator);
                int end = start + operator.length();
                spannableString.setSpan(new ForegroundColorSpan(getColor(R.color.orange)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                displayTextView2.setText(spannableString);
                setHistory(getHistory()+" "+result+ " " + "\n");//Control for history and check all the operations
                Log.d("MainActivity", "displayTextView2 is not null" + getHistory());
            }else{
                displayTextView2.setText("");

            }
        } else {
            Log.d("MainActivity", "displayTextView2 is null");
            displayTextView2.setText("");

        }
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    private void setupButtonListeners() {
        // Number Buttons
        button0.setOnClickListener(v -> viewModel.onDigit("0"));
        button1.setOnClickListener(v -> viewModel.onDigit("1"));
        button2.setOnClickListener(v -> viewModel.onDigit("2"));
        button3.setOnClickListener(v -> viewModel.onDigit("3"));
        button4.setOnClickListener(v -> viewModel.onDigit("4"));
        button5.setOnClickListener(v -> viewModel.onDigit("5"));
        button6.setOnClickListener(v -> viewModel.onDigit("6"));
        button7.setOnClickListener(v -> viewModel.onDigit("7"));
        button8.setOnClickListener(v -> viewModel.onDigit("8"));
        button9.setOnClickListener(v -> viewModel.onDigit("9"));
        buttonPlusMinus.setOnClickListener(v -> viewModel.onPlusMinus());
        buttonDot.setOnClickListener(v -> viewModel.onDot());
        if(buttonDelete != null) {
            buttonDelete.setOnClickListener(v -> viewModel.onBackspace());
        }
        // Operator Buttons
        buttonEquals.setOnClickListener(v -> viewModel.onEquals());
        buttonPlus.setOnClickListener(v -> viewModel.OnOperator('+'));
        buttonMinus.setOnClickListener(v -> viewModel.OnOperator('-'));
        buttonMultiply.setOnClickListener(v -> viewModel.OnOperator('*'));
        buttonDivide.setOnClickListener(v -> viewModel.OnOperator('/'));
        buttonPercent.setOnClickListener(v -> viewModel.OnOperator('%'));
        buttonClear.setOnClickListener(v -> viewModel.onClear());
    }
}
