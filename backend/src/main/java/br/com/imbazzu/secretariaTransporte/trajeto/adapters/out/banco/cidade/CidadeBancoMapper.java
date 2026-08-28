package br.com.imbazzu.secretariaTransporte.trajeto.adapters.out.banco.cidade;

import br.com.imbazzu.secretariaTransporte.trajeto.core.cidade.domain.Cidade;
import br.com.imbazzu.secretariaTransporte.trajeto.core.cidade.valueObject.TempoViagem;
import br.com.imbazzu.secretariaTransporte.compartilhados.domain.valueObject.Nome;

public class CidadeBancoMapper {

    public static Cidade paraDomainCidade(CidadeBanco cidade) {

        return  Cidade.reconstruir(
                cidade.getId(),
                new Nome(cidade.getNome()),
                cidade.getEstado(),
                new TempoViagem(cidade.getTempoViagem()),
                cidade.isArquivado()
        );
    }

    public static CidadeBanco paraEntityCidade(Cidade cidade) {

        return new CidadeBanco(
                cidade.getId(),
                cidade.getNome().valor(),
                cidade.getEstado(),
                cidade.getTempoViagem().emDuration(),
                cidade.isArquivado()
        );
    }




}
