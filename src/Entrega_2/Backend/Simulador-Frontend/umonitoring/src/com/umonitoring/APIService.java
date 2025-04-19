package com.umonitoring;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class APIService {

    private static final String BASE_URL = "http://localhost:3000";

    // =================== CRUD MOTORISTA ===================

    public static boolean cadastrarMotorista(Motorista m) {
        try {
            JSONObject body = new JSONObject();
            body.put("id", m.getId());
            body.put("nome", m.getNome());
            body.put("sobrenome", m.getSobrenome());
            body.put("telefone", m.getTelefone());
            body.put("email", Criptografia.criptografar(m.getEmail(), 3));
            body.put("senha", Criptografia.criptografar(m.getSenha(), 3));
            body.put("modelo_do_carro", m.getModeloDoCarro());
            body.put("placa_do_carro", m.getPlacaDoCarro());
            body.put("disponibilidade", m.getDisponibilidade());
            body.put("status_protocolo", m.getStatusProtocolo());
            body.put("frase_de_seguranca_1", m.getFrase1());
            body.put("frase_de_seguranca_2", m.getFrase2());
            body.put("frase_de_seguranca_3", m.getFrase3());

            URL url = new URL(BASE_URL + "/motorista");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            os.write(body.toString().getBytes());
            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            conn.disconnect();
            return responseCode == 201 || responseCode == 200;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
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
            while ((linha = in.readLine()) != null)
                resposta.append(linha);
            in.close();

            JSONArray jsonArray = new JSONObject(resposta.toString()).getJSONArray("motoristas");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                lista.add(jsonParaMotorista(json));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static Motorista buscarMotoristaPorId(int id) {
        try {
            URL url = new URL(BASE_URL + "/motorista/" + id);
            HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
            conexao.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
            StringBuilder resposta = new StringBuilder();
            String linha;
            while ((linha = in.readLine()) != null)
                resposta.append(linha);
            in.close();

            JSONObject json = new JSONObject(resposta.toString()).getJSONObject("motoristas");
            return jsonParaMotorista(json);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean atualizarMotorista(Motorista m) {
        try {
            JSONObject body = new JSONObject();
            body.put("nome", m.getNome());
            body.put("sobrenome", m.getSobrenome());
            body.put("telefone", m.getTelefone());
            body.put("email", Criptografia.criptografar(m.getEmail(), 3));
            body.put("senha", Criptografia.criptografar(m.getSenha(), 3));
            body.put("modelo_do_carro", m.getModeloDoCarro());
            body.put("placa_do_carro", m.getPlacaDoCarro());
            body.put("disponibilidade", m.getDisponibilidade());
            body.put("status_protocolo", m.getStatusProtocolo());
            body.put("frase_de_seguranca_1", m.getFrase1());
            body.put("frase_de_seguranca_2", m.getFrase2());
            body.put("frase_de_seguranca_3", m.getFrase3());

            URL url = new URL(BASE_URL + "/motorista/" + m.getId());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            os.write(body.toString().getBytes());
            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            conn.disconnect();
            return responseCode == 200;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deletarMotorista(int id) {
        try {
            URL url = new URL(BASE_URL + "/motorista/" + id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");

            int responseCode = conn.getResponseCode();
            conn.disconnect();
            return responseCode == 200 || responseCode == 204;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // =================== CRUD PASSAGEIRO ===================

    public static boolean cadastrarPassageiro(Passageiro p) {
        try {
            JSONObject body = new JSONObject();
            body.put("id", p.getId());
            body.put("nome", p.getNome());
            body.put("sobrenome", p.getSobrenome());
            body.put("telefone", p.getTelefone());
            body.put("email", Criptografia.criptografar(p.getEmail(), 3));
            body.put("senha", Criptografia.criptografar(p.getSenha(), 3));

            URL url = new URL(BASE_URL + "/passageiro");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            os.write(body.toString().getBytes());
            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            conn.disconnect();
            return responseCode == 201 || responseCode == 200;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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
            while ((linha = in.readLine()) != null)
                resposta.append(linha);
            in.close();

            JSONArray jsonArray = new JSONObject(resposta.toString()).getJSONArray("passageiro");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                lista.add(jsonParaPassageiro(json));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static Passageiro buscarPassageiroPorId(int id) {
        try {
            URL url = new URL(BASE_URL + "/passageiro/" + id);
            HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
            conexao.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
            StringBuilder resposta = new StringBuilder();
            String linha;
            while ((linha = in.readLine()) != null)
                resposta.append(linha);
            in.close();

            JSONObject json = new JSONObject(resposta.toString()).getJSONObject("passageiro");
            return jsonParaPassageiro(json);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean atualizarPassageiro(Passageiro p) {
        try {
            JSONObject body = new JSONObject();
            body.put("nome", p.getNome());
            body.put("sobrenome", p.getSobrenome());
            body.put("telefone", p.getTelefone());
            body.put("email", Criptografia.criptografar(p.getEmail(), 3));
            body.put("senha", Criptografia.criptografar(p.getSenha(), 3));

            URL url = new URL(BASE_URL + "/passageiro/" + p.getId());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            os.write(body.toString().getBytes());
            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            conn.disconnect();
            return responseCode == 200;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deletarPassageiro(int id) {
        try {
            URL url = new URL(BASE_URL + "/passageiro/" + id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");

            int responseCode = conn.getResponseCode();
            conn.disconnect();
            return responseCode == 200 || responseCode == 204;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // =================== CRUD VIAGEM ===================

    public static boolean cadastrarViagem(Viagem v) {
        try {
            JSONObject body = new JSONObject();
            body.put("id", v.getId());
            body.put("endereco_de_partida", v.getEnderecoDePartida());
            body.put("endereco_de_chegada", v.getEnderecoDeChegada());
            body.put("data_hora_de_partida", v.getDataHoraDePartida());
            body.put("data_hora_de_chegada", v.getDataHoraDeChegada());
            body.put("status", v.getStatus());
            body.put("motorista_id", v.getMotorista().getId());
            body.put("passageiro_id", v.getPassageiro().getId());

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
            conn.disconnect();
            return responseCode == 201 || responseCode == 200;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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
            while ((linha = in.readLine()) != null)
                resposta.append(linha);
            in.close();

            JSONArray jsonArray = new JSONObject(resposta.toString()).getJSONArray("viagem");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                lista.add(jsonParaViagem(json));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static Viagem buscarViagemPorId(int id) {
        try {
            URL url = new URL(BASE_URL + "/viagem/" + id);
            HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
            conexao.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
            StringBuilder resposta = new StringBuilder();
            String linha;
            while ((linha = in.readLine()) != null)
                resposta.append(linha);
            in.close();

            JSONObject json = new JSONObject(resposta.toString()).getJSONObject("viagem");
            return jsonParaViagem(json);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean atualizarViagem(Viagem v) {
        try {
            JSONObject body = new JSONObject();
            body.put("endereco_de_partida", v.getEnderecoDePartida());
            body.put("endereco_de_chegada", v.getEnderecoDeChegada());
            body.put("data_hora_de_partida", v.getDataHoraDePartida());
            body.put("data_hora_de_chegada", v.getDataHoraDeChegada());
            body.put("status", v.getStatus());
            body.put("motorista_id", v.getMotorista().getId());
            body.put("passageiro_id", v.getPassageiro().getId());

            URL url = new URL(BASE_URL + "/viagem/" + v.getId());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            os.write(body.toString().getBytes());
            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            conn.disconnect();
            return responseCode == 200;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deletarViagem(int id) {
        try {
            URL url = new URL(BASE_URL + "/viagem/" + id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");

            int responseCode = conn.getResponseCode();
            conn.disconnect();
            return responseCode == 200 || responseCode == 204;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // =================== HELPERS ===================

    private static Motorista jsonParaMotorista(JSONObject json) {
        return new Motorista(
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
                json.getString("frase_de_seguranca_3"));
    }

    private static Passageiro jsonParaPassageiro(JSONObject json) {
        return new Passageiro(
                json.getInt("id"),
                json.getString("nome"),
                json.getString("sobrenome"),
                json.getString("telefone"),
                Criptografia.descriptografar(json.getString("email"), 3),
                Criptografia.descriptografar(json.getString("senha"), 3));
    }

    private static Viagem jsonParaViagem(JSONObject json) {
        return new Viagem(
                json.getInt("id"),
                json.getString("endereco_de_partida"),
                json.getString("endereco_de_chegada"),
                json.getString("data_hora_de_partida"),
                json.getString("data_hora_de_chegada"),
                json.getString("status"),
                buscarMotoristaPorId(json.getInt("motorista_id")),
                buscarPassageiroPorId(json.getInt("passageiro_id")));
    }

    
}