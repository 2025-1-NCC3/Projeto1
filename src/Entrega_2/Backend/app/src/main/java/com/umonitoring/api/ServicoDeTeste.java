package com.umonitoring.api;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServicoDeTeste {
    private static final String BASE_URL = ServidorConfig.getUrl("");

    public static void testarConexao(Activity activity) {
        new Thread(() -> {
            try {
                URL url = new URL(BASE_URL);
                HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
                conexao.setRequestMethod("GET");
                conexao.setConnectTimeout(2000);

                BufferedReader reader = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
                StringBuilder resposta = new StringBuilder();
                String linha;

                while ((linha = reader.readLine()) != null) {
                    resposta.append(linha);
                }
                reader.close();

                String msg = "✅ Conectado: " + resposta;
                Log.d("ServicoDeTeste", msg);

                activity.runOnUiThread(() ->
                        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show()
                );

            } catch (Exception e) {
                String erro = "❌ Erro de conexão: " + e.getMessage();
                Log.e("ServicoDeTeste", erro);

                activity.runOnUiThread(() ->
                        Toast.makeText(activity, erro, Toast.LENGTH_LONG).show()
                );
            }
        }).start();
    }
}
