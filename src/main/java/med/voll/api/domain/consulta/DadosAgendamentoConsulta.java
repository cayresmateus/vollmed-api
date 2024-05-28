package med.voll.api.domain.consulta;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record DadosAgendamentoConsulta(Long medicoId,

                                       @NotNull
                                       Long pacienteId,

                                       @NotNull @Future @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
                                       LocalDateTime data) {
}
