package com.umonitoring.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.google.android.material.button.MaterialButton;
import com.umonitoring.R;
import com.umonitoring.api.MotoristaAPI;
import com.umonitoring.api.PassageiroAPI;
import com.umonitoring.utils.Criptografia;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;

public class CadastroPage extends AppCompatActivity {

    LinearLayout layoutPassageiro, layoutMotorista;
    Button btnPassageiro, btnMotorista;
    MaterialButton btnCadastrar;

    EditText editNomePassageiro, editSobrenomePassageiro, editTelefonePassageiro, editEmailPassageiro, editSenhaPassageiro, editConfirmarSenhaPassageiro;
    EditText editNomeMotorista, editSobrenomeMotorista, editTelefoneMotorista, editEmailMotorista, editModeloVeiculoMotorista, editPlacaMotorista, editSenhaMotorista, editConfirmarSenhaMotorista;

    boolean tipoPassageiroSelecionado = false;
    boolean tipoMotoristaSelecionado = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_page);

        layoutPassageiro = findViewById(R.id.layoutPassageiro);
        layoutMotorista = findViewById(R.id.layoutMotorista);
        btnPassageiro = findViewById(R.id.btnPassageiro);
        btnMotorista = findViewById(R.id.btnMotorista);
        btnCadastrar = findViewById(R.id.btnCadastrar);

        editNomePassageiro = findViewById(R.id.editNomePassageiro);
        editSobrenomePassageiro = findViewById(R.id.editSobrenomePassageiro);
        editTelefonePassageiro = findViewById(R.id.editTelefonePassageiro);
        editEmailPassageiro = findViewById(R.id.editEmailPassageiro);
        editSenhaPassageiro = findViewById(R.id.editSenhaPassageiro);
        editConfirmarSenhaPassageiro = findViewById(R.id.editConfirmarSenhaPassageiro);

        editNomeMotorista = findViewById(R.id.editNomeMotorista);
        editSobrenomeMotorista = findViewById(R.id.editSobrenomeMotorista);
        editTelefoneMotorista = findViewById(R.id.editTelefoneMotorista);
        editEmailMotorista = findViewById(R.id.editEmailMotorista);
        editModeloVeiculoMotorista = findViewById(R.id.editModeloVeiculoMotorista);
        editPlacaMotorista = findViewById(R.id.editPlacaMotorista);
        editSenhaMotorista = findViewById(R.id.editSenhaMotorista);
        editConfirmarSenhaMotorista = findViewById(R.id.editConfirmarSenhaMotorista);

        btnPassageiro.setOnClickListener(v -> {
            layoutPassageiro.setVisibility(View.VISIBLE);
            layoutMotorista.setVisibility(View.GONE);
            tipoPassageiroSelecionado = true;
            tipoMotoristaSelecionado = false;
        });

        btnMotorista.setOnClickListener(v -> {
            layoutMotorista.setVisibility(View.VISIBLE);
            layoutPassageiro.setVisibility(View.GONE);
            tipoMotoristaSelecionado = true;
            tipoPassageiroSelecionado = false;
        });

        btnCadastrar.setOnClickListener(v -> {
            if (tipoPassageiroSelecionado) {
                String nome = editNomePassageiro.getText().toString().trim();
                String sobrenome = editSobrenomePassageiro.getText().toString().trim();
                String telefone = editTelefonePassageiro.getText().toString().trim();
                String email = editEmailPassageiro.getText().toString().trim();
                String senha = editSenhaPassageiro.getText().toString().trim();
                String confirmarSenha = editConfirmarSenhaPassageiro.getText().toString().trim();

                if (!senha.equals(confirmarSenha)) {
                    Toast.makeText(this, "As senhas não coincidem", Toast.LENGTH_SHORT).show();
                    return;
                }

                JSONObject json = new JSONObject();
                try {
                    json.put("nome", Criptografia.criptografar(nome));
                    json.put("sobrenome", Criptografia.criptografar(sobrenome));
                    json.put("telefone", Criptografia.criptografar(telefone));
                    json.put("email", Criptografia.criptografar(email));
                    json.put("senha", Criptografia.criptografar(senha));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                new Thread(() -> {
                    String resposta = PassageiroAPI.cadastrar(json);

                    runOnUiThread(() -> {
                        // DEBUG VISUAL + LOGCAT
                        Toast.makeText(this, "Resposta bruta:\n" + resposta, Toast.LENGTH_LONG).show();
                        System.out.println("📨 DEBUG RESPOSTA PASSAGEIRO: " + resposta);

                        if (resposta.toLowerCase().contains("sucesso")) {
                            startActivity(new Intent(this, LoginPage.class));
                            finish();
                        }
                    });
                }).start();

            } else if (tipoMotoristaSelecionado) {
                String nome = editNomeMotorista.getText().toString().trim();
                String sobrenome = editSobrenomeMotorista.getText().toString().trim();
                String telefone = editTelefoneMotorista.getText().toString().trim();
                String email = editEmailMotorista.getText().toString().trim();
                String modelo = editModeloVeiculoMotorista.getText().toString().trim();
                String placa = editPlacaMotorista.getText().toString().trim();
                String senha = editSenhaMotorista.getText().toString().trim();
                String confirmarSenha = editConfirmarSenhaMotorista.getText().toString().trim();

                if (!senha.equals(confirmarSenha)) {
                    Toast.makeText(this, "As senhas não coincidem", Toast.LENGTH_SHORT).show();
                    return;
                }

                JSONObject json = new JSONObject();
                try {
                    json.put("nome", Criptografia.criptografar(nome));
                    json.put("sobrenome", Criptografia.criptografar(sobrenome));
                    json.put("telefone", Criptografia.criptografar(telefone));
                    json.put("email", Criptografia.criptografar(email));
                    json.put("senha", Criptografia.criptografar(senha));
                    json.put("modelo_do_carro", Criptografia.criptografar(modelo));
                    json.put("placa_do_carro", Criptografia.criptografar(placa));
                    json.put("frase_de_seguranca_1", Criptografia.criptografar("padrão1"));
                    json.put("frase_de_seguranca_2", Criptografia.criptografar("padrão2"));
                    json.put("frase_de_seguranca_3", Criptografia.criptografar("padrão3"));
                    json.put("disponibilidade", "Disponível");
                    json.put("status_protocolo", "Ativo");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                new Thread(() -> {
                    String resposta = MotoristaAPI.cadastrar(json);
                    runOnUiThread(() -> {
                        Toast.makeText(this, resposta, Toast.LENGTH_LONG).show();
                        if (resposta.toLowerCase().contains("sucesso")) {
                            startActivity(new Intent(this, LoginPage.class));
                            finish();
                        }
                    });
                }).start();
            } else {
                Toast.makeText(this, "Selecione Passageiro ou Motorista", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
