package br.com.imbazzu.secretariaTransporte.compartilhados.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;

public class PdfUtil {

    public static PdfPCell criarCelula(){
        var celula = new PdfPCell();
        celula.setBorderWidth(2f);
        return celula;
    }

    public static Paragraph paragrafoNegrito(String texto){
        return new Paragraph(fraseNegrita(texto));
    }

    public static Paragraph paragrafoNegritoNormal(String negrito, String normal){
        var frase = fraseNegrita(negrito);
        frase.add(fraseNormal(normal));
        return new Paragraph(frase);
    }

    public static Paragraph paragrafoVerticalVermelho(String textoPreta, String textoVermelho) {
        return paragrafoVertical(fraseNegrita(textoPreta),fraseVermelha(textoVermelho));
    }

    public static Paragraph paragrafoVerticalAzul(String textoPreta, String textoAzul) {
        return paragrafoVertical(fraseNegrita(textoPreta), fraseAzul(textoAzul));
    }

    public static Paragraph paragrafoVertical(Phrase frasePrincipal, Phrase fraseSecundaria){
        frasePrincipal.add(new Chunk(new VerticalPositionMark()));
        frasePrincipal.add(fraseSecundaria);
        return new Paragraph(frasePrincipal);
    }

    public static Paragraph paragrafoCentralizadoVerde(String texto){
        var paragrafo = new Paragraph(fraseVerde(texto));
        paragrafo.setAlignment(Element.ALIGN_CENTER);
        return paragrafo;
    }

    public static Phrase fraseNormal(String texto){
        var fonte = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK);
        return new Phrase(texto, fonte);
    }

    public static Phrase fraseNegrita(String frase) {
        var fonte = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.BLACK);
        return new Phrase(frase, fonte);
    }

    public static Phrase fraseVermelha(String frase) {
        var fonte =  new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.RED);
        return new Phrase(frase, fonte);
    }

    public static Phrase fraseAzul(String frase) {
        var fonte = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.BLUE);
        return new Phrase(frase, fonte);
    }

    public static Phrase fraseVerde(String frase) {
        var fonte = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD,new BaseColor(0x18, 0x5c, 0x37) );
        return new Phrase(frase, fonte);
    }
}
