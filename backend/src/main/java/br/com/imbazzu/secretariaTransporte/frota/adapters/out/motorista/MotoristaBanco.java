package br.com.imbazzu.secretariaTransporte.frota.adapters.out.motorista;


import br.com.imbazzu.secretariaTransporte.frota.adapters.out.motoristaveiculo.MotoristaVeiculoBanco;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "motoristas",
        uniqueConstraints = @UniqueConstraint(name = "uk_motorista_cpf",
                columnNames = {"cpf"}))
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MotoristaBanco {

        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private UUID id;

        @Column(name = "nome", nullable = false, length = 60)
        private String nome;

        @Column(name = "cpf", nullable = false, length = 11)
        private String cpf;

        @Column(name = "telefone",nullable = false, length = 11)
        private String telefone;

        @OneToMany(mappedBy = "motoristaBanco",
                cascade = CascadeType.ALL, orphanRemoval = true)
        private List<MotoristaVeiculoBanco> listaVeiculos;

        @Column(name = "arquivado", nullable = false)
        private boolean arquivado;

}
