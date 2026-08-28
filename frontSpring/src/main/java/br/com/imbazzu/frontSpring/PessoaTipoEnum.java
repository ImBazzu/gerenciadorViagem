package br.com.imbazzu.frontSpring;

public enum PessoaTipoEnum {
    COMUM("Comum"),
    SOMENTE_CARRO("Somente Carro"),
    CARRO_OU_VAN("Carro ou Van");

    private final String label;

    PessoaTipoEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
