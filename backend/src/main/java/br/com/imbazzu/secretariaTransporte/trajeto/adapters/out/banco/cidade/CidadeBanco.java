package br.com.imbazzu.secretariaTransporte.trajeto.adapters.out.banco.cidade;

import br.com.imbazzu.secretariaTransporte.trajeto.core.cidade.domain.CidadeEstadoEnum;
import br.com.imbazzu.secretariaTransporte.compartilhados.util.DurationToMinutesConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import java.time.Duration;
import java.util.UUID;

/**
 * Representa a tabela Cidade
 */
@Entity
@Table(name = "cidades", uniqueConstraints = @UniqueConstraint(
        name = "uk_cidade_nome_estado",columnNames = {"nome","estado"}
))
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

@FilterDef(
        name = "deletadoFilter",
        parameters = @ParamDef(name = "deletadoParam", type = Boolean.class)
)
@Filter(name = "deletadoFilter", condition = "deletado = :deletadoParam")
public class CidadeBanco {

    /**
     * Identificador único
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="id",updatable = false, nullable = false)
    private UUID id;
    /**
     * Nome da cidade
     */
    @Column(name = "nome", nullable = false, length = 100)
    private String nome;
    /**
     * Estado que a cidade pertence
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 2)
    private CidadeEstadoEnum estado;
    /**
     * Tempo de viagem, salvo como minutos
     */
    @Convert(converter = DurationToMinutesConverter.class)
    @Column(name = "tempo_viagem",  nullable = false)
    private Duration tempoViagem;
    /**
     * Exclusão lógica para preservar o histórico
     */
    @Column(name = "arquivado", nullable = false)
    private boolean arquivado = false;

}
