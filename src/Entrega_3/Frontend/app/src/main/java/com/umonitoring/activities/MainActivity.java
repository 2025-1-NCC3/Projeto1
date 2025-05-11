package com.umonitoring.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.umonitoring.R;
import com.umonitoring.models.Motorista;
import com.umonitoring.models.Viagem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Viagem> viagensDisponiveis = new ArrayList<>();
    private int indexAtual = 0;
    private final int motoristaAtualId = 4;

    private LinearLayout boxDeEmbarque, boxChamadaDeCorrida;
    private TextView textNomePassageiro, textEnderecoDesembarque, textTempoDeCorrida;
    private TextView tempoAtePassageiro, enderecoEmbargue, tempoAteDestino, enderecoDestino;
    private Button btnInciarCorrida, btnAceitarCorrida;
    private ImageButton btnRecusarCorrida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        boxDeEmbarque = findViewById(R.id.boxDeEmbarque);
        boxChamadaDeCorrida = findViewById(R.id.boxChamadaDeCorrida);

        textNomePassageiro = findViewById(R.id.textNomePassageiro);
        textEnderecoDesembarque = findViewById(R.id.textEnderecoDesembarque);
        textTempoDeCorrida = findViewById(R.id.textTempoDeCorrida);

        tempoAtePassageiro = findViewById(R.id.tempoAtePassageiro);
        enderecoEmbargue = findViewById(R.id.enderecoEmbargue);
        tempoAteDestino = findViewById(R.id.tempoAteDestino);
        enderecoDestino = findViewById(R.id.enderecoDestino);

        btnInciarCorrida = findViewById(R.id.btnInciarCorrida);
        btnAceitarCorrida = findViewById(R.id.btnAceitarCorrida);
        btnRecusarCorrida = findViewById(R.id.btnRecusarCorrida);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        carregarViagensDisponiveis();

        btnAceitarCorrida.setOnClickListener(v -> {
            Viagem viagem = viagensDisponiveis.get(indexAtual);
            viagem.setStatus("em andamento");

            new Thread(() -> {
                Motorista motorista = Motorista.buscarMotoristaPorId(motoristaAtualId);

                runOnUiThread(() -> {
                    if (motorista == null) {
                        Toast.makeText(getApplicationContext(), "Motorista não encontrado!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    viagem.setMotorista(motorista);
                    viagem.atualizar(viagem.getId());

                    // Envia os dados da viagem para a TelaDeViagemActivity
                    Intent intent = new Intent(MainActivity.this, TelaDeViagemActivity.class);
                    intent.putExtra("partida", viagem.getEnderecoDePartida());
                    intent.putExtra("chegada", viagem.getEnderecoDeChegada());
                    intent.putExtra("nomePassageiro", "Passageiro ID: " + viagem.getPassageiro().getId());
                    startActivity(intent);
                });
            }).start();
        });




        btnRecusarCorrida.setOnClickListener(v -> {
            indexAtual = (indexAtual + 1) % viagensDisponiveis.size();
            mostrarProximaViagem();
        });

        btnInciarCorrida.setOnClickListener(v -> {
            boxDeEmbarque.setVisibility(View.GONE);
            carregarViagensDisponiveis(); // Recarrega a lista ao finalizar uma corrida
        });
    }

    private void carregarViagensDisponiveis() {
        new Thread(() -> {
            List<Viagem> lista = Viagem.listarViagensDisponiveis();
            runOnUiThread(() -> {
                viagensDisponiveis.clear();
                viagensDisponiveis.addAll(lista);
                indexAtual = 0;
                mostrarProximaViagem();
            });
        }).start();
    }

    private void mostrarProximaViagem() {
        if (viagensDisponiveis.isEmpty()) {
            boxChamadaDeCorrida.setVisibility(View.GONE);
            Toast.makeText(this, "Nenhuma viagem disponível.", Toast.LENGTH_SHORT).show();
            return;
        }

        Viagem viagem = viagensDisponiveis.get(indexAtual);

        boxChamadaDeCorrida.setVisibility(View.VISIBLE);
        boxDeEmbarque.setVisibility(View.GONE);

        textNomePassageiro.setText("Passageiro ID: " + viagem.getPassageiro().getId());
        enderecoEmbargue.setText(viagem.getEnderecoDePartida());
        enderecoDestino.setText(viagem.getEnderecoDeChegada());
        tempoAtePassageiro.setText("5 min");
        tempoAteDestino.setText("12 min");
    }

    private void iniciarCorridaComViagemAceita(Viagem viagem) {
        boxChamadaDeCorrida.setVisibility(View.GONE);
        boxDeEmbarque.setVisibility(View.VISIBLE);

        textNomePassageiro.setText("Passageiro ID: " + viagem.getPassageiro().getId());
        textEnderecoDesembarque.setText(viagem.getEnderecoDeChegada());
        textTempoDeCorrida.setText("12 min");
    }
}
