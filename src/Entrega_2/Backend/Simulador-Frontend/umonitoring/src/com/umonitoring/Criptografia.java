package com.umonitoring;

public class Criptografia {

    public static String criptografar(String texto, int chave) {

        StringBuilder resultado = new StringBuilder();

        for (char c : texto.toCharArray()) {
            if (Character.isUpperCase(c)) {
                resultado.append((char) ((c - 'A' + chave) % 26 + 'A'));
            } 
            else if (Character.isLowerCase(c)) {
                resultado.append((char) ((c - 'a' + chave) % 26 + 'a'));
            } 
            else if (Character.isDigit(c)) {
                resultado.append((char) ((c - '0' + chave) % 10 + '0'));
            } 
            else {
                resultado.append(c); // símbolos e outros caracteres inalterados
            }
        }
        return resultado.toString();
    }

    public static String descriptografar(String texto, int chave) {

        StringBuilder resultado = new StringBuilder();
        
        for (char c : texto.toCharArray()) {
            if (Character.isUpperCase(c)) {
                resultado.append((char) ((c - 'A' - chave + 26) % 26 + 'A'));
            } else if (Character.isLowerCase(c)) {
                resultado.append((char) ((c - 'a' - chave + 26) % 26 + 'a'));
            } else if (Character.isDigit(c)) {
                resultado.append((char) ((c - '0' - chave + 10) % 10 + '0'));
            } else {
                resultado.append(c);
            }
        }
        return resultado.toString();
    }
}
