package med.voll.api.domain.consulta;


import java.time.LocalDateTime;

public record DadosDetalhamentoConsulta(Long id, Long medicoId, Long pacienteId, LocalDateTime data) {
}
