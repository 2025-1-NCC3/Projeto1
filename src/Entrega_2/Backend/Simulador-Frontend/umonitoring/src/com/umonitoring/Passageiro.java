package com.umonitoring;

public class Passageiro extends Usuario {

    public Passageiro(int id, String nome, String sobrenome, String telefone, String email, String senha) {
        super(id, nome, sobrenome, telefone, email, senha);
    }

    public void criarPassageiro() {
        System.out.println("Passageiro criado.");
    }

    public void deletarConta(int id) {
        System.out.println("Conta do passageiro com ID " + id + " deletada.");
    }
}
