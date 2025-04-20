package com.umonitoring.models;

import com.umonitoring.utils.Criptografia;

import java.util.ArrayList;
import java.util.List;

import com.umonitoring.api.MotoristaAPI;

import org.json.JSONArray;
import org.json.JSONObject;

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
        this.modeloDoCarro = Criptografia.criptografar(modeloDoCarro);
        this.placaDoCarro = Criptografia.criptografar(placaDoCarro);
        this.disponibilidade = Criptografia.criptografar(disponibilidade);
        this.statusProtocolo = Criptografia.criptografar(statusProtocolo);
        this.frase1 = Criptografia.criptografar(frase1);
        this.frase2 = Criptografia.criptografar(frase2);
        this.frase3 = Criptografia.criptografar(frase3);
    }

    //----------------------------- Getters e Setters -----------------------------

    public String getModeloDoCarro() {
        return Criptografia.descriptografar(modeloDoCarro);
    }
    public void setModeloDoCarro(String modelo) {
        this.modeloDoCarro = Criptografia.criptografar(modelo);
    }
    public String getPlacaDoCarro() {
        return Criptografia.descriptografar(placaDoCarro);
    }
    public void setPlacaDoCarro(String placa) {
        this.placaDoCarro = Criptografia.criptografar(placa);
    }
    public String getDisponibilidade() {
        return Criptografia.descriptografar(disponibilidade);
    }
    public void setDisponibilidade(String disponibilidade) {
        this.disponibilidade = Criptografia.criptografar(disponibilidade);
    }
    public String getStatusProtocolo() {
        return Criptografia.descriptografar(statusProtocolo);
    }
    public void setStatusProtocolo(String status) {
        this.statusProtocolo = Criptografia.criptografar(status);
    }
    public String getFrase1() {
        return Criptografia.descriptografar(frase1);
    }
    public void setFrase1(String f1) {
        this.frase1 = Criptografia.criptografar(f1);
    }
    public String getFrase2() {
        return Criptografia.descriptografar(frase2);
    }
    public void setFrase2(String f2) {
        this.frase2 = Criptografia.criptografar(f2);
    }
    public String getFrase3() {
        return Criptografia.descriptografar(frase3);
    }
    public void setFrase3(String f3) {
        this.frase3 = Criptografia.criptografar(f3);
    }

    // ----------------------------- funções do CRUD -----------------------------

    public void criarMotorista() {
        salvar();
    }
    public List<Motorista> listarMotoristas() {
        String resposta = MotoristaAPI.listarTodos();
        List<Motorista> lista = new ArrayList<>();

        try {
            JSONArray array = new JSONArray(resposta);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);

                Motorista m = new Motorista(
                        obj.getInt("id"),
                        obj.getString("nome"),
                        obj.getString("sobrenome"),
                        obj.getString("telefone"),
                        obj.getString("email"),
                        obj.getString("senha"),
                        obj.getString("modelo_do_carro"),
                        obj.getString("placa_do_carro"),
                        obj.getString("disponibilidade"),
                        obj.getString("status_protocolo"),
                        obj.getString("frase_de_seguranca_1"),
                        obj.getString("frase_de_seguranca_2"),
                        obj.getString("frase_de_seguranca_3")
                );
                lista.add(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public static Motorista buscarMotoristaPorId(int id) {
        String resposta = MotoristaAPI.buscarPorId(id);

        try {
            JSONObject obj = new JSONObject(resposta);
            return new Motorista(
                    obj.getInt("id"),
                    obj.getString("nome"),
                    obj.getString("sobrenome"),
                    obj.getString("telefone"),
                    obj.getString("email"),
                    obj.getString("senha"),
                    obj.getString("modelo_do_carro"),
                    obj.getString("placa_do_carro"),
                    obj.getString("disponibilidade"),
                    obj.getString("status_protocolo"),
                    obj.getString("frase_de_seguranca_1"),
                    obj.getString("frase_de_seguranca_2"),
                    obj.getString("frase_de_seguranca_3")
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void atualizarMotorista(int id, Motorista motorista) {
        motorista.atualizar(id);
    }

    public void deletarMotorista(int id) {
        remover(id);
    }

    public String salvar() {
        try {
            JSONObject json = montarJson();
            return MotoristaAPI.cadastrar(json);
        } catch (Exception e) {
            return "Erro ao salvar motorista: " + e.getMessage();
        }
    }

    public String atualizar(int id) {
        try {
            JSONObject json = montarJson();
            return MotoristaAPI.atualizar(id, json);
        } catch (Exception e) {
            return "Erro ao atualizar motorista: " + e.getMessage();
        }
    }

    public String remover(int id) {
        try {
            return MotoristaAPI.deletar(id);
        } catch (Exception e) {
            return "Erro ao remover motorista: " + e.getMessage();
        }
    }

    private JSONObject montarJson() throws Exception {
        JSONObject json = new JSONObject();
        json.put("nome", Criptografia.criptografar(getNome()));
        json.put("sobrenome", Criptografia.criptografar(getSobrenome()));
        json.put("telefone", Criptografia.criptografar(getTelefone()));
        json.put("email", Criptografia.criptografar(getEmail()));
        json.put("senha", Criptografia.criptografar(getSenha()));
        json.put("modelo_do_carro", modeloDoCarro);
        json.put("placa_do_carro", placaDoCarro);
        json.put("disponibilidade", disponibilidade);
        json.put("status_protocolo", statusProtocolo);
        json.put("frase_de_seguranca_1", frase1);
        json.put("frase_de_seguranca_2", frase2);
        json.put("frase_de_seguranca_3", frase3);
        return json;
    }



}

