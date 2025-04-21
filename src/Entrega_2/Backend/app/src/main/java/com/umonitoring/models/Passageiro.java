package com.umonitoring.models;

import com.umonitoring.utils.Criptografia;

import java.util.ArrayList;
import java.util.List;

import com.umonitoring.api.PassageiroAPI;

import org.json.JSONArray;
import org.json.JSONObject;


public class Passageiro extends Usuario {
    public Passageiro(int id, String nome, String sobrenome, String telefone, String email, String senha) {
        super(id, nome, sobrenome, telefone, email, senha);
    }

    public void criarPassageiro() {
        salvar();
    }

    public static Passageiro buscarPassageiroPorId(int id) {
        String resposta = PassageiroAPI.buscarPorId(id);

        try {
            JSONObject obj = new JSONObject(resposta);
            JSONObject json = obj.getJSONObject("passageiro");

            return new Passageiro(
                    json.getInt("id"),
                    Criptografia.descriptografar(json.getString("nome")),
                    Criptografia.descriptografar(json.getString("sobrenome")),
                    Criptografia.descriptografar(json.getString("telefone")),
                    Criptografia.descriptografar(json.getString("email")),
                    Criptografia.descriptografar(json.getString("senha"))
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }




    public List<Passageiro> listarPassageiros() {
        String resposta = PassageiroAPI.listarTodos();
        List<Passageiro> lista = new ArrayList<>();

        try {
            JSONObject obj = new JSONObject(resposta);
            JSONArray array = obj.getJSONArray("passageiro");

            for (int i = 0; i < array.length(); i++) {
                JSONObject json = array.getJSONObject(i);

                Passageiro p = new Passageiro(
                        json.getInt("id"),
                        Criptografia.descriptografar(json.getString("nome")),
                        Criptografia.descriptografar(json.getString("sobrenome")),
                        Criptografia.descriptografar(json.getString("telefone")),
                        Criptografia.descriptografar(json.getString("email")),
                        Criptografia.descriptografar(json.getString("senha"))
                );

                lista.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }




    public void atualizarPassageiro(int id, Passageiro passageiro) {
        passageiro.atualizar(id);
    }

    public void deletarPassageiro(int id) {
        remover(id);
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
        json.put("nome", Criptografia.criptografar(getNome()));
        json.put("sobrenome", Criptografia.criptografar(getSobrenome()));
        json.put("telefone", Criptografia.criptografar(getTelefone()));
        json.put("email", Criptografia.criptografar(getEmail()));
        json.put("senha", Criptografia.criptografar(getSenha()));
        return json;
    }

}
