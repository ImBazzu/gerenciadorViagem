package br.com.imbazzu.frontSpring.controller;

import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.imbazzu.frontSpring.dto.viagem.ViagemRequestDto;
import br.com.imbazzu.frontSpring.service.PacienteService;
import br.com.imbazzu.frontSpring.service.DestinoService;
import br.com.imbazzu.frontSpring.service.MotoristaService;
import br.com.imbazzu.frontSpring.service.ViagemService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/viagem")
@RequiredArgsConstructor
public class ViagemController {

    private final ViagemService service;

    private final PacienteService pacienteService;

    private final DestinoService destinoService;

    private final MotoristaService motoristaService;

    @GetMapping()
    public String buscarPorData(
            @RequestParam("data") LocalDate data,
            Model model) {

        var resultado = service.buscarViagem(data);
        model.addAttribute("data", data);
        model.addAttribute("viagensAntes5", resultado.antesDas0530());
        model.addAttribute("viagensDepois5", resultado.depoisDas0530());

        return "viagens/index";
    }

    @PostMapping("/salvar")
    @ResponseBody
    public ResponseEntity<Void> salvar(@RequestBody ViagemRequestDto dto) {
        service.salvar(dto);
        return ResponseEntity.ok().header("HX-Redirect", "/")
                .build();
    }

    @GetMapping("/{id}")
    public String buscarPorId(
            @PathVariable Long id,
            Model model) {

        model.addAttribute("viagem", service.buscarDtoPorId(id));
        model.addAttribute("oob", false);
        return "viagens/viagem :: viagemCard";
    }

    @PutMapping("/{origemId}/{passageiroId}/{destinoId}")
    public String moverPassageiro(
            @PathVariable Long origemId,
            @PathVariable Long passageiroId,
            @PathVariable Long destinoId,
            Model model) {

        service.moverPassageiro(destinoId, passageiroId);

        model.addAttribute("origem", service.buscarDtoPorId(origemId));
        model.addAttribute("destino", service.buscarDtoPorId(destinoId));

        return "viagens/atualizarviagem :: atualizarDuasViagens";
    }

    /**
     * Abre o modal de nova viagem.
     * HTMX injeta o fragmento no #modal-container.
     *
     * No index.html:
     * hx-get="/viagem/form"
     * hx-target="#modal-container"
     * hx-swap="innerHTML"
     */
    @GetMapping("/form")
    public String formNovo(Model model, @RequestParam(name = "data", required = true) LocalDate data) {
        model.addAttribute("data", data);
        return "viagens/formulario/form :: formModal";
    }

    /**
     * Retorna um card de passageiro para append dinâmico via HTMX.
     *
     * No form.html (botão "Adicionar passageiro"):
     * hx-get="/viagem/form/passageiro?index=N"
     * hx-target="#lista-passageiros"
     * hx-swap="beforeend"
     */
    @GetMapping("/form/passageiro")
    public String formPassageiro(
            @RequestParam int index,
            Model model) {

        model.addAttribute("index", index);
        return "viagens/formulario/form-passageiro :: passageiroCard";
    }



    @GetMapping("/form/destino")
    public String buscarDestinoItem(Model model, @RequestParam(defaultValue = "") String q) {
        model.addAttribute("destinos", destinoService.buscarDestinos(q));
        return "viagens/formulario/form-destino :: destinoItem";
    }

    @GetMapping("/form/motorista")
    public String buscarMotoristaItem(Model model, @RequestParam(defaultValue = "") String q) {
        var motoristas = motoristaService.buscarMotoristas(q);
        model.addAttribute("motoristas", motoristas);
        return "viagens/formulario/form-motorista :: motoristaItem";
    }
}