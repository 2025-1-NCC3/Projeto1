package com.umonitoring.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.umonitoring.R;
import com.umonitoring.api.MotoristaAPI;
import com.umonitoring.api.PassageiroAPI;
import com.umonitoring.api.ServidorConfig;
import com.umonitoring.api.ViagemAPI;
import com.umonitoring.models.Motorista;
import com.umonitoring.models.Passageiro;
import com.umonitoring.models.Viagem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btnTesteServidor, btnListarMotoristas, btnListarPassageiros, btnListarViagens;

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
        btnListarMotoristas = findViewById(R.id.btnListarMotoristas);
        btnListarPassageiros = findViewById(R.id.btnListarPassageiros);
        btnListarViagens = findViewById(R.id.btnListarViagens);

        btnTesteServidor.setOnClickListener(view -> {
            ServidorConfig.detectarServidor(this);
        });


        btnListarMotoristas.setOnClickListener(view -> {
            new Thread(() -> {
                Motorista debugMotorista = new Motorista(0, "", "", "", "", "", "", "", "", "", "", "", "");
                List<Motorista> motoristas = debugMotorista.listarMotoristas();

                runOnUiThread(() -> {
                    Toast.makeText(this, "Total encontrados: " + motoristas.size(), Toast.LENGTH_SHORT).show();

                    StringBuilder resultado = new StringBuilder("=== MOTORISTAS ===\n");
                    for (Motorista m : motoristas) {
                        resultado.append("ID: ").append(m.getId()).append("\n")
                                .append("Nome: ").append(m.getNome()).append(" ").append(m.getSobrenome()).append("\n")
                                .append("Telefone: ").append(m.getTelefone()).append("\n")
                                .append("Email: ").append(m.getEmail()).append("\n")
                                .append("Modelo do Carro: ").append(m.getModeloDoCarro()).append("\n")
                                .append("Placa do Carro: ").append(m.getPlacaDoCarro()).append("\n")
                                .append("Disponibilidade: ").append(m.getDisponibilidade()).append("\n")
                                .append("Status do Protocolo: ").append(m.getStatusProtocolo()).append("\n")
                                .append("Frase 1: ").append(m.getFrase1()).append("\n")
                                .append("Frase 2: ").append(m.getFrase2()).append("\n")
                                .append("Frase 3: ").append(m.getFrase3()).append("\n\n");
                    }

                    mostrarDialogo("Motoristas", resultado.toString());
                });
            }).start();
        });


        btnListarPassageiros.setOnClickListener(view -> {
            new Thread(() -> {
                List<Passageiro> passageiros = new Passageiro(0, "", "", "", "", "").listarPassageiros();

                runOnUiThread(() -> {
                    Toast.makeText(this, "Total de passageiros: " + passageiros.size(), Toast.LENGTH_SHORT).show();

                    StringBuilder resultado = new StringBuilder("=== PASSAGEIROS ===\n");
                    for (Passageiro p : passageiros) {
                        resultado.append("ID: ").append(p.getId()).append("\n")
                                .append("Nome: ").append(p.getNome()).append(" ").append(p.getSobrenome()).append("\n")
                                .append("Telefone: ").append(p.getTelefone()).append("\n")
                                .append("Email: ").append(p.getEmail()).append("\n")
                                .append("Senha: ").append(p.getSenha()).append("\n\n");
                    }

                    mostrarDialogo("Passageiros", resultado.toString());
                });
            }).start();
        });



        btnListarViagens.setOnClickListener(view -> {
            new Thread(() -> {
                List<Viagem> viagens = new Viagem(0, "", "", "", "", "", null, null).listarTodasAsViagens();

                runOnUiThread(() -> {
                    Toast.makeText(this, "Total de viagens: " + viagens.size(), Toast.LENGTH_SHORT).show();

                    StringBuilder resultado = new StringBuilder("=== VIAGENS ===\n");
                    for (Viagem v : viagens) {
                        String nomeMotorista = v.getMotorista() != null ? v.getMotorista().getNome() + " " + v.getMotorista().getSobrenome() : "Desconhecido";
                        String nomePassageiro = v.getPassageiro() != null ? v.getPassageiro().getNome() + " " + v.getPassageiro().getSobrenome() : "Desconhecido";

                        resultado.append("ID: ").append(v.getId()).append("\n")
                                .append("Partida: ").append(v.getEnderecoDePartida()).append("\n")
                                .append("Chegada: ").append(v.getEnderecoDeChegada()).append("\n")
                                .append("Data/Hora Partida: ").append(v.getDataHoraDePartida()).append("\n")
                                .append("Data/Hora Chegada: ").append(v.getDataHoraDeChegada()).append("\n")
                                .append("Status: ").append(v.getStatus()).append("\n")
                                .append("Motorista: ").append(nomeMotorista).append("\n")
                                .append("Passageiro: ").append(nomePassageiro).append("\n\n");
                    }


                    mostrarDialogo("Viagens", resultado.toString());
                });
            }).start();
        });

    }
    private void mostrarDialogo(String titulo, String mensagem) {
        androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle(titulo)
                .setMessage(mensagem)
                .setPositiveButton("OK", null)
                .create();

        dialog.show();
    }

}