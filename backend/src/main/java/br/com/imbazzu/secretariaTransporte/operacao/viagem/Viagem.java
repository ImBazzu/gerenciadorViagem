package br.com.imbazzu.secretariaTransporte.operacao.viagem;

import br.com.imbazzu.secretariaTransporte.trajeto.core.cidade.domain.Cidade;
import br.com.imbazzu.secretariaTransporte.compartilhados.excecoes.gerais.DadosInvalidosException;
import br.com.imbazzu.secretariaTransporte.operacao.lista.ListaDoDia;
import br.com.imbazzu.secretariaTransporte.frota.core.domain.motorista.Motorista;
import br.com.imbazzu.secretariaTransporte.operacao.passageiro.Passageiro;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

/**
 * Representa uma viagem realizada por um motorista com um conjunto de passageiros.
 * A data da viagem é derivada da {@link ListaDoDia}
 * à qual os passageiros pertencem — não é armazenada aqui.
 */
@Entity
@Table(name = "viagens")
@Getter
@Setter
@NoArgsConstructor
public class Viagem {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id",columnDefinition = "BINARY(16)",updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "motorista_id", columnDefinition = "BINARY(16)")
    private Motorista motorista;


    @Column(name="data",columnDefinition = "DATE",nullable = false)
    private LocalDate data;

    @Column(name="hora",columnDefinition = "TIME", nullable = false)
    private LocalTime hora;

    @OneToMany(mappedBy = "viagem", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Passageiro> passageiros = new ArrayList<>();

    // -------------------------------------------------------------------------
    // Construtores
    // -------------------------------------------------------------------------

    /**
     * Cria uma viagem com hora de saída definida.
     */
    public Viagem(LocalDate data, LocalTime hora) {
        setData(data);
        setHora(hora);
    }

    /**
     * Cria uma viagem já com o primeiro passageiro e calcula a hora de saída a partir dele.
     * Usado na geração automática de viagens.
     */
    public Viagem(Passageiro primeiroPassageiro) {
        setData(primeiroPassageiro.getListaDoDia().getData()); // primeiro
        adicionarPassageiro(primeiroPassageiro);          // depois
        this.hora = primeiroPassageiro.calcularHoraSaida();
    }

    // -------------------------------------------------------------------------
    // Gerenciamento de passageiros
    // -------------------------------------------------------------------------

    /**
     * Adiciona um passageiro à viagem e vincula a referência inversa.
     */
    public void adicionarPassageiro(Passageiro passageiro) {
        if(!(this.data.equals(passageiro.getListaDoDia().getData()))) {
            throw  new DadosInvalidosException("O passageiro deve ser da mesma data da viagem cadastrada");
        }
        this.passageiros.add(passageiro);
        passageiro.setViagem(this);
    }

    /**
     * Remove um passageiro da viagem e desvincula a referência inversa.
     */
    public void removerPassageiro(Passageiro passageiro) {
        if (!this.passageiros.contains(passageiro)) {
            throw new DadosInvalidosException("Passageiro não pertence a esta viagem");
        }
        this.passageiros.remove(passageiro);
        passageiro.setViagem(null);
    }

    /**
     * Remove todos os passageiros da viagem.
     * Usado antes de recarregar a lista em edições.
     */
    public void limparPassageiros() {
        this.passageiros.forEach(p -> p.setViagem(null));
        this.passageiros.clear();
    }

    // -------------------------------------------------------------------------
    // Consultas / regras de negócio
    // -------------------------------------------------------------------------

    /**
     * Retorna as cidades distintas dos destinos dos passageiros, ordenadas por nome.
     */
    public List<Cidade> getDestinos() {
        return passageiros.stream()
                .map(p -> p.getDestino().getCidade())
                .distinct()
                .sorted(Comparator.comparing(Cidade::getNome))
                .toList();
    }

    /**
     * Total de lugares ocupados considerando acompanhantes.
     */
    public int lugaresOcupados() {
        return passageiros.stream()
                .mapToInt(Passageiro::quantidadeTotalPassageiros)
                .sum();
    }

    /**
     * Verifica se um passageiro é compatível com esta viagem segundo três critérios:
     * <ul>
     *   <li>Destino (cidade) em comum com ao menos um passageiro existente</li>
     *   <li>Capacidade disponível (máximo 4 lugares)</li>
     *   <li>Horário de chegada com diferença máxima de 1 hora em relação a todos os passageiros</li>
     * </ul>
     */
    public boolean passageirosCompativel(Passageiro passageiro) {
        // Verifica destino compatível
        boolean destinoCompativel = getDestinos().stream()
                .anyMatch(cidade -> cidade.equals(passageiro.getDestino().getCidade()));
        if (!destinoCompativel) return false;

        // Verifica capacidade
        int lugaresNecessarios = passageiro.quantidadeTotalPassageiros();
        if (lugaresOcupados() + lugaresNecessarios > 4) return false;

        // Verifica compatibilidade de horário (máx. 1h de diferença)
        return passageiros.stream().allMatch(p ->
                Duration.between(p.getHoraChegada(), passageiro.getHoraChegada())
                        .abs()
                        .compareTo(Duration.ofHours(1)) <= 0
        );
    }

    /**
     * Recalcula e define a hora de saída com base no passageiro
     * com o menor horário de chegada calculado.
     *
     * @throws IllegalStateException se a viagem não tiver passageiros
     */
    public void recalcularHoraSaida() {
        if (passageiros.isEmpty()) {
            throw new IllegalStateException("Viagem sem passageiros não pode definir hora de saída");
        }
        this.hora = passageiros.stream()
                .map(Passageiro::calcularHoraSaida)
                .min(LocalTime::compareTo)
                .orElseThrow();
    }
}