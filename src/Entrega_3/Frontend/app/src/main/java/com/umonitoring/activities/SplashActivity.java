package com.umonitoring.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.umonitoring.R;
import com.umonitoring.api.ServidorConfig;

public class SplashActivity extends AppCompatActivity {

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final int INTERVALO_TENTATIVA_MS = 1500; // Intervalo entre tentativas (1,5 segundos)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tentarDetectarServidor(); // Inicia as tentativas
    }

    private void tentarDetectarServidor() {
        ServidorConfig.detectarServidor(this, () -> {
            String servidorUsado = ServidorConfig.getUrl("");
            Log.d("ServidorDetectado", "Servidor ativo: " + servidorUsado);

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();

        }, () -> {
            Log.w("ServidorDetectado", "Servidor ainda não disponível. Tentando novamente...");
            handler.postDelayed(this::tentarDetectarServidor, INTERVALO_TENTATIVA_MS);
        });
    }
}
