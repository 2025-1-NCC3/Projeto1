package com.umonitoring.components;

import android.content.Context;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umonitoring.R;


public class BoxChamadaView extends LinearLayout {

    private TextView tempoAtePassageiro, enderecoEmbargue, tempoAteDestino, enderecoDestino;
    private Button btnAceitarCorrida;
    private ImageButton btnRecusarCorrida;

    private OnChamadaActionListener listener;

    public BoxChamadaView(Context context) {
        super(context);
        init(context);
    }

    public BoxChamadaView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_box_chamada, this, true);

        tempoAtePassageiro = findViewById(R.id.tempoAtePassageiro);
        enderecoEmbargue = findViewById(R.id.enderecoEmbargue);
        tempoAteDestino = findViewById(R.id.tempoAteDestino);
        enderecoDestino = findViewById(R.id.enderecoDestino);
        btnAceitarCorrida = findViewById(R.id.btnAceitarCorrida);
        btnRecusarCorrida = findViewById(R.id.btnRecusarCorrida);

        btnAceitarCorrida.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAceitarCorrida(
                        enderecoEmbargue.getText().toString(),
                        enderecoDestino.getText().toString(),
                        "Passageiro ID: 1" // Esse valor pode vir do backend
                );
            }
        });

        btnRecusarCorrida.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRecusarCorrida();
            }

        });
    }

    public void setDados(String embarque, String tempoPassageiro, String destino, String tempoDestino) {
        enderecoEmbargue.setText(embarque);
        tempoAtePassageiro.setText(tempoPassageiro);
        enderecoDestino.setText(destino);
        tempoAteDestino.setText(tempoDestino);
    }



    public void setOnChamadaActionListener(OnChamadaActionListener listener) {
        this.listener = listener;
    }

    public interface OnChamadaActionListener {
        void onAceitarCorrida(String partida, String destino, String nomePassageiro);
        void onRecusarCorrida();
    }
}
