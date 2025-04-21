
package com.umonitoring.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

public class PassageiroAPI {

    public static String listarTodos() {
        StringBuilder resultado = new StringBuilder();

        try {
            String baseUrl = ServidorConfig.getUrl("passageiro");
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
            return "Erro: " + e.getMessage();
        }

        return resultado.toString();
    }

    public static String buscarPorId(int id) {
        StringBuilder resultado = new StringBuilder();

        try {
            String baseUrl = ServidorConfig.getUrl("passageiro");
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
            return "Erro: " + e.getMessage();
        }

        return resultado.toString();
    }

    public static String cadastrar(JSONObject passageiro) {
        StringBuilder resultado = new StringBuilder();

        try {
            String baseUrl = ServidorConfig.getUrl("passageiro");
            if (baseUrl == null) return "Erro: Servidor não detectado.";

            URL url = new URL(baseUrl);
            HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
            conexao.setRequestMethod("POST");
            conexao.setRequestProperty("Content-Type", "application/json; utf-8");
            conexao.setDoOutput(true);

            OutputStream os = conexao.getOutputStream();
            os.write(passageiro.toString().getBytes("UTF-8"));
            os.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
            String linha;
            while ((linha = reader.readLine()) != null) {
                resultado.append(linha);
            }
            reader.close();
        } catch (Exception e) {
            return "Erro: " + e.getMessage();
        }

        return resultado.toString();
    }

    public static String atualizar(int id, JSONObject passageiro) {
        StringBuilder resultado = new StringBuilder();

        try {
            String baseUrl = ServidorConfig.getUrl("passageiro");
            if (baseUrl == null) return "Erro: Servidor não detectado.";

            URL url = new URL(baseUrl + "/" + id);
            HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
            conexao.setRequestMethod("PUT");
            conexao.setRequestProperty("Content-Type", "application/json; utf-8");
            conexao.setDoOutput(true);

            OutputStream os = conexao.getOutputStream();
            os.write(passageiro.toString().getBytes("UTF-8"));
            os.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
            String linha;
            while ((linha = reader.readLine()) != null) {
                resultado.append(linha);
            }
            reader.close();
        } catch (Exception e) {
            return "Erro: " + e.getMessage();
        }

        return resultado.toString();
    }

    public static String deletar(int id) {
        StringBuilder resultado = new StringBuilder();

        try {
            String baseUrl = ServidorConfig.getUrl("passageiro");
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
            return "Erro: " + e.getMessage();
        }

        return resultado.toString();
    }
}
