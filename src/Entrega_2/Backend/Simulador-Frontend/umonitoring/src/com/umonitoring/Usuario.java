package com.umonitoring;

public class Usuario {
    private int id;
    private String nome;
    private String sobrenome;
    private String telefone;
    private String email;
    private String senha;

    public Usuario(int id, String nome, String sobrenome, String telefone, String email, String senha) {
        this.id = id;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.telefone = telefone;
        this.email = email;
        this.senha = senha;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String n) {
        this.nome = n;
    }

    public String getSobrenome() {
        return sobrenome;
    }
    public void setSobrenome(String s) {
        this.sobrenome = s;
    }

    public String getTelefone() {
        return telefone;
    }
    public void setTelefone(String tel) {
        this.telefone = tel;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String mail) {
        this.email = mail;
    }

    public String getSenha() {
        return senha;
    }
    public void setSenha(String s) {
        this.senha = s;
    }
}
