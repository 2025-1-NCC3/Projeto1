package com.umonitoring.models;

import com.umonitoring.utils.Criptografia;
import java.util.List;

import com.umonitoring.api.ViagemAPI;
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
        this.status = Criptografia.criptografar(status);
        this.motorista = motorista;
        this.passageiro = passageiro;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEnderecoDePartida() {
        return Criptografia.descriptografar(enderecoDePartida);
    }

    public void setEnderecoDePartida(String endereco) {
        this.enderecoDePartida = Criptografia.criptografar(endereco);
    }

    public String getEnderecoDeChegada() {
        return Criptografia.descriptografar(enderecoDeChegada);
    }

    public void setEnderecoDeChegada(String endereco) {
        this.enderecoDeChegada = Criptografia.criptografar(endereco);
    }

    public String getDataHoraDePartida() {
        return Criptografia.descriptografar(dataHoraDePartida);
    }

    public void setDataHoraDePartida(String dataHora) {
        this.dataHoraDePartida = Criptografia.criptografar(dataHora);
    }

    public String getDataHoraDeChegada() {
        return Criptografia.descriptografar(dataHoraDeChegada);
    }

    public void setDataHoraDeChegada(String dataHora) {
        this.dataHoraDeChegada = Criptografia.criptografar(dataHora);
    }

    public String getStatus() {
        return Criptografia.descriptografar(status);
    }

    public void setStatus(String status) {
        this.status = Criptografia.criptografar(status);
    }

    public Motorista getMotorista() {
        return motorista;
    }

    public void setMotorista(Motorista motorista) {
        this.motorista = motorista;
    }

    public Passageiro getPassageiro() {
        return passageiro;
    }

    public void setPassageiro(Passageiro passageiro) {
        this.passageiro = passageiro;
    }

    public void criarViagem() {
    }

    public List<Viagem> listarViagensPorMotorista(Motorista motorista) {
        return null;
    }

    public List<Viagem> listarViagensPorPassageiro(Passageiro passageiro) {
        return null;
    }

    public Viagem buscarViagemPorId(int id) {
        return null;
    }

    public void atualizarViagem(int id, Viagem viagem) {
    }

    public void deletarViagem(int id) {
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

    private JSONObject montarJson() throws Exception {
        JSONObject json = new JSONObject();
        json.put("endereco_de_partida", getEnderecoDePartida());
        json.put("endereco_de_chegada", getEnderecoDeChegada());
        json.put("data_hora_de_partida", getDataHoraDePartida());
        json.put("data_hora_de_chegada", getDataHoraDeChegada());
        json.put("status", getStatus());
        json.put("motorista_id", getMotorista().getId());
        json.put("passageiro_id", getPassageiro().getId());
        return json;
    }
}
