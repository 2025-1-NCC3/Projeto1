package com.umonitoring.api;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.json.JSONObject;

public class MotoristaAPI {

    // GET: lista todos os motoristas
    public static String listarTodos() {
        StringBuilder resultado = new StringBuilder();

        try {
            String baseUrl = ServidorConfig.getUrl("motorista");
            Log.d("DEBUG_LISTAR", "URL usada: " + baseUrl);
            if (baseUrl == null) return "Erro: Servidor não detectado.";

            URL url = new URL(baseUrl);
            HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
            conexao.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
            String linha;
            while ((linha = reader.readLine()) != null) {
                resultado.append(linha);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ERRO_API", "Erro na requisição", e);
            return "Erro: " + e.getClass().getSimpleName() + " - " + e.getMessage();
        }

        Log.d("DEBUG_LISTAR", "Resposta recebida: " + resultado.toString());
        return resultado.toString();
    }



    // GET: busca um motorista por ID
    public static String buscarPorId(int id) {
        StringBuilder resultado = new StringBuilder();

        try {
            String baseUrl = ServidorConfig.getUrl("motorista");
            if (baseUrl == null) return "Erro: Servidor não detectado.";

            URL url = new URL(baseUrl + "/" + id);
            HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
            conexao.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
            String linha;

            while ((linha = reader.readLine()) != null) {
                resultado.append(linha);
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro: " + e.getMessage();
        }

        return resultado.toString();
    }

    // POST: cadastra um novo motorista
    public static String cadastrar(JSONObject motorista) {
        StringBuilder resultado = new StringBuilder();

        try {
            String baseUrl = ServidorConfig.getUrl("motorista");
            if (baseUrl == null) return "Erro: Servidor não detectado.";

            URL url = new URL(baseUrl);
            HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
            conexao.setRequestMethod("POST");
            conexao.setRequestProperty("Content-Type", "application/json; utf-8");
            conexao.setDoOutput(true);

            OutputStream os = conexao.getOutputStream();
            os.write(motorista.toString().getBytes("UTF-8"));
            os.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
            String linha;

            while ((linha = reader.readLine()) != null) {
                resultado.append(linha);
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro: " + e.getMessage();
        }

        return resultado.toString();
    }

    // PUT: atualiza dados de um motorista
    public static String atualizar(int id, JSONObject motorista) {
        StringBuilder resultado = new StringBuilder();

        try {
            String baseUrl = ServidorConfig.getUrl("motorista"); // ← volta pra "motorista"
            if (baseUrl == null) return "Erro: Servidor não detectado.";

            URL url = new URL(baseUrl + "/" + id);
            HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
            conexao.setRequestMethod("PUT"); // ← volta pra PUT (agora que está rodando local)
            conexao.setRequestProperty("Content-Type", "application/json; charset=UTF-8"); // ← charset aqui
            conexao.setRequestProperty("Accept", "application/json");
            conexao.setDoOutput(true);

            // DEBUG: JSON enviado
            Log.d("DEBUG_ENVIO", "JSON enviado: " + motorista.toString());

            byte[] jsonBytes = motorista.toString().getBytes(StandardCharsets.UTF_8); // ← UTF-8 seguro
            conexao.setRequestProperty("Content-Length", String.valueOf(jsonBytes.length));

            OutputStream os = conexao.getOutputStream();
            os.write(jsonBytes);
            os.flush();
            os.close();

            int status = conexao.getResponseCode();

            BufferedReader reader;
            if (status >= 200 && status < 300) {
                reader = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
            } else {
                reader = new BufferedReader(new InputStreamReader(conexao.getErrorStream()));
            }

            String linha;
            while ((linha = reader.readLine()) != null) {
                resultado.append(linha);
            }

            reader.close();
            return "Status: " + status + " - " + resultado.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "Erro: " + e.getMessage();
        }
    }





    // DELETE: remove motorista por ID
    public static String deletar(int id) {
        StringBuilder resultado = new StringBuilder();

        try {
            String baseUrl = ServidorConfig.getUrl("motorista");
            if (baseUrl == null) return "Erro: Servidor não detectado.";

            URL url = new URL(baseUrl + "/" + id);
            HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
            conexao.setRequestMethod("DELETE");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
            String linha;

            while ((linha = reader.readLine()) != null) {
                resultado.append(linha);
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro: " + e.getMessage();
        }

        return resultado.toString();
    }
}
