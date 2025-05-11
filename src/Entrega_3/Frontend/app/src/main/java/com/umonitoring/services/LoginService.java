package com.umonitoring.services;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.umonitoring.activities.LoginActivity;
import com.umonitoring.api.ServidorConfig;
import com.umonitoring.utils.Criptografia;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginService {

    private static final String TAG = "LoginService";

    public interface LoginCallback {
        void onSuccess(int id, String tipo);
        void onError(String mensagem);
    }

    public static void fazerLogin(Context context, String email, String senha, String tipo, LoginCallback callback) {
        new Thread(() -> {
            try {
                String urlStr = ServidorConfig.getUrl(tipo);  // tipo = "motorista" ou "passageiro"
                if (urlStr == null) {
                    Log.e(TAG, "Servidor não detectado.");
                    ((AppCompatActivity) context).runOnUiThread(() ->
                            callback.onError("Servidor não detectado.")
                    );
                    return;
                }

                // Criptografar dados
                String emailCriptografado = Criptografia.criptografar(email);
                String senhaCriptografada = Criptografia.criptografar(senha);
                Log.d(TAG, "Email criptografado: " + emailCriptografado);
                Log.d(TAG, "Senha criptografada: " + senhaCriptografada);

                // Verifica se o email existe
                String rotaVerificacao = tipo.equals("motorista") ? "motorista" : "passageiro";
                String urlVerificacao = ServidorConfig.getUrl(rotaVerificacao + "/existe?email=" + emailCriptografado);
                Log.d(TAG, "Verificando e-mail na URL: " + urlVerificacao);

                HttpURLConnection checkConn = (HttpURLConnection) new URL(urlVerificacao).openConnection();
                checkConn.setRequestMethod("GET");
                int checkCode = checkConn.getResponseCode();
                checkConn.disconnect();

                if (checkCode == 404) {
                    Log.w(TAG, "E-mail não encontrado no servidor.");
                    ((AppCompatActivity) context).runOnUiThread(() ->
                            callback.onError("E-mail não cadastrado.")
                    );
                    return;
                } else {
                    Log.d(TAG, "E-mail verificado com sucesso.");
                }

                // Inicia login
                Log.d(TAG, "Iniciando login em: " + urlStr);
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; utf-8");
                conn.setDoOutput(true);

                JSONObject jsonInput = new JSONObject();
                jsonInput.put("email", emailCriptografado);
                jsonInput.put("senha", senhaCriptografada);

                Log.d(TAG, "Payload enviado: " + jsonInput.toString());

                OutputStream os = conn.getOutputStream();
                os.write(jsonInput.toString().getBytes("utf-8"));
                os.close();

                int code = conn.getResponseCode();
                Log.d(TAG, "Código de resposta do login: " + code);

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader((code == 200) ? conn.getInputStream() : conn.getErrorStream(), "utf-8")
                );

                StringBuilder resposta = new StringBuilder();
                String linha;
                while ((linha = reader.readLine()) != null) {
                    resposta.append(linha.trim());
                }
                reader.close();

                Log.d(TAG, "Resposta do servidor: " + resposta);

                JSONObject jsonResposta = new JSONObject(resposta.toString());

                if (code == 200) {
                    JSONObject usuario = jsonResposta.getJSONObject("usuario");
                    int id = usuario.getInt("id");

                    salvarUsuario(context, id, tipo);

                    Log.d(TAG, "Login bem-sucedido. ID: " + id);
                    ((AppCompatActivity) context).runOnUiThread(() ->
                            callback.onSuccess(id, tipo)
                    );
                } else {
                    String mensagem = jsonResposta.optString("message", "Erro ao fazer login.");
                    Log.e(TAG, "Erro ao fazer login: " + mensagem);
                    ((AppCompatActivity) context).runOnUiThread(() ->
                            callback.onError(mensagem)
                    );
                }

            } catch (Exception e) {
                Log.e(TAG, "Exceção ao conectar com o servidor: ", e);
                ((AppCompatActivity) context).runOnUiThread(() ->
                        callback.onError("Erro ao conectar com o servidor.")
                );
            }
        }).start();
    }

    private static void salvarUsuario(Context context, int id, String tipo) {
        SharedPreferences prefs = context.getSharedPreferences("usuario", Context.MODE_PRIVATE);
        prefs.edit()
                .putInt("id", id)
                .putString("tipo", tipo)
                .apply();
    }

    public static int getUsuarioId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("usuario", Context.MODE_PRIVATE);
        return prefs.getInt("id", -1);
    }

    public static String getTipo(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("usuario", Context.MODE_PRIVATE);
        return prefs.getString("tipo", "");
    }

    public static void autenticar(Context context) {
        if (getUsuarioId(context) == -1 || getTipo(context).isEmpty()) {
            Intent intent = new Intent(context, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
        }
    }

    public static void logout(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("usuario", Context.MODE_PRIVATE);
        prefs.edit().clear().apply();

        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
}
