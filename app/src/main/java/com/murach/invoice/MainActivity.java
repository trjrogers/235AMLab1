package com.murach.invoice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.EditText;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Currency;

public class MainActivity extends AppCompatActivity implements OnEditorActionListener {

    // Instance variables
    private EditText subtotalInput;
    private TextView discountPercent;
    private TextView discountAmount;
    private TextView totalAmount;

    private SharedPreferences savedValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get references to widgets
        subtotalInput = (EditText) findViewById(R.id.subtotal_input);
        discountPercent = (TextView) findViewById(R.id.discount_percent);
        discountAmount = (TextView) findViewById(R.id.discount_amount);
        totalAmount = (TextView) findViewById(R.id.total);

        subtotalInput.setOnEditorActionListener(this);

        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);
    }

    protected void calculateAndDisplay() {
        String subTotalString = subtotalInput.getText().toString();
        Double subTotalDouble;

        if(subTotalString.equals("")) {
            subTotalDouble = 0.0;
        } else {
            subTotalDouble = Double.parseDouble(subTotalString);
        }

        Double discount;

        if(subTotalDouble >= 200) {
            discount = 0.8;
        } else if(subTotalDouble >= 100 && subTotalDouble <= 200) {
            discount = 0.9;
        } else {
            discount = 1.0;
        }

        Double totalDouble = (subTotalDouble * discount);
        Double discountAmountDouble = subTotalDouble - totalDouble;
        Double discountPercentDouble = (100 - (discount * 100));

        NumberFormat currency = NumberFormat.getCurrencyInstance();
        totalAmount.setText(currency.format(totalDouble));
        discountPercent.setText(discountPercentDouble + "%");
        discountAmount.setText(currency.format(discountAmountDouble));
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
            calculateAndDisplay();
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();

        String subtotalString = savedValues.getString("subtotalString", "");

        subtotalInput.setText(subtotalString);

        calculateAndDisplay();
    }

    @Override
    public void onPause() {
        Editor editor = savedValues.edit();
        String subtotalString = subtotalInput.getText().toString();
        editor.putString("subtotalString", subtotalString);
        editor.commit();
        super.onPause();
    }
}
