package br.com.fecapccp.u_monitoring;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import java.util.ArrayList;
import java.util.Locale;

public class VoiceRecognitionService extends Service {

    private SpeechRecognizer speechRecognizer;
    private Intent recognizerIntent;

    private final String[] frasesDeSeguranca = {
            "banana azul", "nave espacial", "emergência agora"

    };

    @Override
    public void onCreate() {
        super.onCreate();
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

    private void reiniciarReconhecimento() {
        if (speechRecognizer != null) {
            speechRecognizer.cancel();
            speechRecognizer.startListening(recognizerIntent);
        }
    }

    private void acionarProtocoloDeSeguranca() {
        Log.d("SEGURANÇA", "🚨 Protocolo de segurança ativado!");
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
