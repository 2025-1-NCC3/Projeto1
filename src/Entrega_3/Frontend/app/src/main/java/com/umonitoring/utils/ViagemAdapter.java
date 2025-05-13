package com.umonitoring.utils;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.umonitoring.R;
import com.umonitoring.activities.DetalhesViagemActivity;
import com.umonitoring.models.Viagem;

import java.util.List;

public class ViagemAdapter extends RecyclerView.Adapter<ViagemAdapter.ViagemViewHolder> {

    private Context context;
    private List<Viagem> listaViagens;

    public ViagemAdapter(Context context, List<Viagem> listaViagens) {
        this.context = context;
        this.listaViagens = listaViagens;
    }

    @NonNull
    @Override
    public ViagemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_viagem, parent, false);
        return new ViagemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViagemViewHolder holder, int position) {
        Viagem viagem = listaViagens.get(position);

        holder.textEnderecoDestino.setText(viagem.getEnderecoDeChegada());
        holder.textDataHoraViagem.setText(viagem.getDataHoraDeChegada());

        holder.btnConsultarDetalhes.setOnClickListener(v -> {
                Intent intent = new Intent(context, DetalhesViagemActivity.class);
                intent.putExtra("Endereco_Partida", viagem.getEnderecoDePartida());
                intent.putExtra("Endereco_Destino", viagem.getEnderecoDeChegada());
                intent.putExtra("DataHoraPartida", viagem.getDataHoraDePartida());
                intent.putExtra("DataHoraDestino", viagem.getDataHoraDeChegada());
                intent.putExtra("passageiro", viagem.getPassageiro().getNome());
                context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listaViagens.size();
    }

    public static class ViagemViewHolder extends RecyclerView.ViewHolder {
        TextView textEnderecoDestino, textDataHoraViagem;
        Button btnConsultarDetalhes;

        public ViagemViewHolder(@NonNull View itemView) {
            super(itemView);
            textEnderecoDestino = itemView.findViewById(R.id.textEnderecoDestino);
            textDataHoraViagem = itemView.findViewById(R.id.textDataHoraViagem);
            btnConsultarDetalhes = itemView.findViewById(R.id.btnConsultarDetalhes);
        }
    }
}
