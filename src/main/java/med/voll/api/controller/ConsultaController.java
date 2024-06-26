package med.voll.api.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import med.voll.api.domain.consulta.AgendaDeConsultasService;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.domain.consulta.DadosCancelamentoConsulta;
import med.voll.api.domain.consulta.DadosDetalhamentoConsulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("consultas")
@SecurityRequirement(name = "bearer-key")
public class ConsultaController {
    @Autowired
    private AgendaDeConsultasService consultaService;

    @PostMapping
    public ResponseEntity<DadosDetalhamentoConsulta> agendar(@RequestBody @Valid DadosAgendamentoConsulta dados) throws Exception {
        var dto = consultaService.agendar(dados);
        return ResponseEntity.ok(dto);
    }
    @DeleteMapping
    public  ResponseEntity<DadosCancelamentoConsulta> cancelar(@RequestBody @Valid DadosCancelamentoConsulta dados){
        this.consultaService.cancelar(dados);
        return ResponseEntity.ok().build();
    }
}
