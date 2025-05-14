package com.umonitoring.utils;

public class Sessao {

    private static int idUsuario = -1;

    // Define o ID do usuário
    public static void setIdUsuario(int id) {
        idUsuario = id;
    }

    // Retorna o ID do usuário
    public static int getIdUsuario() {
        return idUsuario;
    }

    // Verifica se o usuário está logado
    public static boolean isLogado() {
        return idUsuario != -1;
    }

    // Encerra a sessão
    public static void limparSessao() {
        idUsuario = -1;
    }
}
