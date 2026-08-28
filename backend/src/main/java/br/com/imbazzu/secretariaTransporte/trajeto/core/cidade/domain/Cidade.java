package br.com.imbazzu.secretariaTransporte.trajeto.core.cidade.domain;


import br.com.imbazzu.secretariaTransporte.trajeto.core.cidade.valueObject.TempoViagem;
import br.com.imbazzu.secretariaTransporte.compartilhados.domain.valueObject.Nome;

import java.time.LocalTime;
import java.util.*;


//Domain Cidade
//-------------------Não pode depender de Ninguém------------------------
//Contém apenas as regras de negócio
public final class Cidade {

    /**
     * Identificador único
     */
    private final UUID id;

    /**
     * Estado que a cidade pertence
     */
    private CidadeEstadoEnum estado;
    /**
     * Exclusão lógica para preservar o histórico
     */
    private boolean arquivado;
    /**
     * Nome da cidade
     */
    private Nome nome;
    /**
     * Tempo de viagem, salvo como minutos
     */
    private TempoViagem tempoViagem;


    //====================================================================================
    //Construtores
    //====================================================================================

    //Construtor é com todos os dados privado para garantir que somente o mapper utilize
    private Cidade(UUID id,Nome nome, CidadeEstadoEnum estado, TempoViagem tempoViagem,
                    boolean arquivado) {
        Objects.requireNonNull(id,"ID é obrigatório");
        Objects.requireNonNull(nome,"Nome é obrigatório");
        Objects.requireNonNull(estado,"Estado é obrigatório");
        Objects.requireNonNull(tempoViagem,"Tempo de Viagem é obrigatório");


        this.id = id;
        this.nome = nome;
        this.estado = estado;
        this.tempoViagem = tempoViagem;
        this.arquivado = arquivado;
    }

    //====================================================================================
    //Factory Métodos
    //====================================================================================

    /**
     * Public metodo para qualquer criar uma cidade seguindo as regras de criação
     * @param nome nome da cidade | obrigatório
     * @param estado estado da cidade | obrigatório
     * @param tempoViagem tempo de viagem | obrigatório
     * @return Nova cidade criada
     */
    public static Cidade criar(Nome nome, CidadeEstadoEnum estado, TempoViagem tempoViagem) {
        return new Cidade(
                UUID.randomUUID(),
                nome,
                estado,
                tempoViagem,
                false);
    }

    /**
     * Recriar uma cidade do banco de dados
     * @param id identificador único da cidade
     * @param nome nome da cidade
     * @param estado estado da cidade
     * @param tempoViagem tempo de percurso entre as cidades
     * @param arquivado controle de registro por booleano
     * @return Cidade reconstruida
     */
    public static Cidade reconstruir(UUID id, Nome nome, CidadeEstadoEnum  estado,
                              TempoViagem tempoViagem, boolean arquivado) {
        return  new Cidade(id,nome,estado,tempoViagem, arquivado);
    }

    //====================================================================================
    //Regras de Negócios
    //====================================================================================

    /**
     * Define o nome da cidade em capslock
     * @param nome nome da cidade
     */
    public void alterarNome(Nome nome) {
        Objects.requireNonNull(nome,"Nome é obrigatório");

        //define o nome da entidade
        this.nome = nome;
    }

    /**
     * Define o Estado que a cidade pertence
     * @param estado Estado
     */
    public void alterarEstado(CidadeEstadoEnum estado) {
        //Verifica se o estado foi passado
        Objects.requireNonNull(estado,"Estado é obrigatório");

        //armazena o estado
        this.estado = estado;
    }

    public void alterarTempoViagem(TempoViagem tempoViagem) {
        Objects.requireNonNull(tempoViagem,"Tempo de viagem é obrigatório");

        this.tempoViagem = tempoViagem;
    }

    public void arquivar(){
        if (arquivado) {
            return;
        }
        this.arquivado = true;
    }


    /**
     * Dado o horario de chegada, informa o horario mínimo que se deve sair
     * @param chegada horario de chegada
     * @return horario mínimo que se deve sair
     */
    public LocalTime calcularHorarioMinimoSaida(LocalTime chegada) {
        Objects.requireNonNull(chegada,"Horário de chegada é obrigatório");

        return chegada.minus(tempoViagem.emDuration());
    }

    public void editar(Nome nome, CidadeEstadoEnum estado, TempoViagem tempoViagem) {
        alterarNome(nome);
        alterarEstado(estado);
        alterarTempoViagem(tempoViagem);
    }


    public void desarquivar(){
        if(!arquivado) {
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


    public CidadeEstadoEnum getEstado() {
        return estado;
    }

    public Nome getNome() {
        return nome;
    }

    public TempoViagem getTempoViagem() {
        return tempoViagem;
    }

    public boolean isArquivado() {
        return arquivado;
    }


}
