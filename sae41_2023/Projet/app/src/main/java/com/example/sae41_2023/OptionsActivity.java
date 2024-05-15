package com.example.sae41_2023;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class OptionsActivity extends AppCompatActivity {

    private Spinner spinnerCrossSize;
    private CheckBox checkBoxNoExtension;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        ImageButton buttonReturn = findViewById(R.id.buttonReturn);
        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        spinnerCrossSize = findViewById(R.id.spinnerCrossSize);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.cross_size_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCrossSize.setAdapter(adapter);

        // Load the saved state for the Spinner
        int spinnerSelection = sharedPreferences.getInt("spinnerSelection", 0); // Default is 0
        spinnerCrossSize.setSelection(spinnerSelection);

        spinnerCrossSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedSize = parent.getItemAtPosition(position).toString();
                int crossSizeValue = 0;
                switch (selectedSize) {
                    case "Petite":
                        crossSizeValue = 24; // ou une autre valeur que vous associez à "Petite"
                        break;
                    case "Grande":
                        crossSizeValue = 36; // ou une autre valeur que vous associez à "Grande"
                        break;
                }

                getSharedPreferences("GamePrefs", MODE_PRIVATE).edit()
                        .putInt("CrossSize", crossSizeValue)
                        .putInt("CrossSize", crossSizeValue)
                        .apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        checkBoxNoExtension = findViewById(R.id.checkBoxNoExtension);
        boolean checkBoxState = sharedPreferences.getBoolean("checkBoxState", false); // Default is false

        checkBoxNoExtension.setChecked(checkBoxState);

        checkBoxNoExtension.setOnCheckedChangeListener((buttonView, isChecked) ->
                sharedPreferences.edit().putBoolean("checkBoxState", isChecked).apply());
    }
}