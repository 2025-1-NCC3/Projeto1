package com.umonitoring;

import java.util.List;

public class App {
    public static void main(String[] args) {

        System.out.println("TODOS OS MOTORISTAS");
        List<Motorista> motoristas = ApiService.buscarTodosMotoristas();

        for (Motorista m : motoristas) {
            System.out.println("ID: " + m.getId());
            System.out.println("Nome: " + m.getNome() + " " + m.getSobrenome());
            System.out.println("Telefone: " + m.getTelefone());
            System.out.println("Email: " + m.getEmail());
            System.out.println("Senha: " + m.getSenha());
            System.out.println("Modelo do Carro: " + m.getModeloDoCarro());
            System.out.println("Placa do Carro: " + m.getPlacaDoCarro());
            System.out.println("Disponibilidade: " + m.getDisponibilidade());
            System.out.println("Status do Protocolo: " + m.getStatusProtocolo());
            System.out.println("Frase 1: " + m.getFrase1());
            System.out.println("Frase 2: " + m.getFrase2());
            System.out.println("Frase 3: " + m.getFrase3());
            System.out.println("------------------------------------------------");
        }

        System.out.println("\nTODOS OS PASSAGEIROS");
        List<Passageiro> passageiros = ApiService.buscarTodosPassageiros();
        for (Passageiro p : passageiros) {
            System.out.println("ID: " + p.getId());
            System.out.println("Nome: " + p.getNome() + " " + p.getSobrenome());
            System.out.println("Telefone: " + p.getTelefone());
            System.out.println("Email: " + p.getEmail());
            System.out.println("Senha: " + p.getSenha());
            System.out.println("------------------------------------------------");
        }

        System.out.println("\nTODAS AS VIAGENS");
        List<Viagem> viagens = ApiService.buscarTodasViagens();
        for (Viagem v : ApiService.buscarTodasViagens()) {
            System.out.println("ID da Viagem: " + v.getId());
            System.out.println("Endereço de Partida: " + v.getEnderecoDePartida());
            System.out.println("Endereço de Chegada: " + v.getEnderecoDeChegada());
            System.out.println("Data/Hora de Partida: " + v.getDataHoraDePartida());
            System.out.println("Data/Hora de Chegada: " + v.getDataHoraDeChegada());
            System.out.println("Status: " + v.getStatus());
            System.out.println("Motorista: " + v.getMotorista().getNome() + " " + v.getMotorista().getSobrenome());
            System.out.println("Passageiro: " + v.getPassageiro().getNome() + " " + v.getPassageiro().getSobrenome());
            System.out.println("------------------------------------------------");
        }
        
    }
}
