package br.com.imbazzu.secretariaTransporte.trajeto.adapters.out.banco.destino;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import java.util.UUID;

/**
 * Representa o local onde o passageiro irá
 */
@Entity
@Table(name = "destinos",uniqueConstraints = @UniqueConstraint(
        columnNames = {"nome","cidade_id"},
        name = "uk_destinos_nome_cidade"))
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FilterDef(
        name = "deletadoFilter",
        parameters = @ParamDef(name = "deletadoParam", type = Boolean.class)
)
@Filter(name = "deletadoFilter", condition = "deletado = :deletadoParam")
public class DestinoBanco {
    /**
     * Identificador único
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    @Setter(AccessLevel.PRIVATE)
    private UUID id;

    /**
     * Nome do destino
     */
    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    /**
     * A cidade onde o destino se encontra
     */
    @Column(name = "cidade_id", nullable = false,updatable = false)
    private UUID cidadeId;

    @Column(name = "deletado", nullable = false)
    private boolean deletado = false;
}
