package com.umonitoring.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.umonitoring.R;


public class DetalhesViagemActivity extends AppCompatActivity {

    private TextView textNomePassageiro, textDataHoraPartida, textDataHoraDestino, textEnderecoPartida, textEnderecoDestino, textidViagem;
    private ImageView BtnVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detalhes_viagem);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textNomePassageiro = findViewById(R.id.textNomePassageiro);
       //textidViagem = findViewById(R.id.textidViagem);
       textDataHoraPartida = findViewById(R.id.textDataHoraPartida);
       textEnderecoPartida = findViewById(R.id.textEnderecoPartida);
       textDataHoraDestino = findViewById(R.id.textDataHoraDestino);
       textEnderecoDestino = findViewById(R.id.textEnderecoDestino);
       BtnVoltar = findViewById(R.id.BtnVoltar);

        // Pegando os dados passados pela MainActivity
        //String id = getIntent().getStringExtra("id");
        String partida = getIntent().getStringExtra("Endereco_Partida");
        String destino = getIntent().getStringExtra("Endereco_Destino");
        String partidaHoraData = getIntent().getStringExtra("DataHoraPartida");
        String destinoHoraData = getIntent().getStringExtra("DataHoraDestino");
        //String id_motorista = getIntent().getStringExtra("id_motorista");
        //String id_passageiro = getIntent().getStringExtra("id_passageiro");
        String passageiro = getIntent().getStringExtra("passageiro");

        //String id_Viagem = id;
        String HoraDataDestino = destinoHoraData;
        String Destino_Endereco = destino;
        String HoraDataPartida = partidaHoraData;
        String Partida_Endereco = partida;
        //String Passageiro_id = id_passageiro;
        String Passageiro_nome = passageiro;

        //textidViagem.setText("Viagem:" + id_Viagem);
        //textPassageiro.setText("Passageiro: " + Passageiro_id);
        textNomePassageiro.setText(Passageiro_nome);
        textDataHoraPartida.setText(HoraDataPartida);
        textEnderecoPartida.setText(Partida_Endereco);
        textDataHoraDestino.setText(HoraDataDestino);
        textEnderecoDestino.setText(Destino_Endereco);

        BtnVoltar.setOnClickListener(v -> finish());

    }
}