package com.umonitoring;

public class Motorista extends Usuario {
    private String modeloDoCarro;
    private String placaDoCarro;
    private String disponibilidade;
    private String statusProtocolo;
    private String frase1;
    private String frase2;
    private String frase3;

    public Motorista(int id, String nome, String sobrenome, String telefone, String email, String senha, String modeloDoCarro, String placaDoCarro, String disponibilidade, String statusProtocolo, String frase1, String frase2, String frase3) {
        super(id, nome, sobrenome, telefone, email, senha);
        this.modeloDoCarro = modeloDoCarro;
        this.placaDoCarro = placaDoCarro;
        this.disponibilidade = disponibilidade;
        this.statusProtocolo = statusProtocolo;
        this.frase1 = frase1;
        this.frase2 = frase2;
        this.frase3 = frase3;
    }
    public String getModeloDoCarro() {
        return modeloDoCarro;
    }
    public void setModeloDoCarro(String modelCar) {
        this.modeloDoCarro = modelCar;
    }
    public String getPlacaDoCarro() {
        return placaDoCarro;
    }
    public void setPlacaDoCarro(String placaCar) {
        this.placaDoCarro = placaCar;
    }

    public String getDisponibilidade() {
        return disponibilidade;
    }
    public void setDisponibilidade(String disp) {
        this.disponibilidade = disp;
    }

    public String getStatusProtocolo() {
        return statusProtocolo;
    }
    public void setStatusProtocolo(String sProtocolo) {
        this.statusProtocolo = sProtocolo;
    }

    public String getFrase1() {
        return frase1;
    }
    public void setFrase1(String f1) {
        this.frase1 = f1;
    }

    public String getFrase2() {
        return frase2;
    }
    public void setFrase2(String f2) {
        this.frase2 = f2;
    }

    public String getFrase3() {
        return frase3;
    }
    public void setFrase3(String f3) {
        this.frase3 = f3;
    }

    public void deletarConta(int id) {
        System.out.println("Conta do motorista com ID " + id + " deletada.");
    }
}
