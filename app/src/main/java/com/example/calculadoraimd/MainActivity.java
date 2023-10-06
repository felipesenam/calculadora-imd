package com.example.calculadoraimd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {
    Calculator calculatorFragment;
    Grades gradesFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gradesFragment = new Grades();
        calculatorFragment = new Calculator();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.fcContent, calculatorFragment);
        fragmentTransaction.commit();

        // Disable night mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_main);
    }

    public void openCalculator(View view){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fcContent, calculatorFragment);
        fragmentTransaction.commit();
    }
    public void openGrades(View view){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fcContent, gradesFragment);
        fragmentTransaction.commit();
    }

    String operator = null;
    Double firstOperand = null;
    Boolean waitingForOperand = false;

    private Double getValueFromTextView(TextView tv){
        String str = tv.getText().toString();
        if(!str.isEmpty()) {
            return Double.parseDouble(str);
        }

        return 0.;
    }

    private Double getResult(View view){
        TextView tvDisplay = view.getRootView().findViewById(R.id.tvDisplay);
        Double secondOperand = getValueFromTextView(tvDisplay);

        Double result = 0.;
        if(operator.equals(getString(R.string.keyboard_plus))) {
            result = firstOperand + secondOperand;
        }
        else if(operator.equals(getString(R.string.keyboard_minus))) {
            result = firstOperand - secondOperand;
        }
        else if(operator.equals(getString(R.string.keyboard_multiply))) {
            result = firstOperand * secondOperand;
        }
        else if(operator.equals(getString(R.string.keyboard_slash))) {
            if(secondOperand == 0) {
                Toast.makeText(MainActivity.this, getString(R.string.invalid_operation), Toast.LENGTH_SHORT).show();
            } else {
                result = firstOperand / secondOperand;
            }
        }

        return result;
    }

    private void updateCalcOperationTextViews(View view){
        View root = view.getRootView();

        TextView tvFirstOperand = root.findViewById(R.id.tvFirstOperand);
        if (firstOperand != null) {
            tvFirstOperand.setText(formatDouble(firstOperand));
        } else {
            tvFirstOperand.setText("");
        }

        TextView tvOperator = root.findViewById(R.id.tvOperator);
        tvOperator.setText(operator);
        if (operator != null) {
            tvOperator.setText(operator);
        } else {
            tvOperator.setText("");
        }
    }

    private void reset(View view){
        operator = null;
        firstOperand = null;
        waitingForOperand = true;

        updateCalcOperationTextViews(view);
    }

    private String formatDouble(Double num){
        if (num % 1 == 0){
            return String.valueOf(Double.valueOf(num).intValue());
        }
        return String.valueOf(num);
    }

    public void calcKeyboardNumPress(View view) {
        TextView tvDisplay = view.getRootView().findViewById(R.id.tvDisplay);
        Button btn = (Button) view;

        if(waitingForOperand){
            tvDisplay.setText("");
        }

        String text = tvDisplay.getText() + btn.getText().toString();
        tvDisplay.setText(formatDouble(Double.parseDouble(text)));

        waitingForOperand = false;
    }
    public void calcKeyboardOpPress(View view) {
        View root = view.getRootView();
        TextView tvDisplay = root.findViewById(R.id.tvDisplay);
        Button btn = (Button) view;

        if (!waitingForOperand && operator != null) {
            if(firstOperand == null) {
                firstOperand = getValueFromTextView(tvDisplay);
            } else{
                firstOperand = getResult(view);
                tvDisplay.setText(formatDouble(firstOperand));
            }
        }
        operator = btn.getText().toString();
        if (firstOperand == null) {
            firstOperand = getValueFromTextView(tvDisplay);
        }

        updateCalcOperationTextViews(view);

        waitingForOperand = true;
    }
    public void calcKeyboardResPress(View view) {
        Double result = getResult(view);
        TextView tvDisplay = view.getRootView().findViewById(R.id.tvDisplay);
        tvDisplay.setText(formatDouble(result));

        reset(view);
    }
    public void calcKeyboardDelPress(View view) {
        View root = view.getRootView();
        TextView tvDisplay = root.findViewById(R.id.tvDisplay);

        String text = tvDisplay.getText().toString();
        if(text.length() > 1) {
            text = text.substring(0, text.length() - 1);
            try {
                tvDisplay.setText(formatDouble(Double.parseDouble(text)));
            } catch (NumberFormatException e){
                tvDisplay.setText("0");
            }
        } else if(text.equals("0")) {
            reset(view);
        }
        else{
            tvDisplay.setText("0");
        }
    }

    public void calculateGrades(View view) {
        View root = view.getRootView();

        EditText etGrade01 = root.findViewById(R.id.et01);
        String strGrade01 = etGrade01.getText().toString();
        EditText etGrade02 = root.findViewById(R.id.et02);
        String strGrade02 = etGrade02.getText().toString();
        EditText etGrade03 = root.findViewById(R.id.et03);
        String strGrade03 = etGrade03.getText().toString();

        TextView tvResult = root.findViewById(R.id.tvResult);

        if(!strGrade03.isEmpty()){
            Double grade01 = Double.parseDouble(strGrade01);
            Double grade02 = Double.parseDouble(strGrade02);
            Double grade03 = Double.parseDouble(strGrade03);

            Double averageGrade = (grade01+grade02+grade03)/3;
            if(averageGrade >= 7){
                tvResult.setText(getString(R.string.appr_message));
                Toast.makeText(this, getString(R.string.appr_toast_message), Toast.LENGTH_SHORT).show();
            } else if (averageGrade >= 5){
                tvResult.setText(getString(R.string.apprn_message));
                Toast.makeText(this, getString(R.string.apprn_toast_message), Toast.LENGTH_SHORT).show();
            } else {
                tvResult.setText(getString(R.string.failed_message));
                Toast.makeText(this, getString(R.string.failed_toast_message), Toast.LENGTH_SHORT).show();
            }
            return;
        }

        Double apprGrade = 7.;
        Double apprnGrade = 5.;
        String periods = "1ª, 2ª e 3ª";
        String result = new String();
        if (!strGrade02.isEmpty()) {
            Double grade01 = Double.parseDouble(strGrade01);
            Double grade02 = Double.parseDouble(strGrade02);

            apprGrade = (7*3)-grade01-grade02;
            apprnGrade = (5*3)-grade01-grade02;
            periods = "3ª";
        } else if (!strGrade01.isEmpty()) {
            Double grade01 = Double.parseDouble(strGrade01);

            apprGrade = ((7*3)-grade01)/2;
            apprnGrade = ((5*3)-grade01)/2;
            periods = "2ª e 3ª";
        }

        // Se o requisito para atingir uma aprovação por média é inatingível, isto é, uma nota maior que 10, então **ignore**
        if (apprGrade <= 10){
            result = String.format(getString(R.string.appr_text), Math.max(apprGrade, 3.), periods);
        }

        // Se o requisito para atingir para uma aprovação por nota é negativo, então o aluno já possui nota suficiente para uma aprovação,
        // sendo assim, **ignore**.
        if(apprnGrade > 0) {
            result += String.format(getString(R.string.apprn_text), Math.max(apprnGrade, 3.), periods);
        }
        tvResult.setText(result);
    }
}