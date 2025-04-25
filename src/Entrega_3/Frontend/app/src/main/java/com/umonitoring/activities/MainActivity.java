package com.umonitoring.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.umonitoring.R;
import com.umonitoring.api.ServidorConfig;
import com.umonitoring.models.Motorista;

public class MainActivity extends AppCompatActivity {

    private EditText editTextNome;
    private Button btnSet;
    private int ID = 3;

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

        editTextNome = findViewById(R.id.editTextNome);
        btnSet = findViewById(R.id.btnSet);

        Motorista[] motorista = new Motorista[1];
        btnSet.setEnabled(false);

        new Thread(() -> {
            Motorista mot = Motorista.buscarMotoristaPorId(ID);
            motorista[0] = mot;

            runOnUiThread(() -> {
                if (mot != null) {
                    editTextNome.setText(mot.getNome());
                    btnSet.setEnabled(true); //
                } else {
                    editTextNome.setText("");
                    Toast.makeText(this, "Motorista não encontrado", Toast.LENGTH_SHORT).show();
                }
            });

        }).start();

        btnSet.setOnClickListener(view -> {
            Motorista mot = motorista[0];
            if (mot != null) {
                String nomeAtualizado = editTextNome.getText().toString().trim();
                if (nomeAtualizado.isEmpty()) {
                    Toast.makeText(this, "O nome não pode estar vazio!", Toast.LENGTH_SHORT).show();
                    return;
                }

                mot.setNome(nomeAtualizado);

                new Thread(() -> {
                    String resposta = mot.atualizar(mot.getId());
                    runOnUiThread(() ->
                            Toast.makeText(this, resposta, Toast.LENGTH_LONG).show()
                    );
                }).start();

            } else {
                Toast.makeText(this, "Motorista ainda não carregado", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
