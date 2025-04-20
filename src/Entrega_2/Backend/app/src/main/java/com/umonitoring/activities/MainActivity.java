package com.umonitoring.activities;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.umonitoring.R;
import com.umonitoring.api.ServidorConfig;

public class MainActivity extends AppCompatActivity {

    private Button btnTesteServidor, btnTesteMotoristaAPI, btnTestePasageiroAPI, btnTesteViagemAPI;

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

        btnTesteServidor = findViewById(R.id.btnTesteServidor);
        btnTesteMotoristaAPI = findViewById(R.id.btnTesteMotoristaAPI);
        btnTestePasageiroAPI = findViewById(R.id.btnTestePasageiroAPI);
        btnTesteViagemAPI = findViewById(R.id.btnTesteViagemAPI);

        btnTesteServidor.setOnClickListener( view -> {
            ServidorConfig.detectarServidor(this);
        });

        btnTesteMotoristaAPI.setOnClickListener( view -> {
            ServidorConfig.testarRota(this, "motorista");
        });
        btnTestePasageiroAPI.setOnClickListener( view -> {
            ServidorConfig.testarRota(this, "passageiro");
        });
        btnTesteViagemAPI.setOnClickListener( view -> {
            ServidorConfig.testarRota(this, "viagem");
        });
    }
}