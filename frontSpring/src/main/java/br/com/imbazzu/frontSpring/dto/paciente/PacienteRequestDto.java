package br.com.imbazzu.frontSpring.dto.paciente;

import br.com.imbazzu.frontSpring.PessoaTipoEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

public record PacienteRequestDto(

        @NotBlank(message = "O nome é obrigatório")
        @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
                String nome,

        @NotBlank(message = "O CPF é obrigatório")
        @CPF(message = "CPF inválido")
        String cpf,

        @NotBlank(message = "O telefone é obrigatório")
        @Pattern(regexp = "^\\(\\d{2}\\)9\\d{4}-\\d{4}$", message = "Formato de telefone inválido. Use (DD)9XXXX-XXXX")
        String telefone,

        @NotBlank(message = "O endereço é obrigatório")
        @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
        String endereco,

        @NotNull(message = "O tipo de passageiro é obrigatório")
        PessoaTipoEnum tipo
) {}
