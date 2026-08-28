package br.com.imbazzu.secretariaTransporte.usuario;

public enum UsuarioRoleEnum {

    ADMIN("ADMIN"),
    USUARIO("USER");

    private String role;

    UsuarioRoleEnum(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
