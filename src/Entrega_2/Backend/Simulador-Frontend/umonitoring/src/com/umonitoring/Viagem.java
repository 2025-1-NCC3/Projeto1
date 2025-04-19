package com.umonitoring;

public class Viagem {
    private int id;
    private String enderecoDePartida;
    private String enderecoDeChegada;
    private String dataHoraDePartida;
    private String dataHoraDeChegada;
    private String status;
    private Motorista motorista;
    private Passageiro passageiro;

    public Viagem(int id, String enderecoDePartida, String enderecoDeChegada, String dataHoraDePartida, String dataHoraDeChegada, String status,Motorista motorista, Passageiro passageiro) {
        this.id = id;
        this.enderecoDePartida = enderecoDePartida;
        this.enderecoDeChegada = enderecoDeChegada;
        this.dataHoraDePartida = dataHoraDePartida;
        this.dataHoraDeChegada = dataHoraDeChegada;
        this.status = status;
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
        return enderecoDePartida;
    }
    public void setEnderecoDePartida(String endPartida) {
        this.enderecoDePartida = endPartida;
    }

    public String getEnderecoDeChegada() {
        return enderecoDeChegada;
    }
    public void setEnderecoDeChegada(String endChegada) {
        this.enderecoDeChegada = endChegada;
    }

    public String getDataHoraDePartida() {
        return dataHoraDePartida;
    }
    public void setDataHoraDePartida(String horaPartida) {
        this.dataHoraDePartida = horaPartida;
    }

    public String getDataHoraDeChegada() {
        return dataHoraDeChegada;
    }
    public void setDataHoraDeChegada(String horaChegada) {
        this.dataHoraDeChegada = horaChegada;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String s) {
        this.status = s;
    }

    public Motorista getMotorista() {
        return motorista;
    }
    public void setMotorista(Motorista mot) {
        this.motorista = mot;
    }

    public Passageiro getPassageiro() {
        return passageiro;
    }
    public void setPassageiro(Passageiro pas) {
        this.passageiro = pas;
    }
}
