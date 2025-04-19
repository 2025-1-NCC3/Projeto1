package com.umonitoring;

import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcao1 = -1;

        while (opcao1 != 0) {
            System.out.println("\n==== MENU PRINCIPAL ====");
            System.out.println("1 - CRUD Motorista");
            System.out.println("2 - CRUD Passageiro");
            System.out.println("3 - CRUD Viagem");
            System.out.println("0 - Sair");
            System.out.print(">> ");
            opcao1 = scanner.nextInt();
            scanner.nextLine();

            switch (opcao1) {
                case 1 -> crudMotorista(scanner);
                case 2 -> crudPassageiro(scanner);
                case 3 -> crudViagem(scanner);
                case 0 -> System.out.println("Encerrando...");
                default -> System.out.println("Opção inválida.");
            }
        }
        scanner.close();
    }

    private static void crudMotorista(Scanner scanner) {
        System.out.println("\n=== CRUD MOTORISTA ===");
        System.out.println("1 - Cadastrar");
        System.out.println("2 - Listar todos");
        System.out.println("3 - Buscar por ID");
        System.out.println("4 - Atualizar");
        System.out.println("5 - Deletar");
        System.out.print(">> ");
        int op = scanner.nextInt();
        scanner.nextLine();

        switch (op) {
            case 1 -> {
                System.out.print("ID: "); int id = scanner.nextInt(); scanner.nextLine();
                System.out.print("Nome: "); String nome = scanner.nextLine();
                System.out.print("Sobrenome: "); String sobrenome = scanner.nextLine();
                System.out.print("Telefone: "); String telefone = scanner.nextLine();
                System.out.print("Email: "); String email = scanner.nextLine();
                System.out.print("Senha: "); String senha = scanner.nextLine();
                System.out.print("Modelo do carro: "); String modelo = scanner.nextLine();
                System.out.print("Placa do carro: "); String placa = scanner.nextLine();
                System.out.print("Disponibilidade: "); String disp = scanner.nextLine();
                System.out.print("Status protocolo: "); String status = scanner.nextLine();
                System.out.print("Frase 1: "); String f1 = scanner.nextLine();
                System.out.print("Frase 2: "); String f2 = scanner.nextLine();
                System.out.print("Frase 3: "); String f3 = scanner.nextLine();

                Motorista novo = new Motorista(id, nome, sobrenome, telefone, email, senha, modelo, placa, disp, status, f1, f2, f3);
                System.out.println(APIService.cadastrarMotorista(novo) ? "✅ Cadastrado com sucesso." : "❌ Falha ao cadastrar.");
            }
            case 2 -> APIService.buscarTodosMotoristas().forEach(m ->
                System.out.println(m.getId() + ": " + m.getNome() + " " + m.getSobrenome()));
            case 3 -> {
                System.out.print("ID: "); int id = scanner.nextInt();
                Motorista m = APIService.buscarMotoristaPorId(id);
                if (m != null) System.out.println(m.getNome() + " " + m.getSobrenome());
                else System.out.println("❌ Não encontrado.");
            }
            case 4 -> {
                System.out.print("ID: "); int id = scanner.nextInt(); scanner.nextLine();
                System.out.print("Nome: "); String nome = scanner.nextLine();
                System.out.print("Sobrenome: "); String sobrenome = scanner.nextLine();
                System.out.print("Telefone: "); String telefone = scanner.nextLine();
                System.out.print("Email: "); String email = scanner.nextLine();
                System.out.print("Senha: "); String senha = scanner.nextLine();
                System.out.print("Modelo do carro: "); String modelo = scanner.nextLine();
                System.out.print("Placa do carro: "); String placa = scanner.nextLine();
                System.out.print("Disponibilidade: "); String disp = scanner.nextLine();
                System.out.print("Status protocolo: "); String status = scanner.nextLine();
                System.out.print("Frase 1: "); String f1 = scanner.nextLine();
                System.out.print("Frase 2: "); String f2 = scanner.nextLine();
                System.out.print("Frase 3: "); String f3 = scanner.nextLine();
                Motorista m = new Motorista(id, nome, sobrenome, telefone, email, senha, modelo, placa, disp, status, f1, f2, f3);
                System.out.println(APIService.atualizarMotorista(m) ? "✅ Atualizado." : "❌ Erro ao atualizar.");
            }
            case 5 -> {
                System.out.print("ID: "); int id = scanner.nextInt();
                System.out.println(APIService.deletarMotorista(id) ? "✅ Deletado." : "❌ Erro ao deletar.");
            }
            default -> System.out.println("Opção inválida.");
        }
    }

    private static void crudPassageiro(Scanner scanner) {
        System.out.println("\n=== CRUD PASSAGEIRO ===");
        System.out.println("1 - Cadastrar");
        System.out.println("2 - Listar todos");
        System.out.println("3 - Buscar por ID");
        System.out.println("4 - Atualizar");
        System.out.println("5 - Deletar");
        System.out.print(">> ");
        int op = scanner.nextInt();
        scanner.nextLine();

        switch (op) {
            case 1 -> {
                System.out.print("ID: "); int id = scanner.nextInt(); scanner.nextLine();
                System.out.print("Nome: "); String nome = scanner.nextLine();
                System.out.print("Sobrenome: "); String sobrenome = scanner.nextLine();
                System.out.print("Telefone: "); String telefone = scanner.nextLine();
                System.out.print("Email: "); String email = scanner.nextLine();
                System.out.print("Senha: "); String senha = scanner.nextLine();
                Passageiro novo = new Passageiro(id, nome, sobrenome, telefone, email, senha);
                System.out.println(APIService.cadastrarPassageiro(novo) ? "✅ Cadastrado com sucesso." : "❌ Falha ao cadastrar.");
            }
            case 2 -> APIService.buscarTodosPassageiros().forEach(p ->
                System.out.println(p.getId() + ": " + p.getNome() + " " + p.getSobrenome()));
            case 3 -> {
                System.out.print("ID: "); int id = scanner.nextInt();
                Passageiro p = APIService.buscarPassageiroPorId(id);
                if (p != null) System.out.println(p.getNome() + " " + p.getSobrenome());
                else System.out.println("❌ Não encontrado.");
            }
            case 4 -> {
                System.out.print("ID: "); int id = scanner.nextInt(); scanner.nextLine();
                System.out.print("Nome: "); String nome = scanner.nextLine();
                System.out.print("Sobrenome: "); String sobrenome = scanner.nextLine();
                System.out.print("Telefone: "); String telefone = scanner.nextLine();
                System.out.print("Email: "); String email = scanner.nextLine();
                System.out.print("Senha: "); String senha = scanner.nextLine();
                Passageiro p = new Passageiro(id, nome, sobrenome, telefone, email, senha);
                System.out.println(APIService.atualizarPassageiro(p) ? "✅ Atualizado." : "❌ Erro ao atualizar.");
            }
            case 5 -> {
                System.out.print("ID: "); int id = scanner.nextInt();
                System.out.println(APIService.deletarPassageiro(id) ? "✅ Deletado." : "❌ Erro ao deletar.");
            }
            default -> System.out.println("Opção inválida.");
        }
    }

    private static void crudViagem(Scanner scanner) {
        System.out.println("\n=== CRUD VIAGEM ===");
        System.out.println("1 - Cadastrar");
        System.out.println("2 - Listar todas");
        System.out.println("3 - Buscar por ID");
        System.out.println("4 - Atualizar");
        System.out.println("5 - Deletar");
        System.out.print(">> ");
        int op = scanner.nextInt();
        scanner.nextLine();

        switch (op) {
            case 1 -> {
                System.out.print("ID: "); int id = scanner.nextInt(); scanner.nextLine();
                System.out.print("Partida: "); String partida = scanner.nextLine();
                System.out.print("Chegada: "); String chegada = scanner.nextLine();
                System.out.print("Hora partida: "); String hp = scanner.nextLine();
                System.out.print("Hora chegada: "); String hc = scanner.nextLine();
                System.out.print("Status: "); String status = scanner.nextLine();
                System.out.print("ID Motorista: "); int idM = scanner.nextInt();
                System.out.print("ID Passageiro: "); int idP = scanner.nextInt();
                Viagem v = new Viagem(id, partida, chegada, hp, hc, status,
                APIService.buscarMotoristaPorId(idM), APIService.buscarPassageiroPorId(idP));
                System.out.println(APIService.cadastrarViagem(v) ? "✅ Cadastrada." : "❌ Falha ao cadastrar.");
            }
            case 2 -> APIService.buscarTodasViagens().forEach(v ->
                System.out.println(v.getId() + ": " + v.getEnderecoDePartida() + " → " + v.getEnderecoDeChegada()));
            case 3 -> {
                System.out.print("ID: "); int id = scanner.nextInt();
                Viagem v = APIService.buscarViagemPorId(id);
                if (v != null) System.out.println(v.getEnderecoDePartida() + " → " + v.getEnderecoDeChegada());
                else System.out.println("❌ Não encontrada.");
            }
            case 4 -> {
                System.out.print("ID: "); int id = scanner.nextInt(); scanner.nextLine();
                System.out.print("Partida: "); String partida = scanner.nextLine();
                System.out.print("Chegada: "); String chegada = scanner.nextLine();
                System.out.print("Hora partida: "); String hp = scanner.nextLine();
                System.out.print("Hora chegada: "); String hc = scanner.nextLine();
                System.out.print("Status: "); String status = scanner.nextLine();
                System.out.print("ID Motorista: "); int idM = scanner.nextInt();
                System.out.print("ID Passageiro: "); int idP = scanner.nextInt();
                Viagem v = new Viagem(id, partida, chegada, hp, hc, status,
                APIService.buscarMotoristaPorId(idM), APIService.buscarPassageiroPorId(idP));
                System.out.println(APIService.atualizarViagem(v) ? "✅ Atualizada." : "❌ Falha ao atualizar.");
            }
            case 5 -> {
                System.out.print("ID: "); int id = scanner.nextInt();
                System.out.println(APIService.deletarViagem(id) ? "✅ Deletada." : "❌ Erro ao deletar.");
            }
            default -> System.out.println("Opção inválida.");
        }
    }
}
  
