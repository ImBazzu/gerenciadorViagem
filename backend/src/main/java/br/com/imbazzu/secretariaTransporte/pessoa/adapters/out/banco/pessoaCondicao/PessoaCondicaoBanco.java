package br.com.imbazzu.secretariaTransporte.pessoa.adapters.out.banco.pessoaCondicao;

import br.com.imbazzu.secretariaTransporte.pessoa.core.pessoaCondicao.domain.PessoaCondicao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "justificativas", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"nome"})
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PessoaCondicaoBanco {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "nome",nullable = false, length = 60)
    private String nome;

    @Column(name = "ativo",nullable = false)
    @Setter
    private boolean ativo = true;


    public PessoaCondicaoBanco(PessoaCondicao pessoaCondicao) {
        this.id = pessoaCondicao.getId();
        this.nome = pessoaCondicao.getNome().valor();
        this.ativo = pessoaCondicao.isArquivado();
    }
}
