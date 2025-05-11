package com.umonitoring.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.umonitoring.R;
import com.umonitoring.activities.TelaDeViagemActivity;

import java.util.ArrayList;

public class VoiceRecognitionService extends Service {

    private SpeechRecognizer speechRecognizer;
    private Intent recognizerIntent;

    private final String[] frasesDeSeguranca = {
            "banana azul",
            "nave espacial",
            "emergência agora"
    };

    @Override
    public void onCreate() {
        super.onCreate();
        iniciarComoServicoEmPrimeiroPlano();
        iniciarReconhecimento();
    }

    private void iniciarReconhecimento() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "pt-BR");

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                if (matches != null) {
                    Log.d("RECONHECIMENTO", "onResults chamado");
                    Log.d("RECONHECIMENTO", "Frases captadas: " + matches.toString());

                    for (String result : matches) {
                        for (String gatilho : frasesDeSeguranca) {
                            if (result.toLowerCase().contains(gatilho.toLowerCase())) {
                                Log.d("RECONHECIMENTO", "🔐 Frase de segurança detectada: " + result);
                                acionarProtocoloDeSeguranca();
                                break;
                            }
                        }
                    }
                } else {
                    Log.d("RECONHECIMENTO", "Nenhuma frase reconhecida");
                }

                reiniciarReconhecimento();
            }

            @Override public void onError(int error) { reiniciarReconhecimento(); }
            @Override public void onReadyForSpeech(Bundle params) {}
            @Override public void onBeginningOfSpeech() {}
            @Override public void onRmsChanged(float rmsdB) {}
            @Override public void onBufferReceived(byte[] buffer) {}
            @Override public void onEndOfSpeech() {}
            @Override public void onPartialResults(Bundle partialResults) {}
            @Override public void onEvent(int eventType, Bundle params) {}
        });

        speechRecognizer.startListening(recognizerIntent);
    }

    private void iniciarComoServicoEmPrimeiroPlano() {
        String canalId = "monitoramento_de_voz";
        String canalNome = "Monitoramento de voz";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel canal = new NotificationChannel(
                    canalId, canalNome, NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(canal);
            }
        }

        NotificationCompat.Builder notificacao = new NotificationCompat.Builder(this, canalId)
                .setContentTitle("Monitoramento de Segurança Ativo")
                .setContentText("Escutando por frases de segurança...")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_LOW);

        startForeground(1, notificacao.build());
    }

    private void reiniciarReconhecimento() {
        if (speechRecognizer != null) {
            speechRecognizer.cancel();
            speechRecognizer.startListening(recognizerIntent);
        }
    }

    private void acionarProtocoloDeSeguranca() {
        Log.d("SEGURANÇA", "🚨 Protocolo de segurança ativado!");

        Intent intent = new Intent(getApplicationContext(), TelaDeViagemActivity.class);
        intent.setAction("ativar_protocolo");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);

        new Handler(getMainLooper()).post(() -> {
            Toast.makeText(getApplicationContext(),
                    "🚨 Protocolo de segurança ativado!", Toast.LENGTH_LONG).show();
        });
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
        super.onDestroy();
    }
}
