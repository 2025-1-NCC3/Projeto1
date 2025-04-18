package br.com.fecapccp.u_monitoring;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import java.util.Arrays;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import android.preference.PreferenceManager;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btnAceitarCorrida;
    private MapView map;
    private MyLocationNewOverlay locationOverlay;
    private Marker corridaMarker;
    private boolean corridaEmAndamento = false;
    private Thread corridaThread;

    private static final int PERMISSAO_MICROFONE = 1;
    private static final int PERMISSAO_LOCALIZACAO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));

        setContentView(R.layout.activity_main);

        map = findViewById(R.id.map);
        map.setMultiTouchControls(true);
        IMapController mapController = map.getController();
        mapController.setZoom(17.0);

        btnAceitarCorrida = findViewById(R.id.btnAceitarCorrida);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Permissão de localização
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSAO_LOCALIZACAO);
        } else {
            configurarLocalizacao();
        }

        // Botão iniciar/encerrar corrida
        btnAceitarCorrida.setOnClickListener(view -> {
            if (!corridaEmAndamento) {
                // Iniciar
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.RECORD_AUDIO},
                            PERMISSAO_MICROFONE);
                } else {
                    iniciarServicoDeVoz();

                    GeoPoint origem = locationOverlay.getMyLocation();
                    if (origem != null) {
                        GeoPoint destino = new GeoPoint(origem.getLatitude() + 0.005, origem.getLongitude() + 0.005);
                        simularCorrida(origem, destino);
                        btnAceitarCorrida.setText("Encerrar Corrida");
                        corridaEmAndamento = true;
                    } else {
                        Toast.makeText(this, "Localização ainda não disponível", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                // Encerrar
                corridaEmAndamento = false;
                btnAceitarCorrida.setText("Iniciar Corrida");
                pararCorrida();
                pararServicoDeVoz();
                Toast.makeText(this, "Corrida encerrada.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void configurarLocalizacao() {
        locationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), map);
        locationOverlay.enableMyLocation();
        map.getOverlays().add(locationOverlay);

        locationOverlay.runOnFirstFix(() -> runOnUiThread(() -> {
            GeoPoint location = locationOverlay.getMyLocation();
            if (location != null) {
                map.getController().setCenter(location);
            }
        }));
    }

    private void simularCorrida(GeoPoint inicio, GeoPoint fim) {
        if (corridaMarker != null) {
            map.getOverlays().remove(corridaMarker);
        }

        corridaMarker = new Marker(map);
        corridaMarker.setTitle("Veículo em movimento");
        corridaMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(corridaMarker);

        Polyline rota = new Polyline();
        rota.setWidth(5f);
        rota.setColor(0xAA00796B);
        rota.setPoints(Arrays.asList(inicio, fim));
        map.getOverlays().add(rota);
        map.invalidate();

        corridaThread = new Thread(() -> {
            int steps = 100;
            double latStep = (fim.getLatitude() - inicio.getLatitude()) / steps;
            double lonStep = (fim.getLongitude() - inicio.getLongitude()) / steps;

            for (int i = 0; i <= steps && corridaEmAndamento; i++) {
                double lat = inicio.getLatitude() + (latStep * i);
                double lon = inicio.getLongitude() + (lonStep * i);
                GeoPoint pontoAtual = new GeoPoint(lat, lon);

                runOnUiThread(() -> {
                    corridaMarker.setPosition(pontoAtual);
                    map.getController().setCenter(pontoAtual);
                    map.invalidate();
                });

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    break;
                }
            }

            runOnUiThread(() -> {
                Toast.makeText(this, "Corrida finalizada visualmente. Aguarde encerramento manual.", Toast.LENGTH_SHORT).show();
            });
        });
        corridaThread.start();
    }

    private void pararCorrida() {
        if (corridaThread != null && corridaThread.isAlive()) {
            corridaThread.interrupt();
        }
    }

    private void iniciarServicoDeVoz() {
        Toast.makeText(this, "Iniciando reconhecimento de voz...", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, VoiceRecognitionService.class);
        startService(intent);
    }

    private void pararServicoDeVoz() {
        Intent intent = new Intent(this, VoiceRecognitionService.class);
        stopService(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSAO_MICROFONE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                iniciarServicoDeVoz();
            } else {
                Toast.makeText(this, "Permissão de microfone negada!", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == PERMISSAO_LOCALIZACAO) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                configurarLocalizacao();
            } else {
                Toast.makeText(this, "Permissão de localização negada!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
    }
}
