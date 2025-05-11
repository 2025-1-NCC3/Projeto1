package com.umonitoring.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umonitoring.R;

public class BoxEmbarqueView extends LinearLayout {

    private TextView textNomePassageiro, textEnderecoDesembarque, textTempoDeCorrida;
    private Button btnIniciarCorrida;
    private OnEmbarqueActionListener listener;

    public BoxEmbarqueView(Context context) {
        super(context);
        init(context);
    }

    public BoxEmbarqueView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_box_embarque, this, true);

        textNomePassageiro = findViewById(R.id.textNomePassageiro);
        textEnderecoDesembarque = findViewById(R.id.textEnderecoDesembarque);
        textTempoDeCorrida = findViewById(R.id.textTempoDeCorrida);
        btnIniciarCorrida = findViewById(R.id.btnInciarCorrida);

        btnIniciarCorrida.setOnClickListener(v -> {
            if (listener != null) listener.onIniciarCorrida();
        });
    }

    public void setDados(String nome, String destino, String tempoEstimado) {
        textNomePassageiro.setText(nome);
        textEnderecoDesembarque.setText(destino);
        textTempoDeCorrida.setText(tempoEstimado);
    }

    public void setOnEmbarqueActionListener(OnEmbarqueActionListener listener) {
        this.listener = listener;
    }

    public interface OnEmbarqueActionListener {
        void onIniciarCorrida();
    }
}
