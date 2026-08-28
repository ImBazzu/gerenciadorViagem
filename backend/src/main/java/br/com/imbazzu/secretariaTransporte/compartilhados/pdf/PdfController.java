package br.com.imbazzu.secretariaTransporte.compartilhados.pdf;

import br.com.imbazzu.secretariaTransporte.compartilhados.util.DataHoraUtil;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
public class PdfController {

    private final PdfService service;


    @PermitAll
    @GetMapping("/{data}")
    public ResponseEntity<byte[]> gerarPdfViagemDoDIa (@PathVariable LocalDate data){
        try {

            byte[] pdfCarro = service.gerarPdf(data);

            HttpHeaders cabecalho = new HttpHeaders();
            cabecalho.setContentType(MediaType.APPLICATION_PDF);
            cabecalho.setContentDisposition(ContentDisposition.inline()
                    .filename("Viagem-do-dia-" + DataHoraUtil.dataParaTexto(data))
                    .build());

            return ResponseEntity.ok().headers(cabecalho).body(pdfCarro);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
