package br.com.imbazzu.frontSpring.controller;

import br.com.imbazzu.frontSpring.PessoaTipoEnum;
import br.com.imbazzu.frontSpring.dto.SelectOption;
import br.com.imbazzu.frontSpring.dto.paciente.PacienteRequestDto; // Sugestão posterior: Renomear para PassageiroRequestDto
import br.com.imbazzu.frontSpring.dto.paciente.PacienteResponseDto;
import br.com.imbazzu.frontSpring.exception.FormularioError;
import br.com.imbazzu.frontSpring.exception.RegraNegocioException;
import br.com.imbazzu.frontSpring.service.PacienteService;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/passageiro")
@RequiredArgsConstructor
public class PassageiroController {

    private final PacienteService pacienteService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("tiposPassageiro", tiposPassageiro());
        return "passageiro/index";
    }

    @GetMapping("/form")
    public String formNovo(Model model) {
        model.addAttribute("pessoa", new PacienteRequestDto("", "", "", "", null));
        model.addAttribute("edicao", false);
        model.addAttribute("tiposPassageiro", tiposPassageiro());
        return "passageiro/form :: passageiroForm";
    }

    @GetMapping("/{id}/form")
    public String formEditar(@PathVariable String id, Model model) {
        PacienteResponseDto response = pacienteService.buscarPacientePorId(id);
        PacienteRequestDto pessoa = new PacienteRequestDto(
                response.nome(), response.cpf(), response.telefone(), response.endereco(), response.tipo()
        );

        model.addAttribute("pessoa", pessoa);
        model.addAttribute("edicao", true);
        model.addAttribute("tiposPassageiro", tiposPassageiro());
        model.addAttribute("id", id);
        return "passageiro/form :: passageiroForm";
    }

    @PostMapping
    public String salvar(
            @Valid @ModelAttribute("pessoa") PacienteRequestDto dto,
            BindingResult bindingResult,
            Model model,
            HttpServletResponse response,
            HtmxResponse htmxResponse) {

        if (bindingResult.hasErrors()) {
            return redigirFormularioComErros(dto,  model, response, false);
        }

        try {
            pacienteService.criar(dto);
            htmxResponse.addTrigger("passengerUpdated");
            response.setStatus(HttpServletResponse.SC_OK);
            return ""; // Retorna resposta vazia para fechar o modal HTMX
        } catch (Exception e) {
            bindingResult.reject("erro.negocio", e.getMessage());
            return redigirFormularioComErros(dto, model, response, false);
        }
    }

    @PutMapping("/{id}")
    public String atualizar(
            @PathVariable String id,
            @Valid @ModelAttribute("pessoa") PacienteRequestDto dto,
            BindingResult bindingResult,
            Model model,
            HttpServletResponse response,
            HtmxResponse htmxResponse) {

        if (bindingResult.hasErrors()) {
            return redigirFormularioComErros(dto,  model, response, true);
        }

        try {
            pacienteService.atualizar(id, dto);
            htmxResponse.addTrigger("passengerUpdated");
            return "";
        } catch (Exception e) {
            bindingResult.reject("erro.negocio", e.getMessage());
            return redigirFormularioComErros(dto, model, response, false);
        }
    }

    @GetMapping("/lista")
    public String listar(
            @RequestParam(name = "q", defaultValue = "") String q,
            @RequestParam(name = "tipo", required = false) String tipo,
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model) {

        var buscaPaginada = pacienteService.buscarPacientes(q, tipo, page);
        model.addAttribute("passageiros", buscaPaginada);
        return "passageiro/list :: list";
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public void delete(@PathVariable String id, HtmxResponse htmxResponse) {
        pacienteService.excluir(id);
        htmxResponse.addTrigger("passengerUpdated");
    }

    private List<SelectOption> tiposPassageiro() {
        return Arrays.stream(PessoaTipoEnum.values())
                .map(t -> new SelectOption(t.name(), t.getLabel()))
                .toList();
    }

    private String redigirFormularioComErros(
            PacienteRequestDto dto, Model model,
            HttpServletResponse response, boolean edicao) {
        response.setStatus(422);
        model.addAttribute("edicao", edicao);
        model.addAttribute("pessoa", dto);
        model.addAttribute("tiposPassageiro", tiposPassageiro());
        return "passageiro/form :: passageiroConteudo";
    }
}