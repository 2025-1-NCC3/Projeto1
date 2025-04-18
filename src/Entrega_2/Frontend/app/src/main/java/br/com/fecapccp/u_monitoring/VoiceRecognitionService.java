package br.com.fecapccp.u_monitoring;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.Locale;

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
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "pt-BR"); // <- aqui está o ponto chave

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

        // Cria o canal de notificação (Android 8+)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel canal = new NotificationChannel(
                    canalId,
                    canalNome,
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(canal);
            }
        }

        // Cria a notificação
        NotificationCompat.Builder notificacao = new NotificationCompat.Builder(this, canalId)
                .setContentTitle("Monitoramento de Segurança Ativo")
                .setContentText("Escutando por frases de segurança...")
                .setSmallIcon(R.mipmap.ic_launcher) // substitua por um ícone do seu app
                .setPriority(NotificationCompat.PRIORITY_LOW);

        // Inicia o serviço como foreground
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

        // Exibe um Toast para o usuário
        android.os.Handler handler = new android.os.Handler(getMainLooper());
        handler.post(() -> {
            android.widget.Toast.makeText(getApplicationContext(),
                    "🚨 Protocolo de segurança ativado!", android.widget.Toast.LENGTH_LONG).show();
        });

        // Para o reconhecimento de voz
        if (speechRecognizer != null) {
            speechRecognizer.stopListening();
            speechRecognizer.destroy();
            speechRecognizer = null;
        }
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
