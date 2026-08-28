package br.com.imbazzu.frontSpring.controller;

import br.com.imbazzu.frontSpring.dto.destino.DestinoResponseDto;
import br.com.imbazzu.frontSpring.dto.destino.DestinoFormDto;
import br.com.imbazzu.frontSpring.dto.destino.DestinoRequestDto;
import br.com.imbazzu.frontSpring.service.DestinoService;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxReswap;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/destino")
@RequiredArgsConstructor
public class DestinoController {

    private final DestinoService service;

    @GetMapping()
    public String home() {
        return "destinos/page";
    }

    /**
     * Listagem + busca
     */
    @GetMapping("/list")
    public String listar(
            @RequestParam(name = "q", defaultValue = "") String q,
            Model model) {
        List<DestinoResponseDto> destinos = service.buscarDestinos(q);
        model.addAttribute("destinos", destinos);
        model.addAttribute("q", q);
        return "destinos/list :: list";
    }

    /**
     * Formulário de novo paciente
     */
    @GetMapping("/form")
    public String formNovo(Model model) {
        model.addAttribute("nome", "");
        model.addAttribute("percurso", "00:00");
        model.addAttribute("id", null);
        model.addAttribute("edicao", false);
        return "destinos/form";
    }

    /**
     * Formulário de edição
     */
    @GetMapping("/{id}/form")
    public String formEdicao(@PathVariable Long id, Model model) {
        var destino = service.buscarPorId(id);

        model.addAttribute("nome", destino.nome());
        model.addAttribute("percurso", destino.tempo());
        model.addAttribute("id", id);
        model.addAttribute("edicao", true);

        return "destinos/form";
    }

    /**
     * Criação / edição de paciente
     */
    @PostMapping
    public ResponseEntity<Void> salvar(@ModelAttribute DestinoFormDto dto, HtmxResponse response, Model model) {
        LocalTime percurso = LocalTime.parse(dto.tempoPercurso(), DateTimeFormatter.ofPattern("HH:mm"));
        service.criar(new DestinoRequestDto(dto.nome(), percurso.getHour(), percurso.getMinute()));
        response.addTrigger("destinoAtualizado");
        response.addTrigger("fechar-formulario");
        return ResponseEntity.ok().build();

    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizar(
            @PathVariable Long id,
            @ModelAttribute DestinoFormDto dto) {

        LocalTime percurso = LocalTime.parse(dto.tempoPercurso(), DateTimeFormatter.ofPattern("HH:mm"));
        service.atualizar(id, new DestinoRequestDto(dto.nome(), percurso.getHour(), percurso.getMinute()));

        return ResponseEntity
                .noContent()
                .header("HX-Trigger", "destinoAtualizado")
                .build();
    }

    /**
     * Exclusão
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity
                .noContent()
                .header("HX-Trigger", "destinoAtualizado")
                .build();
    }

}
