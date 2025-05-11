package com.umonitoring.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.umonitoring.R;
import com.umonitoring.services.LoginService;

public class LoginActivity extends AppCompatActivity {

    private EditText editEmail, editSenha;
    private Button btnMotoristaAuth, btnPassageiroAuth, btnLogin;
    private String tipoSelecionado = "motorista"; // padrão

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editEmail = findViewById(R.id.editTextText);
        editSenha = findViewById(R.id.editTextText2);
        btnMotoristaAuth = findViewById(R.id.btnMotoristaAuth);
        btnPassageiroAuth = findViewById(R.id.btnPassageiroAuth);
        btnLogin = findViewById(R.id.btnLongar);

        // Estado inicial
        btnMotoristaAuth.setEnabled(false); // selecionado inicialmente

        btnMotoristaAuth.setOnClickListener(v -> {
            tipoSelecionado = "motorista";
            btnMotoristaAuth.setEnabled(false);
            btnPassageiroAuth.setEnabled(true);
            Toast.makeText(this, "Login como Motorista selecionado", Toast.LENGTH_SHORT).show();
        });

        btnPassageiroAuth.setOnClickListener(v -> {
            tipoSelecionado = "passageiro";
            btnPassageiroAuth.setEnabled(false);
            btnMotoristaAuth.setEnabled(true);
            Toast.makeText(this, "Login como Passageiro selecionado", Toast.LENGTH_SHORT).show();
        });


        btnLogin.setOnClickListener(v -> {
            String email = editEmail.getText().toString().trim();
            String senha = editSenha.getText().toString().trim();

            if (email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                return;
            }

            LoginService.fazerLogin(this, email, senha, tipoSelecionado, new LoginService.LoginCallback() {
                @Override
                public void onSuccess(int id, String tipo) {
                    Toast.makeText(LoginActivity.this, "Login realizado como " + tipo + "!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(LoginActivity.this, TelaDeViagemActivity.class);
                    intent.putExtra("usuarioId", id);
                    intent.putExtra("tipo", tipo);
                    startActivity(intent);
                    finish();
                }



                @Override
                public void onError(String mensagem) {
                    Toast.makeText(LoginActivity.this, mensagem, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
