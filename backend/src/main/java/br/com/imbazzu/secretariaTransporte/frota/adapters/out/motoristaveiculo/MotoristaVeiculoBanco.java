package br.com.imbazzu.secretariaTransporte.frota.adapters.out.motoristaveiculo;


import br.com.imbazzu.secretariaTransporte.frota.adapters.out.motorista.MotoristaBanco;
import br.com.imbazzu.secretariaTransporte.frota.adapters.out.veiculo.VeiculoBanco;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;


@Entity
@Table(name = "motorista_veiculo", uniqueConstraints =
@UniqueConstraint(name = "uk_motoristaveiculo_idmotorista_idveiculo",
        columnNames = {"motorista_id","veiculo_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MotoristaVeiculoBanco {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(
            name = "motorista_id"
    )
    private MotoristaBanco motoristaBanco;

    @ManyToOne
    @JoinColumn(name = "veiculo_id")
    private VeiculoBanco veiculoBanco;

}
