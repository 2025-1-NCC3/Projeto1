package com.umonitoring.utils;

public class Criptografia {

    private static final int CHAVE = 3;

    public static String criptografar(String texto) {
        StringBuilder resultado = new StringBuilder();

        for (char c : texto.toCharArray()) {
            if (Character.isUpperCase(c)) {
                resultado.append((char) ((c - 'A' + CHAVE) % 26 + 'A'));
            } else if (Character.isLowerCase(c)) {
                resultado.append((char) ((c - 'a' + CHAVE) % 26 + 'a'));
            } else if (Character.isDigit(c)) {
                resultado.append((char) ((c - '0' + CHAVE) % 10 + '0'));
            } else {
                resultado.append(c);
            }
        }

        return resultado.toString();
    }

    public static String descriptografar(String texto) {
        StringBuilder resultado = new StringBuilder();

        for (char c : texto.toCharArray()) {
            if (Character.isUpperCase(c)) {
                resultado.append((char) ((c - 'A' - CHAVE + 26) % 26 + 'A'));
            } else if (Character.isLowerCase(c)) {
                resultado.append((char) ((c - 'a' - CHAVE + 26) % 26 + 'a'));
            } else if (Character.isDigit(c)) {
                resultado.append((char) ((c - '0' - CHAVE + 10) % 10 + '0'));
            } else {
                resultado.append(c);
            }
        }

        return resultado.toString();
    }
}
