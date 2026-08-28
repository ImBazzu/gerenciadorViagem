package br.com.imbazzu.secretariaTransporte.frota.core.domain.veiculo;

import br.com.imbazzu.secretariaTransporte.compartilhados.excecoes.gerais.DadosInvalidosException;
import br.com.imbazzu.secretariaTransporte.compartilhados.excecoes.gerais.EntidadeDuplicadaException;
import br.com.imbazzu.secretariaTransporte.compartilhados.excecoes.gerais.EntidadeNaoEncontradoException;
import br.com.imbazzu.secretariaTransporte.frota.core.domain.veiculo.valueobjects.NomeVeiculo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Veiculo {

    private UUID id;

    private NomeVeiculo nome;

    private int capacidade;

    private boolean arquivado;


    private Veiculo(UUID id, NomeVeiculo nome, int capacidade, boolean arquivado) {
        Objects.requireNonNull(id,"Id é obrigatório");
        Objects.requireNonNull(nome,"Nome do veiculo é obrigatório");

        this.id = id;
        this.nome = nome;
        this.capacidade = capacidade;
        this.arquivado = arquivado;
    }

    public Veiculo criar( NomeVeiculo nome, int capacidade) {
        Objects.requireNonNull(nome,"Nome do veiculo é obrigatório");
        if(capacidade <= 0) {
            throw new DadosInvalidosException("Capacidade informada não pode ser igual ou menor que zero");
        }
        return new Veiculo(UUID.randomUUID(),nome,capacidade,false);
    }

    public Veiculo recriar(UUID id,NomeVeiculo nome, int capacidade, boolean arquivado) {
        return new Veiculo(id, nome, capacidade ,arquivado);
    }

    public void editarVeiculo(NomeVeiculo nome, int capacidade) {
        alterarCapacidade(capacidade);
        alterarNome(nome);
    }

    public void alterarNome(NomeVeiculo nome){
        Objects.requireNonNull(nome,"Nome do veiculo é obrigatório");
        this.nome = nome;
    }

    public void alterarCapacidade(int capacidade){
        if(capacidade <= 0) {
            throw new DadosInvalidosException("Capacidade informada não pode ser igual ou menor que zero");
        }
        this.capacidade = capacidade;
    }

    public void arquivar(){
        this.arquivado = true;
    }
    public void desarquivar(){
        this.arquivado = false;
    }
}
