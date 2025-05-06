package com.umonitoring.models;

import android.util.Log;

import com.umonitoring.utils.Criptografia;
import java.util.ArrayList;
import java.util.List;
import com.umonitoring.api.ViagemAPI;
import org.json.JSONArray;
import org.json.JSONObject;

public class Viagem {
    private int id;
    private String enderecoDePartida;
    private String enderecoDeChegada;
    private String dataHoraDePartida;
    private String dataHoraDeChegada;
    private String status;
    private Motorista motorista;
    private Passageiro passageiro;

    public Viagem(int id, String enderecoDePartida, String enderecoDeChegada, String dataHoraDePartida, String dataHoraDeChegada, String status, Motorista motorista, Passageiro passageiro) {
        this.id = id;
        this.enderecoDePartida = Criptografia.criptografar(enderecoDePartida);
        this.enderecoDeChegada = Criptografia.criptografar(enderecoDeChegada);
        this.dataHoraDePartida = Criptografia.criptografar(dataHoraDePartida);
        this.dataHoraDeChegada = Criptografia.criptografar(dataHoraDeChegada);
        this.status = status;
        this.motorista = motorista;
        this.passageiro = passageiro;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getEnderecoDePartida() { return Criptografia.descriptografar(enderecoDePartida); }
    public void setEnderecoDePartida(String endereco) { this.enderecoDePartida = Criptografia.criptografar(endereco); }

    public String getEnderecoDeChegada() { return Criptografia.descriptografar(enderecoDeChegada); }
    public void setEnderecoDeChegada(String endereco) { this.enderecoDeChegada = Criptografia.criptografar(endereco); }

    public String getDataHoraDePartida() { return Criptografia.descriptografar(dataHoraDePartida); }
    public void setDataHoraDePartida(String dataHora) { this.dataHoraDePartida = Criptografia.criptografar(dataHora); }

    public String getDataHoraDeChegada() { return Criptografia.descriptografar(dataHoraDeChegada); }
    public void setDataHoraDeChegada(String dataHora) { this.dataHoraDeChegada = Criptografia.criptografar(dataHora); }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Motorista getMotorista() { return motorista; }
    public void setMotorista(Motorista motorista) { this.motorista = motorista; }

    public Passageiro getPassageiro() { return passageiro; }
    public void setPassageiro(Passageiro passageiro) { this.passageiro = passageiro; }

    public void criarViagem() {
        agendar();
    }

    public static List<Viagem> listarTodasAsViagens() {
        String resposta = ViagemAPI.listarTodas();
        List<Viagem> lista = new ArrayList<>();

        try {
            JSONObject obj = new JSONObject(resposta);
            if (obj.has("viagens")) {
                JSONArray array = obj.getJSONArray("viagens");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject json = array.getJSONObject(i);
                    Viagem v = construirViagem(json);
                    lista.add(v);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public static List<Viagem> listarViagensDisponiveis() {
        String resposta = ViagemAPI.listarDisponiveis();
        Log.d("VIAGEM_DISPONIVEIS", "Resposta: " + resposta); // debug

        List<Viagem> lista = new ArrayList<>();

        try {
            JSONObject obj = new JSONObject(resposta);
            if (obj.has("viagens")) {
                JSONArray array = obj.getJSONArray("viagens");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject json = array.getJSONObject(i);
                    Viagem v = construirViagem(json);
                    lista.add(v);
                }
            } else {
                Log.e("VIAGEM_DISPONIVEIS", "Chave 'viagens' não encontrada no JSON.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("VIAGEM_DISPONIVEIS", "Erro ao processar JSON: " + e.getMessage());
        }

        return lista;
    }



    public static List<Viagem> listarViagensPorMotorista(Motorista motorista) {
        String resposta = ViagemAPI.listarPorMotorista(motorista.getId());
        List<Viagem> lista = new ArrayList<>();

        try {
            JSONArray array = new JSONArray(resposta);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                Viagem v = construirViagem(obj);
                lista.add(v);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public static List<Viagem> listarViagensPorPassageiro(Passageiro passageiro) {
        String resposta = ViagemAPI.listarPorPassageiro(passageiro.getId());
        List<Viagem> lista = new ArrayList<>();

        try {
            JSONArray array = new JSONArray(resposta);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                Viagem v = construirViagem(obj);
                lista.add(v);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public static Viagem buscarViagemPorId(int id) {
        String resposta = ViagemAPI.buscarPorId(id);

        try {
            JSONObject obj = new JSONObject(resposta);
            return construirViagem(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void atualizarViagem(int id, Viagem viagem) {
        viagem.atualizar(id);
    }

    public void deletarViagem(int id) {
        cancelar(id);
    }

    public String agendar() {
        try {
            JSONObject json = montarJson();
            return ViagemAPI.cadastrar(json);
        } catch (Exception e) {
            return "Erro ao agendar viagem: " + e.getMessage();
        }
    }

    public String atualizar(int id) {
        try {
            JSONObject json = montarJson();
            return ViagemAPI.atualizar(id, json);
        } catch (Exception e) {
            return "Erro ao atualizar viagem: " + e.getMessage();
        }
    }

    public String cancelar(int id) {
        try {
            return ViagemAPI.deletar(id);
        } catch (Exception e) {
            return "Erro ao cancelar viagem: " + e.getMessage();
        }
    }

    private static Viagem construirViagem(JSONObject obj) throws Exception {
        Motorista motorista = null;
        Passageiro passageiro = Passageiro.buscarPassageiroPorId(obj.getInt("passageiro_id"));

        if (!obj.isNull("motorista_id")) {
            motorista = Motorista.buscarMotoristaPorId(obj.getInt("motorista_id"));
        }

        return new Viagem(
                obj.getInt("id"),
                Criptografia.descriptografar(obj.getString("endereco_de_partida")),
                Criptografia.descriptografar(obj.getString("endereco_de_chegada")),
                Criptografia.descriptografar(obj.optString("data_hora_de_partida", "")),
                Criptografia.descriptografar(obj.optString("data_hora_de_chegada", "")),
                obj.getString("status"),
                motorista,
                passageiro
        );
    }


    private JSONObject montarJson() throws Exception {
        JSONObject json = new JSONObject();
        json.put("endereco_de_partida", enderecoDePartida);
        json.put("endereco_de_chegada", enderecoDeChegada);
        json.put("data_hora_de_partida", dataHoraDePartida);
        json.put("data_hora_de_chegada", dataHoraDeChegada);
        json.put("status", status);
        json.put("motorista_id", motorista.getId());
        json.put("passageiro_id", passageiro.getId());
        return json;
    }
}