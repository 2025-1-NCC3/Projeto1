package com.umonitoring.activities;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.umonitoring.R;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.umonitoring.components.BoxChamadaView;
import com.umonitoring.components.BoxEmbarqueView;
import com.umonitoring.controllers.ViagemController;
import com.umonitoring.models.Viagem;
import com.umonitoring.services.VoiceRecognitionService;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import android.preference.PreferenceManager;

public class TelaDeViagemActivity extends AppCompatActivity {

    private int idMotorista;
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

    private String enderecoEmbarqueStr = "Avenida Liberdade, 532, São Paulo";
    private String enderecoDestinoStr = "Avenida Liberdade, 532, São Paulo";

    private static final int PERMISSAO_MICROFONE = 1;
    private static final int PERMISSAO_LOCALIZACAO = 2;

    private TextView cabecalhoCorridaAtiva;
    private LinearLayout rodapeCorridaAtiva;
    private TextView tempoRestante, distanciaRestante;
    private ImageView btnSeguranca, btnConfiguracao;
    private LinearLayout boxSetting;
    private TextView StatusProtocoloDeSeguranca;
    private Button btnEncerrarCorrida;

    private ViagemController viagemController;
    private BoxChamadaView boxChamada;
    private BoxEmbarqueView boxEmbarque;

    private LinearLayout overlayProtocolo;

    private BroadcastReceiver protocoloReceiver;


    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String partida = intent.getStringExtra("partida");
        String chegada = intent.getStringExtra("chegada");

        if (partida != null && chegada != null) {
            enderecoEmbarqueStr = partida;
            enderecoDestinoStr = chegada;
        }


        EdgeToEdge.enable(this);
        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        setContentView(R.layout.activity_tela_de_viagem);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        cabecalhoCorridaAtiva = findViewById(R.id.cabecalhoCorridaAtiva);
        rodapeCorridaAtiva = findViewById(R.id.rodapeCorridaAtiva);
        tempoRestante = findViewById(R.id.tempoRestante);
        distanciaRestante = findViewById(R.id.distanciaRestante);
        btnSeguranca = findViewById(R.id.btnSeguranca);
        btnConfiguracao = findViewById(R.id.btnConfiguracao);
        boxSetting = findViewById(R.id.boxSetting);
        StatusProtocoloDeSeguranca = findViewById(R.id.StatusProtocoloDeSeguranca);
        btnEncerrarCorrida = findViewById(R.id.btnEncerrarCorrida);
        boxChamada = findViewById(R.id.boxChamada);
        boxEmbarque = findViewById(R.id.boxEmbarque);

        View includedOverlay = findViewById(R.id.overlayProtocolo);
        overlayProtocolo = includedOverlay.findViewById(R.id.overlayProtocolo);


        map = findViewById(R.id.map);
        map.setMultiTouchControls(true);
        IMapController mapController = map.getController();
        mapController.setZoom(17.0);

        localFake = geocodificarEndereco("Avenida Liberdade, 532, São Paulo");

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

        rodapeCorridaAtiva.setOnClickListener(v -> boxSetting.setVisibility(View.VISIBLE));

        btnSeguranca.setOnClickListener(v -> exibirOverlayProtocolo());


        btnEncerrarCorrida.setOnClickListener(v -> {
            corridaEmAndamento = false;
            pararCorrida();
            pararServicoDeVoz();

            if (corridaMarker != null) {
                map.getOverlays().remove(corridaMarker);
                corridaMarker = null;
            }

            // Remove todas as rotas (polylines) do mapa
            for (int i = map.getOverlays().size() - 1; i >= 0; i--) {
                if (map.getOverlays().get(i) instanceof Polyline) {
                    map.getOverlays().remove(i);
                }
            }
            rotaCorrida = null;

            rodapeCorridaAtiva.setVisibility(View.GONE);
            cabecalhoCorridaAtiva.setVisibility(View.INVISIBLE);
            boxSetting.setVisibility(View.GONE);

            if (localFake != null) {
                map.getController().setCenter(localFake);
            }

            // Reiniciar fluxo
            viagemController.carregarViagens();
            boxChamada.setVisibility(View.VISIBLE);

            // Delay para garantir que a rota antiga foi removida antes de desenhar a nova
            new android.os.Handler().postDelayed(() -> {
                Viagem novaViagem = viagemController.getViagemAtual();
                if (novaViagem != null) {
                    atualizarRotaPreviaNoMapa(
                            novaViagem.getEnderecoDePartida(),
                            novaViagem.getEnderecoDeChegada()
                    );
                }
            }, 300);
        });


        viagemController = new ViagemController(this, boxChamada);
        viagemController.carregarViagens();

        boxChamada.setOnChamadaActionListener(new BoxChamadaView.OnChamadaActionListener() {
            @Override
            public void onAceitarCorrida(String partida, String destino, String nomePassageiro) {
                boxChamada.setVisibility(View.GONE);
                Viagem viagem = viagemController.getViagemAtual();
                nomePassageiro = "Passageiro";

                if (viagem != null && viagem.getPassageiro() != null) {
                    nomePassageiro = viagem.getPassageiro().getNome();
                }

                iniciarSimulacaoParaEmbarque(partida, destino, nomePassageiro);

            }

            @Override
            public void onRecusarCorrida() {
                viagemController.avancarViagem();

                Viagem viagem = viagemController.getViagemAtual();
                if (viagem != null) {
                    String partida = viagem.getEnderecoDePartida();
                    String destino = viagem.getEnderecoDeChegada();

                    GeoPoint geoOrigem = localFake;  // ponto fixo do motorista
                    GeoPoint geoPartida = geocodificarEndereco(partida);
                    GeoPoint geoDestino = geocodificarEndereco(destino);

                    String tempoAtePassageiro = "N/D";
                    String tempoEstimadoViagem = "N/D";

                    if (geoOrigem != null && geoPartida != null) {
                        double dist = calcularDistanciaKm(geoOrigem, geoPartida);
                        tempoAtePassageiro = estimarTempo(dist);
                    }

                    if (geoPartida != null && geoDestino != null) {
                        double dist = calcularDistanciaKm(geoPartida, geoDestino);
                        tempoEstimadoViagem = estimarTempo(dist);
                    }

                    boxChamada.setDados(partida, tempoAtePassageiro, destino, tempoEstimadoViagem);
                    atualizarRotaPreviaNoMapa(partida, destino);
                }
            }



        });

        boxEmbarque.setOnEmbarqueActionListener(() -> {
            boxEmbarque.setVisibility(View.GONE);
            iniciarCorrida("Passageiro", enderecoEmbarqueStr, enderecoDestinoStr);
        });

        atualizarRotaPreviaNoMapa(enderecoEmbarqueStr, enderecoDestinoStr);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent); // Atualiza o intent atual da Activity

        if ("ativar_protocolo".equals(intent.getAction())) {
            Log.d("PROTOCOLO", "🔔 Protocolo recebido via intent!");
            exibirOverlayProtocolo();
        }
    }


    private void atualizarRotaPreviaNoMapa(String partida, String destino) {
        GeoPoint origem = localFake;  // ou a localização real, se disponível
        GeoPoint embarque = geocodificarEndereco(partida);
        GeoPoint chegada = geocodificarEndereco(destino);

        if (embarque == null || chegada == null) return;

        if (rotaCorrida != null) {
            map.getOverlays().remove(rotaCorrida);
        }

        rotaCorrida = new Polyline();
        rotaCorrida.setWidth(5f);
        rotaCorrida.setColor(0xAA555555); // tom mais neutro
        rotaCorrida.setPoints(Arrays.asList(origem, embarque, chegada));
        map.getOverlays().add(rotaCorrida);
        map.invalidate();
    }


    private void iniciarSimulacaoParaEmbarque(String partida, String destino, String nomePassageiro) {
        enderecoEmbarqueStr = partida;
        enderecoDestinoStr = destino;

        iniciarServicoDeVoz();

        GeoPoint origem = localFake;
        GeoPoint pontoDeEmbarque = geocodificarEndereco(partida);
        GeoPoint destinoGeo = geocodificarEndereco(destino);

        cabecalhoCorridaAtiva.setText("Indo buscar passageiro...");
        cabecalhoCorridaAtiva.setVisibility(View.VISIBLE);
        rodapeCorridaAtiva.setVisibility(View.VISIBLE);
        boxSetting.setVisibility(View.GONE);
        corridaEmAndamento = true;

        simularCorrida(origem, pontoDeEmbarque, () -> {
            cabecalhoCorridaAtiva.setVisibility(View.GONE);
            rodapeCorridaAtiva.setVisibility(View.GONE);
            boxEmbarque.setVisibility(View.VISIBLE);

            double distDestino = calcularDistanciaKm(pontoDeEmbarque, destinoGeo);
            boxEmbarque.setDados(nomePassageiro, destino, estimarTempo(distDestino));
        });
    }

    public void iniciarCorrida(String nomePassageiro, String partida, String chegada) {
        enderecoEmbarqueStr = partida;
        enderecoDestinoStr = chegada;

        GeoPoint pontoDeEmbarque = geocodificarEndereco(partida);
        GeoPoint destinoFinalGeo = geocodificarEndereco(chegada);

        cabecalhoCorridaAtiva.setText("Rumo ao destino final...");
        cabecalhoCorridaAtiva.setVisibility(View.VISIBLE);
        rodapeCorridaAtiva.setVisibility(View.VISIBLE);
        boxSetting.setVisibility(View.GONE);
        corridaEmAndamento = true;

        simularCorrida(pontoDeEmbarque, destinoFinalGeo, () -> {
            boxSetting.setVisibility(View.VISIBLE);
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

        tempoRestante.setText(estimarTempo(distAteDestino));
        distanciaRestante.setText(String.format("%.1f km", distAteDestino));
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

    private void exibirOverlayProtocolo() {
        Log.d("OVERLAY", "🛡️ exibirOverlayProtocolo chamado");
        overlayProtocolo.setVisibility(View.VISIBLE);
        overlayProtocolo.bringToFront();

        new Handler().postDelayed(() -> {
            overlayProtocolo.setVisibility(View.INVISIBLE);
        }, 2000);
    }



    private void iniciarServicoDeVoz() {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

}
