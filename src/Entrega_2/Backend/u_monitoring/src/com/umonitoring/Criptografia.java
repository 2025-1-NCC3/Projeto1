package com.umonitoring;

public class Criptografia {

    public static String criptografar(String texto, int chave) {
        StringBuilder resultado = new StringBuilder();
        for (char c : texto.toCharArray()) {
            if (Character.isUpperCase(c)) {
                resultado.append((char) ((c - 'A' + chave) % 26 + 'A'));
            } else if (Character.isLowerCase(c)) {
                resultado.append((char) ((c - 'a' + chave) % 26 + 'a'));
            } else {
                resultado.append(c); // números, @, ., etc. ficam inalterados
            }
        }
        return resultado.toString();
    }

    public static String descriptografar(String texto, int chave) {
        return criptografar(texto, 26 - (chave % 26)); // inverso da cifra
    }
}
