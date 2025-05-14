package com.umonitoring.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import com.umonitoring.models.Motorista;
import com.umonitoring.R;
import com.umonitoring.components.BottomNavHelper;
import com.umonitoring.utils.Sessao;

public class ProfilePage extends AppCompatActivity {

    Button botaoAtualizar;
    String nomeOriginal, sobrenomeOriginal, telefoneOriginal, emailOriginal;

    ImageView botaoVoltar, setaNome, setaTelefone, setaEmail;
    LinearLayout layoutNome, layoutTelefone, layoutEmail;
    LinearLayout camposNome, camposTelefone, camposEmail;
    EditText editNome1, editNome2, editTelefone, editEmail;

    boolean expandedNome = false;
    boolean expandedTelefone = false;
    boolean expandedEmail = false;

    int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        BottomNavHelper.setupBottomNavigation(this, R.id.nav_home);

        idUsuario = Sessao.getIdUsuario();

        new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(this::carregarDadosUsuario, 500);

        botaoVoltar = findViewById(R.id.botaoVoltar);
        botaoVoltar.setOnClickListener(v -> finish());

        layoutNome = findViewById(R.id.layoutNome);
        layoutTelefone = findViewById(R.id.layoutTelefone);
        layoutEmail = findViewById(R.id.layoutEmail);

        camposNome = findViewById(R.id.camposNome);
        camposTelefone = findViewById(R.id.camposTelefone);
        camposEmail = findViewById(R.id.camposEmail);

        editNome1 = findViewById(R.id.editNome1);
        editNome2 = findViewById(R.id.editNome2);
        editTelefone = findViewById(R.id.editTelefone);
        editEmail = findViewById(R.id.editEmail);

        setaNome = findViewById(R.id.setaNome);
        setaTelefone = findViewById(R.id.setaTelefone);
        setaEmail = findViewById(R.id.setaEmail);

        botaoAtualizar = findViewById(R.id.botaoAtualizar);
        botaoAtualizar.setOnClickListener(v -> atualizarDados());

        layoutNome.setOnClickListener(v -> {
            expandedNome = !expandedNome;
            camposNome.setVisibility(expandedNome ? View.VISIBLE : View.GONE);
            setaNome.animate().rotation(expandedNome ? 90 : 0).setDuration(200).start();
        });

        layoutTelefone.setOnClickListener(v -> {
            expandedTelefone = !expandedTelefone;
            camposTelefone.setVisibility(expandedTelefone ? View.VISIBLE : View.GONE);
            setaTelefone.animate().rotation(expandedTelefone ? 90 : 0).setDuration(200).start();
        });

        layoutEmail.setOnClickListener(v -> {
            expandedEmail = !expandedEmail;
            camposEmail.setVisibility(expandedEmail ? View.VISIBLE : View.GONE);
            setaEmail.animate().rotation(expandedEmail ? 90 : 0).setDuration(200).start();
        });

        Button botaoSair = findViewById(R.id.botaoSair);
        botaoSair.setOnClickListener(v -> mostrarDialogoLogout());
    }

    private void mostrarDialogoLogout() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_logout, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialog);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.getWindow().setDimAmount(0.6f);
        }

        Button botaoCancelar = dialogView.findViewById(R.id.botaoNao);
        Button botaoConfirmar = dialogView.findViewById(R.id.botaoSim);

        botaoCancelar.setOnClickListener(v -> dialog.dismiss());

        botaoConfirmar.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(ProfilePage.this, LoginPage.class);
            startActivity(intent);
            finish();
        });

        dialog.show();
    }

    private void carregarDadosUsuario() {
        new Thread(() -> {
            Motorista debugMotorista = new Motorista(0, "", "", "", "", "", "", "", "", "", "", "", "");
            List<Motorista> motoristas = debugMotorista.listarMotoristas();

            if (motoristas != null && !motoristas.isEmpty()) {
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
                        editNome1.setHint(finalMotorista.getNome());
                        editNome2.setHint(finalMotorista.getSobrenome());
                        editTelefone.setHint(finalMotorista.getTelefone());
                        editEmail.setHint(finalMotorista.getEmail());

                        nomeOriginal = finalMotorista.getNome();
                        sobrenomeOriginal = finalMotorista.getSobrenome();
                        telefoneOriginal = finalMotorista.getTelefone();
                        emailOriginal = finalMotorista.getEmail();

                        Toast.makeText(ProfilePage.this, "Dados carregados!", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(ProfilePage.this, "Motorista não encontrado.", Toast.LENGTH_LONG).show());
                }
            } else {
                runOnUiThread(() -> Toast.makeText(ProfilePage.this, "Nenhum motorista encontrado.", Toast.LENGTH_LONG).show());
            }
        }).start();
    }

    private void atualizarDados() {
        String nome = editNome1.getText().toString().trim();
        String sobrenome = editNome2.getText().toString().trim();
        String telefone = editTelefone.getText().toString().trim();
        String email = editEmail.getText().toString().trim();

        new Thread(() -> {
            Motorista debugMotorista = new Motorista(0, "", "", "", "", "", "", "", "", "", "", "", "");
            List<Motorista> motoristas = debugMotorista.listarMotoristas();

            if (motoristas != null && !motoristas.isEmpty()) {
                Motorista motoristaSelecionado = null;
                for (Motorista m : motoristas) {
                    if (m.getId() == idUsuario) {
                        motoristaSelecionado = m;
                        break;
                    }
                }

                if (motoristaSelecionado != null) {
                    if (!nome.isEmpty()) motoristaSelecionado.setNome(nome);
                    if (!sobrenome.isEmpty()) motoristaSelecionado.setSobrenome(sobrenome);
                    if (!telefone.isEmpty()) motoristaSelecionado.setTelefone(telefone);
                    if (!email.isEmpty()) motoristaSelecionado.setEmail(email);

                    String resposta = motoristaSelecionado.atualizar(motoristaSelecionado.getId());

                    runOnUiThread(() -> {
                        if (resposta.contains("sucesso") || resposta.toLowerCase().contains("ok")) {
                            Toast.makeText(ProfilePage.this, "Dados atualizados com sucesso!", Toast.LENGTH_SHORT).show();
                            carregarDadosUsuario();
                        } else {
                            Toast.makeText(ProfilePage.this, "Erro: " + resposta, Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(ProfilePage.this, "Motorista não encontrado para atualizar.", Toast.LENGTH_LONG).show());
                }
            } else {
                runOnUiThread(() -> Toast.makeText(ProfilePage.this, "Nenhum motorista encontrado para atualizar.", Toast.LENGTH_LONG).show());
            }
        }).start();
    }
}
