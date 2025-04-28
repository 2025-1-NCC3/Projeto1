package br.com.fecapccp.umonitoring;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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

import br.com.fecapccp.u_monitoring.R;

public class MainActivity extends AppCompatActivity {

    private MapView map;
    private MyLocationNewOverlay locationOverlay;
    private Marker corridaMarker;
    private boolean corridaEmAndamento = false;
    private Thread corridaThread;
    private Polyline rotaCorrida;

    private GeoPoint origemMotorista;
    private GeoPoint localPassageiro;
    private GeoPoint destinoFinal;
    private GeoPoint localFake;

    private String enderecoEmbarqueStr = "Rua Maria Paula, 122 - Bela Vista, São Paulo - SP";
    private String enderecoDestinoStr = "Praça João Mendes, 150 - Sé, São Paulo - SP";

    private static final int PERMISSAO_MICROFONE = 1;
    private static final int PERMISSAO_LOCALIZACAO = 2;

    private TextView cabecalhoCorridaAtiva;
    private LinearLayout boxChamadaDeCorrida;
    private TextView tempoAtePassageiro, enderecoEmbarque, tempoAteDestino, enderecoDestino;
    private Button btnAceitarCorrida;

    private LinearLayout boxDeEmbarque;
    private TextView textNomePassageiro, textEnderecoDesembarque, textTempoDeCorrida;
    private Button btnInciarCorrida;


    private LinearLayout rodapeCorridaAtiva;
    private TextView tempoRestante, distanciaRestante, textView7_destino;
    private ImageView imageViewNavegacao, btnSeguranca, btnConfiguracao;

    private LinearLayout boxSetting;
    private TextView StatusProtocoloDeSeguranca;
    private Button btnEncerrarCorrida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        setContentView(R.layout.activity_main);

        cabecalhoCorridaAtiva = findViewById(R.id.cabecalhoCorridaAtiva);
        boxChamadaDeCorrida = findViewById(R.id.boxChamadaDeCorrida);
        tempoAtePassageiro = findViewById(R.id.tempoAtePassageiro);
        enderecoEmbarque = findViewById(R.id.enderecoEmbargue);
        tempoAteDestino = findViewById(R.id.tempoAteDestino);
        enderecoDestino = findViewById(R.id.enderecoDestino);
        btnAceitarCorrida = findViewById(R.id.btnAceitarCorrida);

        boxDeEmbarque = findViewById(R.id.boxDeEmbarque);
        textNomePassageiro = findViewById(R.id.textNomePassageiro);
        textEnderecoDesembarque = findViewById(R.id.textEnderecoDesembarque);
        textTempoDeCorrida = findViewById(R.id.textTempoDeCorrida);
        btnInciarCorrida = findViewById(R.id.btnInciarCorrida);


        rodapeCorridaAtiva = findViewById(R.id.rodapeCorridaAtiva);
        tempoRestante = findViewById(R.id.tempoRestante);
        distanciaRestante = findViewById(R.id.distanciaRestante);
        
        btnSeguranca = findViewById(R.id.btnSeguranca);
        btnConfiguracao = findViewById(R.id.btnConfiguracao);

        boxSetting = findViewById(R.id.boxSetting);
        StatusProtocoloDeSeguranca = findViewById(R.id.StatusProtocoloDeSeguranca);
        btnEncerrarCorrida = findViewById(R.id.btnEncerrarCorrida);

        map = findViewById(R.id.map);
        map.setMultiTouchControls(true);
        IMapController mapController = map.getController();
        mapController.setZoom(17.0);

        localFake = geocodificarEndereco("Avenida Liberdade, 532, São Paulo");

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        boxChamadaDeCorrida.setVisibility(View.VISIBLE);
        cabecalhoCorridaAtiva.setVisibility(View.INVISIBLE);
        rodapeCorridaAtiva.setVisibility(View.GONE);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSAO_LOCALIZACAO);
        } else {
            configurarLocalizacao();
        }

        btnAceitarCorrida.setOnClickListener(view -> {
            if (!corridaEmAndamento) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.RECORD_AUDIO},
                            PERMISSAO_MICROFONE);
                } else {
                    iniciarServicoDeVoz();

                    GeoPoint origem = localFake;
                    if (origem != null) {
                        GeoPoint pontoDeEmbarque = geocodificarEndereco(enderecoEmbarqueStr);
                        GeoPoint destinoFinalGeo = geocodificarEndereco(enderecoDestinoStr);

                        // Início da corrida: indo buscar passageiro
                        boxChamadaDeCorrida.setVisibility(View.GONE);
                        cabecalhoCorridaAtiva.setVisibility(View.VISIBLE);
                        rodapeCorridaAtiva.setVisibility(View.VISIBLE);
                        cabecalhoCorridaAtiva.setText("Indo buscar passageiro...");
                        boxSetting.setVisibility(View.GONE);
                        boxDeEmbarque.setVisibility(View.GONE); // <- garante que o box esteja oculto

                        corridaEmAndamento = true;

                        simularCorrida(origem, pontoDeEmbarque, () -> {
                            // Chegou no embarque
                            cabecalhoCorridaAtiva.setVisibility(View.GONE);
                            rodapeCorridaAtiva.setVisibility(View.GONE);
                            boxDeEmbarque.setVisibility(View.VISIBLE);

                            // Preenche informações do embarque
                            textNomePassageiro.setText("Fulana de Tal"); // pode vir do backend no futuro
                            textEnderecoDesembarque.setText(enderecoDestinoStr);
                            textTempoDeCorrida.setText(estimarTempo(calcularDistanciaKm(pontoDeEmbarque, destinoFinalGeo)));

                            // Configura botão para iniciar corrida ao destino
                            btnInciarCorrida.setOnClickListener(v -> {
                                boxDeEmbarque.setVisibility(View.GONE);
                                cabecalhoCorridaAtiva.setVisibility(View.VISIBLE);
                                rodapeCorridaAtiva.setVisibility(View.VISIBLE);
                                cabecalhoCorridaAtiva.setText("Rumo ao destino final...");

                                simularCorrida(pontoDeEmbarque, destinoFinalGeo, () -> {
                                    boxSetting.setVisibility(View.VISIBLE);
                                });
                            });
                        });

                    } else {
                        Toast.makeText(this, "Localização ainda não disponível", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });



        rodapeCorridaAtiva.setOnClickListener(v -> {
            boxSetting.setVisibility(View.VISIBLE);
        });

        btnEncerrarCorrida.setOnClickListener(v -> {
            corridaEmAndamento = false;
            pararCorrida();
            pararServicoDeVoz();

            if (corridaMarker != null) {
                map.getOverlays().remove(corridaMarker);
                corridaMarker = null;
            }
            if (rotaCorrida != null) {
                map.getOverlays().remove(rotaCorrida);
                rotaCorrida = null;
            }

            rodapeCorridaAtiva.setVisibility(View.GONE);
            cabecalhoCorridaAtiva.setVisibility(View.INVISIBLE);
            boxChamadaDeCorrida.setVisibility(View.VISIBLE);
            boxSetting.setVisibility(View.GONE);

            if (localFake != null) {
                map.getController().setCenter(localFake);
            }

            //Toast.makeText(this, "Corrida encerrada com sucesso.", Toast.LENGTH_SHORT).show();
        });
    }

    private void configurarLocalizacao() {
        locationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), map);
        locationOverlay.enableMyLocation();
        map.getOverlays().add(locationOverlay);

        map.getController().setCenter(localFake);

        origemMotorista = localFake;
        localPassageiro = geocodificarEndereco(enderecoEmbarqueStr);
        destinoFinal = geocodificarEndereco(enderecoDestinoStr);

        double distAtePassageiro = calcularDistanciaKm(origemMotorista, localPassageiro);
        double distAteDestino = calcularDistanciaKm(localPassageiro, destinoFinal);

        tempoAtePassageiro.setText(estimarTempo(distAtePassageiro));
        tempoAteDestino.setText(estimarTempo(distAteDestino));
        tempoRestante.setText(estimarTempo(distAteDestino));
        distanciaRestante.setText(String.format("%.1f km", distAteDestino));

        enderecoEmbarque.setText(enderecoEmbarqueStr);
        enderecoDestino.setText(enderecoDestinoStr);
    }

    private GeoPoint geocodificarEndereco(String endereco) {
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> resultados = geocoder.getFromLocationName(endereco, 1);
            if (resultados != null && !resultados.isEmpty()) {
                Address local = resultados.get(0);
                return new GeoPoint(local.getLatitude(), local.getLongitude());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void simularCorrida(GeoPoint inicio, GeoPoint fim, Runnable aoFinalizar) {
        if (corridaMarker != null) {
            map.getOverlays().remove(corridaMarker);
        }

        corridaMarker = new Marker(map);
        corridaMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(corridaMarker);

        rotaCorrida = new Polyline();
        rotaCorrida.setWidth(5f);
        rotaCorrida.setColor(0xAA00796B);
        rotaCorrida.setPoints(Arrays.asList(inicio, fim));
        map.getOverlays().add(rotaCorrida);

        corridaThread = new Thread(() -> {
            int steps = 100;
            double latStep = (fim.getLatitude() - inicio.getLatitude()) / steps;
            double lonStep = (fim.getLongitude() - inicio.getLongitude()) / steps;

            for (int i = 0; i <= steps && corridaEmAndamento; i++) {
                double lat = inicio.getLatitude() + (latStep * i);
                double lon = inicio.getLongitude() + (lonStep * i);
                GeoPoint pontoAtual = new GeoPoint(lat, lon);

                double distanciaRest = calcularDistanciaKm(pontoAtual, fim);
                String tempoEstimado = estimarTempo(distanciaRest);
                String distanciaTexto = String.format("%.1f km", distanciaRest);

                runOnUiThread(() -> {
                    corridaMarker.setPosition(pontoAtual);
                    map.getController().setCenter(pontoAtual);
                    map.invalidate();

                    tempoRestante.setText(tempoEstimado);
                    distanciaRestante.setText(distanciaTexto);
                });

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    break;
                }
            }

            runOnUiThread(() -> {
                if (aoFinalizar != null) {
                    aoFinalizar.run();
                } else {
                    //Toast.makeText(this, "Corrida finalizada visualmente. Aguarde encerramento manual.", Toast.LENGTH_SHORT).show();
                    boxSetting.setVisibility(View.VISIBLE);
                }
            });
        });

        corridaThread.start();
    }

    private double calcularDistanciaKm(GeoPoint p1, GeoPoint p2) {
        double R = 6371.0;
        double dLat = Math.toRadians(p2.getLatitude() - p1.getLatitude());
        double dLon = Math.toRadians(p2.getLongitude() - p1.getLongitude());
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(p1.getLatitude())) * Math.cos(Math.toRadians(p2.getLatitude()))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }

    private String estimarTempo(double distanciaKm) {
        double velocidadeMediaKmH = 40.0;
        int minutos = (int) Math.round((distanciaKm / velocidadeMediaKmH) * 60);
        return minutos + " min";
    }

    private void pararCorrida() {
        if (corridaThread != null && corridaThread.isAlive()) {
            corridaThread.interrupt();
        }
    }

    private void iniciarServicoDeVoz() {
        //Toast.makeText(this, "Iniciando reconhecimento de voz...", Toast.LENGTH_SHORT).show();
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