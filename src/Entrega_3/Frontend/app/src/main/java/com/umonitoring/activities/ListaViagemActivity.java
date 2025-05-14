package com.umonitoring.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.umonitoring.R;
import com.umonitoring.components.BottomNavHelper;
import com.umonitoring.models.Motorista;
import com.umonitoring.models.Viagem;
import com.umonitoring.utils.ViagemAdapter;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;




public class ListaViagemActivity extends AppCompatActivity {

    private RecyclerView HistoricoDeViagens;
    private ViagemAdapter adapter;
    private List<Viagem> listaViagens;
    private View botaoVoltar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lista_viagem);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        HistoricoDeViagens = findViewById(R.id.HistoricoDeViagens);
        HistoricoDeViagens.setLayoutManager(new LinearLayoutManager(this));

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                int idMotorista = 2;
                Motorista motorista = Motorista.buscarMotoristaPorId(idMotorista);
                listaViagens = Viagem.listarViagensPorMotorista(motorista);

                runOnUiThread(() -> {
                    if (listaViagens != null && !listaViagens.isEmpty()) {
                        adapter = new ViagemAdapter(this, listaViagens);
                        HistoricoDeViagens.setAdapter(adapter);
                    } else {
                        Log.e("MainActivity", "Nenhuma viagem encontrada para o motorista ID: " + idMotorista);
                    }
                });
            } catch (Exception e) {
                Log.e("MainActivity", "Erro ao carregar viagens: " + e.getMessage(), e);
            }
        });
        BottomNavHelper.setupBottomNavigation(this, R.id.nav_home);

        botaoVoltar = findViewById(R.id.botaoVoltar);
        botaoVoltar.setOnClickListener(v -> finish());
    }
}
