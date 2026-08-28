package br.com.imbazzu.secretariaTransporte.frota.adapters.out.veiculo;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(
        name = "veiculos",
        uniqueConstraints = {@UniqueConstraint(name = "uk_tipoveiculo_nome", columnNames = "nome")}
)
@NoArgsConstructor
@Getter
@Setter
public class VeiculoBanco {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "nome", nullable = false, length = 20)
    private String nome;

    @Column(name = "capacidade", nullable = false)
    private int capacidade;


    @Column(name = "arquivado", nullable = false)
    private boolean arquivado;
}
