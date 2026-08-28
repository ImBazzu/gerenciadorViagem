package br.com.imbazzu.secretariaTransporte.trajeto.core.destino.domain;

import br.com.imbazzu.secretariaTransporte.compartilhados.domain.valueObject.Nome;

import java.util.Objects;
import java.util.UUID;


public final class Destino {

    /**
     * Identificador único
     */
    private final UUID id;

    /**
     * Nome do lugar
     */
    private Nome nome;

    /**
     * Cidade dona de destino
     */
    private final UUID cidadeId;

    /**
     * arquivado
     */
    private boolean arquivado;

    //====================================================================================
    //Construtores
    //====================================================================================

    /**
     * Construtor para o mapper
     *
     * @param nome nome do destino
     */
    private Destino(UUID id, UUID cidadeId,Nome nome, boolean arquivado) {
        Objects.requireNonNull(id,"ID é obrigatório");
        Objects.requireNonNull(cidadeId,"Id cidade é obrigatório");
        Objects.requireNonNull(nome,"Nome é obrigatório"
            );

        this.id = id;
        this.nome = nome;
        this.cidadeId = cidadeId;
        this.arquivado = arquivado;
    }

    /**
     * Construtor para a criação do destino gerenciado pela cidade
     * @param nome nome da cidade
     */
    public static Destino criar(UUID cidadeId,Nome nome) {
        return new Destino(UUID.randomUUID(), cidadeId, nome,false);
    }

    public static Destino reconstruir(UUID id, UUID cidadeId, Nome nome, boolean arquivado) {
        return new Destino(id, cidadeId ,nome, arquivado);
    }

    //====================================================================================
    //Regras de Negócios
    //====================================================================================
    public void alterarNome(Nome novoNome) {
        Objects.requireNonNull(novoNome,"Nome é obrigatório");

        this.nome = novoNome;
    }

    public void arquivar() {
        if(this.arquivado) {
            return;
        }
        this.arquivado = true;
    }

    public void desarquivar() {
        if(!this.arquivado) {
            return;
        }
        this.arquivado = false;
    }


    //====================================================================================
    //Getters
    //====================================================================================

    public UUID getId() {
        return id;
    }

    public Nome getNome() {
        return nome;
    }

    public UUID getIdCidade() {
        return cidadeId;
    }

    public boolean isArquivado() {return arquivado;}
}
