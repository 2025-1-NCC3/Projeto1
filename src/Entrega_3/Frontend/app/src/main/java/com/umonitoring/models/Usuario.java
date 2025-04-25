package com.umonitoring.models;

import com.umonitoring.utils.Criptografia;

public class Usuario {
    private int id;
    private String nome;
    private String sobrenome;
    private String telefone;
    private String email;
    private String senha;

    public Usuario(int id, String nome, String sobrenome, String telefone, String email, String senha) {
        this.id = id;
        this.nome = Criptografia.criptografar(nome);
        this.sobrenome = Criptografia.criptografar(sobrenome);
        this.telefone = Criptografia.criptografar(telefone);
        this.email = Criptografia.criptografar(email);
        this.senha = Criptografia.criptografar(senha);
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return Criptografia.descriptografar(nome);
    }
    public void setNome(String nome) {
        this.nome = Criptografia.criptografar(nome);
    }

    public String getSobrenome() {
        return Criptografia.descriptografar(sobrenome);
    }
    public void setSobrenome(String sobrenome) {
        this.sobrenome = Criptografia.criptografar(sobrenome);
    }

    public String getTelefone() {
        return Criptografia.descriptografar(telefone);
    }
    public void setTelefone(String telefone) {
        this.telefone = Criptografia.criptografar(telefone);
    }

    public String getEmail() {
        return Criptografia.descriptografar(email);
    }
    public void setEmail(String email) {
        this.email = Criptografia.criptografar(email);
    }

    public String getSenha() {
        return Criptografia.descriptografar(senha);
    }
    public void setSenha(String senha) {
        this.senha = Criptografia.criptografar(senha);
    }
}
