package br.com.liletbaby.back_end.services;

import org.springframework.stereotype.Service;

/**
 * DataValidatorService class.
 *
 * @author Wender Couto
 * @since 0.0.1-SNAPSHOT
 */

@Service
public class DataValidatorService { // Classe não tratada para ataques de XSS ou SQL Injection.

    public DataValidatorService() {

    }

    public static String validator(String username, String password) {
        if (username == null || username.isBlank() || username.length() < 3 || password == null || password.isBlank() || password.length() < 8) {
            return null;
        }
        return "Válidos";
    }

    public static String validator(String username, String fullName, String mail) {
        if (username == null || username.isBlank() || username.length() < 3 || fullName == null
            || fullName.isBlank() || fullName.length() < 3 || mail == null || mail.isBlank() || mail.length() < 5 || !mail.contains("@") || !mail.contains(".")) {
            return null;
        }
        return "Válidos";
    }

    public static String validator(String name, String sku, String color, String size, Integer stock, Double discount, Double price) {
        if (name == null || name.isBlank() || sku == null || sku.isBlank() || color == null
            || color.isBlank() || size == null || size.isBlank() || stock == null || discount == null || price == null) {
            return null;
        }
        return "Válidos";
    }

    //Próximo, para o cpf devemos saber se já tem algum cadastrado no banco de dados, se já houver, não pode ser alterado.
    public static String validator(String cpf) {
        cpf = cpf.replace(".", "").replace("-", "");

        if (cpf == null || cpf.isBlank() || cpf.length() != 11 || cpf.matches(cpf.charAt(0) + "{11}")) {
            return null;
        }

        for (int i = 0; i < 2; i++) {
            int sum = 0;
            for (int j = 0; j < 9 + i; j++) {
                sum += Integer.parseInt(cpf.charAt(i) + "") * (10 + i - j);
            }
            int calculatedDigit = 11 - sum % 11;
            calculatedDigit = calculatedDigit >= 10 ? 0 : calculatedDigit;
            if (calculatedDigit != Integer.parseInt(cpf.charAt(9 + i) + "")) {
                return null;
            }
        }
        return "Válidos";
    }

}