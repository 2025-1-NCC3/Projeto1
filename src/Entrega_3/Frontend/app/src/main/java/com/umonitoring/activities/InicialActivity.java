package com.umonitoring.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.umonitoring.R;

public class InicialActivity extends AppCompatActivity {

    MaterialButton btnEntrar, btnCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicial_activity);

        btnEntrar = findViewById(R.id.btnEntrar);
        btnCadastrar = findViewById(R.id.btnCadastro);

        btnEntrar.setOnClickListener(v -> {
            Intent intent = new Intent(InicialActivity.this, LoginPage.class);
            startActivity(intent);
        });

        btnCadastrar.setOnClickListener(v -> {
            Intent intent = new Intent(InicialActivity.this, CadastroPage.class);
            startActivity(intent);
        });
    }
}
