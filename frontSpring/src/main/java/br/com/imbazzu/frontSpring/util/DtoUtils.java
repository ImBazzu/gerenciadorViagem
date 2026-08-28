package br.com.imbazzu.frontSpring.util;

public final class DtoUtils {

    public static String formatarTelefone(String telefone) {
        if (telefone == null || telefone.isBlank()) {
            return telefone;
        }

        String digitos = telefone.replaceAll("\\D", "");

        if (digitos.length() != 11) {
            return telefone;
        }

        return "(%s)%s-%s".formatted(
                digitos.substring(0, 2),
                digitos.substring(2, 7),
                digitos.substring(7)
        );
    }

    public static String formatarCpf(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            return cpf;
        }

        String digitos = cpf.replaceAll("\\D", "");

        if (digitos.length() != 11) {
            return cpf;
        }

        return "%s.%s.%s-%s".formatted(
                digitos.substring(0, 3),
                digitos.substring(3, 6),
                digitos.substring(6, 9),
                digitos.substring(9)
        );
    }
}
