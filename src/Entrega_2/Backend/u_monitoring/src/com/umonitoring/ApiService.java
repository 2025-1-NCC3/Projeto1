package com.umonitoring;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class ApiService {

    private static final String BASE_URL = "https://9dc78641-1bd1-45c8-90c2-5ef78eb75b05-00-1liaz9tknfzel.spock.replit.dev";

    public static Motorista buscarMotoristaPorId(int id) {
        try {
            String endpoint = BASE_URL + "/motorista/" + id;
            JSONObject json = requisitarObjeto(endpoint, "motoristas");

            if (json == null) return null;

            String email = Criptografia.descriptografar(json.getString("email"), 3);
            String senha = Criptografia.descriptografar(json.getString("senha"), 3);

            return new Motorista(
                json.getInt("id"),
                json.getString("nome"),
                json.getString("sobrenome"),
                json.getString("telefone"),
                email,
                senha,
                json.getString("modelo_do_carro"),
                json.getString("placa_do_carro"),
                json.optString("disponibilidade", "Indefinido"),
                json.optString("status_protocolo", "Desconhecido"),
                json.getString("frase_de_seguranca_1"),
                json.getString("frase_de_seguranca_2"),
                json.getString("frase_de_seguranca_3")
            );

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Passageiro buscarPassageiroPorId(int id) {
        try {
            String endpoint = BASE_URL + "/passageiro/" + id;
            JSONObject json = requisitarObjeto(endpoint, "passageiro");

            if (json == null) return null;

            String email = Criptografia.descriptografar(json.getString("email"), 3);
            String senha = Criptografia.descriptografar(json.getString("senha"), 3);

            return new Passageiro(
                json.getInt("id"),
                json.getString("nome"),
                json.getString("sobrenome"),
                json.getString("telefone"),
                email,
                senha
            );

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Viagem buscarViagemPorId(int id) {
        try {
            String endpoint = BASE_URL + "/viagem/" + id;
            JSONObject json = requisitarObjeto(endpoint, "viagem");

            if (json == null) return null;

            return new Viagem(
                json.getInt("id"),
                json.getString("endereco_de_partida"),
                json.getString("endereco_de_chegada"),
                json.getString("data_hora_de_partida"),
                json.getString("data_hora_de_chegada"),
                json.getString("status"),
                null, // Motorista (pode buscar via ID se quiser)
                null  // Passageiro (idem)
            );

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private static JSONObject requisitarObjeto(String endpoint, String chave) {
        try {
            URL url = new URL(endpoint);
            HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
            conexao.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
            StringBuilder resposta = new StringBuilder();
            String linha;
            while ((linha = in.readLine()) != null) {
                resposta.append(linha);
            }
            in.close();

            return new JSONObject(resposta.toString()).getJSONObject(chave);

        } catch (Exception e) {
            System.err.println("❌ Erro ao requisitar: " + endpoint);
            e.printStackTrace();
            return null;
        }
    }

    public static List<Motorista> buscarTodosMotoristas() {
    List<Motorista> lista = new ArrayList<>();
    try {
        URL url = new URL(BASE_URL + "/motorista");
        HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
        conexao.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
        StringBuilder resposta = new StringBuilder();
        String linha;
        while ((linha = in.readLine()) != null) {
            resposta.append(linha);
        }
        in.close();

        JSONArray jsonArray = new JSONObject(resposta.toString()).getJSONArray("motoristas");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject json = jsonArray.getJSONObject(i);
            Motorista m = new Motorista(
                json.getInt("id"),
                json.getString("nome"),
                json.getString("sobrenome"),
                json.getString("telefone"),
                Criptografia.descriptografar(json.getString("email"), 3),
                Criptografia.descriptografar(json.getString("senha"), 3),
                json.getString("modelo_do_carro"),
                json.getString("placa_do_carro"),
                json.optString("disponibilidade", "Indefinido"),
                json.optString("status_protocolo", "Desconhecido"),
                json.getString("frase_de_seguranca_1"),
                json.getString("frase_de_seguranca_2"),
                json.getString("frase_de_seguranca_3")
            );
            lista.add(m);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
    return lista;
}

public static List<Passageiro> buscarTodosPassageiros() {
    List<Passageiro> lista = new ArrayList<>();
    try {
        URL url = new URL(BASE_URL + "/passageiro");
        HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
        conexao.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
        StringBuilder resposta = new StringBuilder();
        String linha;
        while ((linha = in.readLine()) != null) {
            resposta.append(linha);
        }
        in.close();

        JSONArray jsonArray = new JSONObject(resposta.toString()).getJSONArray("passageiro");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject json = jsonArray.getJSONObject(i);
            Passageiro p = new Passageiro(
                json.getInt("id"),
                json.getString("nome"),
                json.getString("sobrenome"),
                json.getString("telefone"),
                Criptografia.descriptografar(json.getString("email"), 3),
                Criptografia.descriptografar(json.getString("senha"), 3)
            );
            lista.add(p);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
    return lista;
}

public static List<Viagem> buscarTodasViagens() {
    List<Viagem> lista = new ArrayList<>();
    try {
        URL url = new URL(BASE_URL + "/viagem");
        HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
        conexao.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
        StringBuilder resposta = new StringBuilder();
        String linha;
        while ((linha = in.readLine()) != null) {
            resposta.append(linha);
        }
        in.close();

        JSONArray jsonArray = new JSONObject(resposta.toString()).getJSONArray("viagem");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject json = jsonArray.getJSONObject(i);

            // IDs
            int idMotorista = json.getInt("motorista_id");
            int idPassageiro = json.getInt("passageiro_id");

            // Requisições individuais
            Motorista motorista = buscarMotoristaPorId(idMotorista);
            Passageiro passageiro = buscarPassageiroPorId(idPassageiro);

            Viagem v = new Viagem(
                json.getInt("id"),
                json.getString("endereco_de_partida"),
                json.getString("endereco_de_chegada"),
                json.getString("data_hora_de_partida"),
                json.getString("data_hora_de_chegada"),
                json.getString("status"),
                motorista,
                passageiro
            );
            lista.add(v);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
    return lista;
}

public static void cadastrarViagensAutomaticamente() {
    List<Motorista> motoristas = buscarTodosMotoristas();
    List<Passageiro> passageiros = buscarTodosPassageiros();

    int total = Math.min(motoristas.size(), passageiros.size());

    for (int i = 0; i < total; i++) {
        Motorista m = motoristas.get(i);
        Passageiro p = passageiros.get(i);

        JSONObject body = new JSONObject();
        body.put("endereco_de_partida", "Endereço " + (i + 1) + "A");
        body.put("endereco_de_chegada", "Endereço " + (i + 1) + "B");
        body.put("data_hora_de_partida", "2025-04-18 14:0" + i);
        body.put("data_hora_de_chegada", "2025-04-18 14:2" + i);
        body.put("status", "Finalizada");
        body.put("motorista_id", m.getId());
        body.put("passageiro_id", p.getId());

        try {
            URL url = new URL(BASE_URL + "/viagem");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            os.write(body.toString().getBytes());
            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            if (responseCode == 201) {
                System.out.println("✅ Viagem cadastrada entre " + m.getNome() + " e " + p.getNome());
            } else {
                System.out.println("❌ Falha ao cadastrar viagem. Código HTTP: " + responseCode);
            }

            conn.disconnect();
        } catch (Exception e) {
            System.out.println("❌ Erro ao cadastrar viagem automática");
            e.printStackTrace();
        }
    }
}


}
