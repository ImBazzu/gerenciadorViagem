package br.com.imbazzu.secretariaTransporte.compartilhados.pdf;

import br.com.imbazzu.secretariaTransporte.operacao.passageiro.dto.PassageiroResponseDto;
import br.com.imbazzu.secretariaTransporte.operacao.viagem.dto.ViagemPorPeriodoResponseDto;
import br.com.imbazzu.secretariaTransporte.operacao.viagem.dto.ViagemResponseDto;
import br.com.imbazzu.secretariaTransporte.operacao.viagem.ViagemService;
import br.com.imbazzu.secretariaTransporte.compartilhados.util.DataHoraUtil;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;


@Service
@RequiredArgsConstructor
public class PdfService {

    private final ViagemService viagemService;

    public byte[] gerarPdf(LocalDate data) {
        try(var baos = new ByteArrayOutputStream()){
            var doc = new Document();
            PdfWriter.getInstance(doc,baos);
            doc.open();
            var carros = viagemService.buscarViagemSeparadoPorHorario(data);
            inserirCarros(doc, carros,data);
            doc.close();
            return baos.toByteArray();
        } catch (DocumentException | IOException e) {
            throw new RuntimeException("Erro ao gerar PDF para data: " + data, e);
        }
    }


    private void inserirCarros(Document doc, ViagemPorPeriodoResponseDto carros, LocalDate data){
        try {
            var titulo = PdfUtil.paragrafoCentralizadoVerde("RELAÇÃO DOS CARROS");

            titulo.setSpacingAfter(5);
            var subtitulo = PdfUtil.paragrafoCentralizadoVerde(DataHoraUtil.dataParaTexto(data));
            subtitulo.getFont().setSize(12);
            subtitulo.setSpacingAfter(10);
            var listaAntesDas5 = carros.antesDas0530();
            var listaDepoisDas5 = carros.depoisDas0530();
            if(!listaAntesDas5.isEmpty()){
                doc.add(titulo);
                doc.add(subtitulo);
                var tabelaAntesDas5h = inserirCarro(listaAntesDas5);
                doc.add(tabelaAntesDas5h);
                if(!listaDepoisDas5.isEmpty()){
                    doc.newPage();
                }
            }

            if(!listaDepoisDas5.isEmpty()){
                doc.add(titulo);
                doc.add(subtitulo);
                var tabelaDepoisDas5h = inserirCarro(listaDepoisDas5);
                doc.add(tabelaDepoisDas5h);
            }

        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }

    }

    private PdfPTable inserirCarro(List<ViagemResponseDto> viagems) throws DocumentException {
        var table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{5,5});


        for(int i=0; i<viagems.size(); i++){
            var viagem = viagems.get(i);
            PdfPCell celula = PdfUtil.criarCelula();
            viagem.passageiros().forEach(passageiro -> {
                celula.addElement(PdfUtil.paragrafoVerticalVermelho("*"+passageiro.nome(),passageiro.telefone()));
                for(int acompanhante=0;acompanhante<passageiro.acompanhante(); acompanhante++){
                    celula.addElement(PdfUtil.paragrafoNegrito("*ACOMP"));
                }
            });
            if(i<viagems.size()-1){
                int qtdPassageiros = viagem.passageiros().stream().mapToInt(PassageiroResponseDto::acompanhante).sum() + viagem.passageiros().size();
                var viagemComparativa = viagems.get(i + i%2==0?1:-1);
                var qtdPassageiroComparativo = viagemComparativa
                        .passageiros().stream().mapToInt(PassageiroResponseDto::acompanhante).sum() + viagemComparativa.passageiros().size();

                int resultado = qtdPassageiros - qtdPassageiroComparativo;

                while(resultado<0){
                    celula.addElement(PdfUtil.paragrafoNegrito("\n"));
                    resultado++;
                }
            }
            celula.addElement(PdfUtil.paragrafoNegritoNormal("\nFrete: ", "Sim (  ) Não (  )"));

            celula.addElement(PdfUtil.paragrafoVerticalAzul("\nMotorista:____________", viagem.destino()));
            celula.addElement(PdfUtil.paragrafoVerticalVermelho("Carro:____________", viagem.hora()));
            table.addCell(celula);

        }
        if(viagems.size() % 2 != 0){
            var celula = PdfUtil.criarCelula();
            celula.addElement(new Paragraph("\n"));
            table.addCell(celula);
        }
        return table;
    }
}