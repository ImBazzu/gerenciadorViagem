package br.com.imbazzu.frontSpring.controller;

import br.com.imbazzu.frontSpring.dto.motorista.MotoristaResponseDto;
import br.com.imbazzu.frontSpring.dto.motorista.MotoristaRequestDto;
import br.com.imbazzu.frontSpring.service.MotoristaService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Controller
@RequestMapping("/motorista")
@RequiredArgsConstructor
public class MotoristaController {

    private final MotoristaService service;

    @GetMapping()
    public String home() {
        return "motoristas/page";
    }

    /**
     * Listagem + busca
     */
    @GetMapping("/list")
    public String listar(
            @RequestParam(name = "q", defaultValue = "") String q,
            Model model) {
        List<MotoristaResponseDto> pacientes = service.buscarMotoristas(q);
        model.addAttribute("motoristas", pacientes);
        model.addAttribute("q", q);
        return "motoristas/list :: list";
    }

    /**
     * Formulário de novo paciente
     */
    @GetMapping("/form")
    public String formNovo(Model model) {
        model.addAttribute("motorista", new MotoristaRequestDto("", ""));
        model.addAttribute("edicao", false);
        model.addAttribute("id", null);
        return "motoristas/form";
    }

    /**
     * Formulário de edição
     */
    @GetMapping("/{id}/form")
    public String formEdicao(@PathVariable Long id, Model model) {
        var motorista = service.buscarPacientePorId(id);

        model.addAttribute("motorista", motorista);
        model.addAttribute("edicao", true);

        return "motoristas/form :: form";
    }

    /**
     * Criação / edição de paciente
     */
    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> salvar(@ModelAttribute MotoristaRequestDto dto) {
        service.criar(dto);
        return ResponseEntity
                .noContent()
                .header("HX-Trigger", "motoristaAtualizado", "fechar-modal")
                .build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizar(
            @PathVariable Long id,
            @ModelAttribute MotoristaRequestDto dto) {

        service.atualizar(id, dto);

        return ResponseEntity
                .noContent()
                .header("HX-Trigger", "motoristaAtualizado")
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
                .header("HX-Trigger", "motoristaAtualizado")
                .build();
    }
}
