package com.umonitoring.controllers;

import android.content.Context;

import com.umonitoring.components.BoxChamadaView;
import com.umonitoring.models.Viagem;

import java.util.ArrayList;
import java.util.List;

public class ViagemController {

    private final Context context;
    private final BoxChamadaView boxChamadaView;
    private final List<Viagem> viagensDisponiveis = new ArrayList<>();
    private int indexAtual = 0;

    public ViagemController(Context context, BoxChamadaView boxChamadaView) {
        this.context = context;
        this.boxChamadaView = boxChamadaView;
    }

    public void carregarViagens() {
        new Thread(() -> {
            List<Viagem> lista = Viagem.listarViagensDisponiveis();

            ((android.app.Activity) context).runOnUiThread(() -> {
                viagensDisponiveis.clear();
                viagensDisponiveis.addAll(lista);
                indexAtual = 0;
                mostrarProxima();
            });
        }).start();
    }

    public void mostrarProxima() {
        if (viagensDisponiveis.isEmpty()) return;

        Viagem v = viagensDisponiveis.get(indexAtual);

        boxChamadaView.setDados(
                v.getEnderecoDePartida(),
                "5 min",
                v.getEnderecoDeChegada(),
                "12 min"
        );
    }

    public void avancarViagem() {
        indexAtual = (indexAtual + 1) % viagensDisponiveis.size();
        mostrarProxima();
    }

    public Viagem getViagemAtual() {
        if (viagensDisponiveis.isEmpty()) return null;
        return viagensDisponiveis.get(indexAtual);
    }
}
