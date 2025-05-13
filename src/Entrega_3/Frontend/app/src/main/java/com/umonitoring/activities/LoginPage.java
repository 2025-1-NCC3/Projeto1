package com.umonitoring.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.umonitoring.R;
import com.umonitoring.api.ServidorConfig;
import com.umonitoring.utils.Criptografia;

import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginPage extends AppCompatActivity {

    EditText editLogin, editSenha;
    MaterialButton btnEntrar;
    RadioGroup radioTipo;
    RadioButton radioPassageiro, radioMotorista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        editLogin = findViewById(R.id.editLogin);
        editSenha = findViewById(R.id.editSenha);
        btnEntrar = findViewById(R.id.btnEntrar);
        radioTipo = findViewById(R.id.radioTipo);
        radioPassageiro = findViewById(R.id.radioPassageiro);
        radioMotorista = findViewById(R.id.radioMotorista);

        btnEntrar.setOnClickListener(v -> {
            String login = editLogin.getText().toString().trim();
            String senha = editSenha.getText().toString().trim();

            if (login.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                return;
            }

            int idSelecionado = radioTipo.getCheckedRadioButtonId();
            if (idSelecionado == -1) {
                Toast.makeText(this, "Selecione Passageiro ou Motorista", Toast.LENGTH_SHORT).show();
                return;
            }

            String tipoLogin = (idSelecionado == R.id.radioPassageiro) ? "passageiro" : "motorista";

            new Thread(() -> {
                try {
                    JSONObject json = new JSONObject();

                    String emailCripto = Criptografia.criptografar(login);
                    String senhaCripto = Criptografia.criptografar(senha);

                    json.put("email", emailCripto);
                    json.put("senha", senhaCripto);

                    String baseUrl = ServidorConfig.getUrl("login/" + tipoLogin);
                    Log.d("LOGIN_DEBUG", "POST para: " + baseUrl);
                    Log.d("LOGIN_DEBUG", "JSON: " + json.toString());

                    URL url = new URL(baseUrl);
                    HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
                    conexao.setRequestMethod("POST");
                    conexao.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    conexao.setDoOutput(true);

                    OutputStream os = conexao.getOutputStream();
                    os.write(json.toString().getBytes("UTF-8"));
                    os.flush();
                    os.close();

                    int responseCode = conexao.getResponseCode();
                    Log.d("LOGIN_DEBUG", "Código de resposta: " + responseCode);

                    BufferedReader reader;
                    if (responseCode >= 200 && responseCode < 300) {
                        reader = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
                    } else {
                        reader = new BufferedReader(new InputStreamReader(conexao.getErrorStream()));
                    }

                    StringBuilder sb = new StringBuilder();
                    String linha;
                    while ((linha = reader.readLine()) != null) {
                        sb.append(linha);
                    }
                    reader.close();

                    String respostaTexto = sb.toString();
                    Log.d("LOGIN_DEBUG", "Resposta da API: " + respostaTexto);

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        JSONObject resposta = new JSONObject(respostaTexto);
                        JSONObject usuario = resposta.getJSONObject("usuario");
                        int idUsuario = usuario.getInt("id");

                        runOnUiThread(() -> {
                            Toast.makeText(this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(this, TelaDeViagemActivity.class);
                            intent.putExtra("idUsuario", idUsuario);
                            startActivity(intent);
                        });

                    } else {
                        runOnUiThread(() -> Toast.makeText(this, "Erro: " + respostaTexto, Toast.LENGTH_LONG).show());
                    }

                } catch (Exception e) {
                    Log.e("LOGIN_DEBUG", "Erro: ", e);
                    runOnUiThread(() -> Toast.makeText(this, "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show());
                }
            }).start();
        });
    }
}
