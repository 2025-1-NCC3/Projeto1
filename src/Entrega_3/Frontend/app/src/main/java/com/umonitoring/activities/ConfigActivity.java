package com.umonitoring.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.List;

import com.umonitoring.models.Motorista;
import com.umonitoring.R;
import com.umonitoring.components.BottomNavHelper;
import com.umonitoring.utils.Sessao;

public class ConfigActivity extends AppCompatActivity {

    Switch switchSeguranca;
    TextView tituloConfig;
    LinearLayout chave1Layout, chave2Layout, chave3Layout;
    TextView chave1Titulo, chave2Titulo, chave3Titulo;
    EditText editChave1, editChave2, editChave3;
    ImageView botaoVoltar;

    // Ícones
    ImageView setaChave1, setaChave2, setaChave3;

    boolean expanded1 = false;
    boolean expanded2 = false;
    boolean expanded3 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        // Botão voltar
        botaoVoltar = findViewById(R.id.botaoVoltar);
        botaoVoltar.setOnClickListener(v -> finish());

        // Inicialização dos componentes
        switchSeguranca = findViewById(R.id.switchSeguranca);
        tituloConfig = findViewById(R.id.tituloConfig);

        chave1Layout = findViewById(R.id.chave1Layout);
        chave2Layout = findViewById(R.id.chave2Layout);
        chave3Layout = findViewById(R.id.chave3Layout);

        chave1Titulo = findViewById(R.id.chave1Titulo);
        chave2Titulo = findViewById(R.id.chave2Titulo);
        chave3Titulo = findViewById(R.id.chave3Titulo);

        editChave1 = findViewById(R.id.editChave1);
        editChave2 = findViewById(R.id.editChave2);
        editChave3 = findViewById(R.id.editChave3);

        setaChave1 = findViewById(R.id.setaChave1);
        setaChave2 = findViewById(R.id.setaChave2);
        setaChave3 = findViewById(R.id.setaChave3);

        // Switch
        switchSeguranca.setOnCheckedChangeListener((buttonView, isChecked) -> {
            boolean enabled = isChecked;
            float alpha = isChecked ? 1.0f : 0.5f;

            tituloConfig.setEnabled(enabled);
            tituloConfig.setAlpha(alpha);

            chave1Layout.setEnabled(enabled);
            chave1Layout.setAlpha(alpha);

            chave2Layout.setEnabled(enabled);
            chave2Layout.setAlpha(alpha);

            chave3Layout.setEnabled(enabled);
            chave3Layout.setAlpha(alpha);

            editChave1.setEnabled(enabled);
            editChave2.setEnabled(enabled);
            editChave3.setEnabled(enabled);

            if (isChecked) {
                switchSeguranca.getThumbDrawable().setTint(ContextCompat.getColor(this, android.R.color.holo_green_dark));
                switchSeguranca.getTrackDrawable().setTint(ContextCompat.getColor(this, android.R.color.holo_green_light));
            } else {
                switchSeguranca.getThumbDrawable().setTint(ContextCompat.getColor(this, android.R.color.holo_red_dark));
                switchSeguranca.getTrackDrawable().setTint(ContextCompat.getColor(this, android.R.color.holo_red_light));

                // Fecha campos se estavam abertos
                expanded1 = false;
                expanded2 = false;
                expanded3 = false;

                editChave1.setVisibility(View.GONE);
                editChave2.setVisibility(View.GONE);
                editChave3.setVisibility(View.GONE);

                setaChave1.setRotation(0);
                setaChave2.setRotation(0);
                setaChave3.setRotation(0);
            }
        });

        // Clique Chave 1
        chave1Layout.setOnClickListener(v -> {
            expanded1 = !expanded1;
            editChave1.setVisibility(expanded1 ? View.VISIBLE : View.GONE);
            setaChave1.animate().rotation(expanded1 ? 90 : 0).setDuration(200).start();
        });

        // Clique Chave 2
        chave2Layout.setOnClickListener(v -> {
            expanded2 = !expanded2;
            editChave2.setVisibility(expanded2 ? View.VISIBLE : View.GONE);
            setaChave2.animate().rotation(expanded2 ? 90 : 0).setDuration(200).start();
        });

        // Clique Chave 3
        chave3Layout.setOnClickListener(v -> {
            expanded3 = !expanded3;
            editChave3.setVisibility(expanded3 ? View.VISIBLE : View.GONE);
            setaChave3.animate().rotation(expanded3 ? 90 : 0).setDuration(200).start();
        });

        Button botaoAtualizarConfig = findViewById(R.id.botaoAtualizar);
        botaoAtualizarConfig.setOnClickListener(v -> {
            if (!switchSeguranca.isChecked()) {
                Toast.makeText(this, "Ative o modo de segurança para atualizar.", Toast.LENGTH_SHORT).show();
                return;
            }

            String frase1 = editChave1.getText().toString().trim();
            String frase2 = editChave2.getText().toString().trim();
            String frase3 = editChave3.getText().toString().trim();

            new Thread(() -> {
                Motorista debugMotorista = new Motorista(0, "", "", "", "", "", "", "", "", "", "", "", "");
                List<Motorista> motoristas = debugMotorista.listarMotoristas();

                int idUsuario = Sessao.getIdUsuario();
                if (motoristas != null && !motoristas.isEmpty()) {
                    final Motorista motorista = motoristas.get(idUsuario);

                    // Atualiza apenas os campos preenchidos
                    if (!frase1.isEmpty()) motorista.setFrase1(frase1);
                    if (!frase2.isEmpty()) motorista.setFrase2(frase2);
                    if (!frase3.isEmpty()) motorista.setFrase3(frase3);

                    String resposta = motorista.atualizar(motorista.getId());

                    runOnUiThread(() -> {
                        Toast.makeText(ConfigActivity.this, resposta, Toast.LENGTH_LONG).show();

                        // Atualiza os hints se o campo foi preenchido
                        if (!frase1.isEmpty()) editChave1.setHint(frase1);
                        if (!frase2.isEmpty()) editChave2.setHint(frase2);
                        if (!frase3.isEmpty()) editChave3.setHint(frase3);

                        // Limpa os campos após atualização
                        editChave1.setText("");
                        editChave2.setText("");
                        editChave3.setText("");
                    });
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(ConfigActivity.this, "Motorista não encontrado.", Toast.LENGTH_LONG).show();
                    });
                }
            }).start();
        });

        // Menu inferior
        BottomNavHelper.setupBottomNavigation(this, R.id.nav_config);

        carregarDadosUsuario();

    }

    private void carregarDadosUsuario() {
        new Thread(() -> {
            Motorista debugMotorista = new Motorista(0, "", "", "", "", "", "", "", "", "", "", "", "");
            List<Motorista> motoristas = debugMotorista.listarMotoristas();

            int idUsuario = Sessao.getIdUsuario();
            Motorista motoristaSelecionado = null;

            for (Motorista m : motoristas) {
                if (m.getId() == idUsuario) {
                    motoristaSelecionado = m;
                    break;
                }
            }

            if (motoristaSelecionado != null) {
                Motorista finalMotorista = motoristaSelecionado;
                runOnUiThread(() -> {
                    editChave1.setHint(finalMotorista.getFrase1());
                    editChave2.setHint(finalMotorista.getFrase2());
                    editChave3.setHint(finalMotorista.getFrase3());
                    Toast.makeText(ConfigActivity.this, "Dados carregados!", Toast.LENGTH_SHORT).show();
                });
            } else {
                runOnUiThread(() -> Toast.makeText(ConfigActivity.this, "Motorista não encontrado.", Toast.LENGTH_LONG).show());
            }
        }).start();
    }

}