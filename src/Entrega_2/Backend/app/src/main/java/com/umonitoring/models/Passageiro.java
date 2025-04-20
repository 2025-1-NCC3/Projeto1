package com.umonitoring.models;

import com.umonitoring.utils.Criptografia;
import java.util.List;

import com.umonitoring.api.PassageiroAPI;
import org.json.JSONObject;


public class Passageiro extends Usuario {
    public Passageiro(int id, String nome, String sobrenome, String telefone, String email, String senha) {
        super(id, nome, sobrenome, telefone, email, senha);
    }

    public void criarPassageiro() {
    }

    public List<Passageiro> listarPassageiros() {
        return null;
    }

    public Passageiro buscarPassageiroPorId(int id) {
        return null;
    }

    public void atualizarPassageiro(int id, Passageiro passageiro) {
    }

    public void deletarPassageiro(int id) {
    }

    public String salvar() {
        try {
            JSONObject json = montarJson();
            return PassageiroAPI.cadastrar(json);
        } catch (Exception e) {
            return "Erro ao salvar passageiro: " + e.getMessage();
        }
    }

    public String atualizar(int id) {
        try {
            JSONObject json = montarJson();
            return PassageiroAPI.atualizar(id, json);
        } catch (Exception e) {
            return "Erro ao atualizar passageiro: " + e.getMessage();
        }
    }

    public String remover(int id) {
        try {
            return PassageiroAPI.deletar(id);
        } catch (Exception e) {
            return "Erro ao remover passageiro: " + e.getMessage();
        }
    }

    private JSONObject montarJson() throws Exception {
        JSONObject json = new JSONObject();
        json.put("nome", getNome());
        json.put("sobrenome", getSobrenome());
        json.put("telefone", getTelefone());
        json.put("email", getEmail());
        json.put("senha", getSenha());
        return json;
    }
}
