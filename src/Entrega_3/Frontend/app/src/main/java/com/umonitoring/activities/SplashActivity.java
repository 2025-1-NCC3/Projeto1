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

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            ServidorConfig.detectarServidor(this, () -> {
                String servidorUsado = ServidorConfig.getUrl("");
                android.util.Log.d("ServidorDetectado", "Servidor ativo: " + servidorUsado);

                Intent intent = new Intent(this, TelaDeViagemActivity.class);
                startActivity(intent);
                finish();
            });

        }, 1500); // 1,5 segundo de splash
    }
}
