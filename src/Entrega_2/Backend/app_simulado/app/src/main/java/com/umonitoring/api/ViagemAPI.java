
package com.umonitoring.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

public class ViagemAPI {

    public static String listarTodas() {
        return requisicaoGET("viagem");
    }

    public static String buscarPorId(int id) {
        return requisicaoGET("viagem/" + id);
    }

    public static String listarPorPassageiro(int id) {
        return requisicaoGET("viagem/passageiro/" + id);
    }

    public static String listarPorMotorista(int id) {
        return requisicaoGET("viagem/motorista/" + id);
    }

    public static String cadastrar(JSONObject viagem) {
        return requisicaoPOST_PUT("POST", "viagem", viagem);
    }

    public static String atualizar(int id, JSONObject viagem) {
        return requisicaoPOST_PUT("PUT", "viagem/" + id, viagem);
    }

    public static String deletar(int id) {
        StringBuilder resultado = new StringBuilder();
        try {
            String urlStr = ServidorConfig.getUrl("viagem/" + id);
            if (urlStr == null) return "Erro: Servidor não detectado.";

            URL url = new URL(urlStr);
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

    // Métodos auxiliares
    private static String requisicaoGET(String rota) {
        StringBuilder resultado = new StringBuilder();
        try {
            String urlStr = ServidorConfig.getUrl(rota);
            if (urlStr == null) return "Erro: Servidor não detectado.";

            URL url = new URL(urlStr);
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

    private static String requisicaoPOST_PUT(String metodo, String rota, JSONObject jsonBody) {
        StringBuilder resultado = new StringBuilder();
        try {
            String urlStr = ServidorConfig.getUrl(rota);
            if (urlStr == null) return "Erro: Servidor não detectado.";

            URL url = new URL(urlStr);
            HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
            conexao.setRequestMethod(metodo);
            conexao.setRequestProperty("Content-Type", "application/json; utf-8");
            conexao.setDoOutput(true);

            OutputStream os = conexao.getOutputStream();
            os.write(jsonBody.toString().getBytes("UTF-8"));
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
}
