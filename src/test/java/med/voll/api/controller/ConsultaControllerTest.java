package med.voll.api.controller;

import med.voll.api.domain.consulta.AgendaDeConsultasService;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.domain.consulta.DadosDetalhamentoConsulta;
import med.voll.api.domain.medico.Especialidade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class ConsultaControllerTest {

    @Autowired
    private JacksonTester<DadosAgendamentoConsulta> dadosAgendamentoConsultaJson;

    @Autowired
    private JacksonTester<DadosDetalhamentoConsulta> dadosDetalhamentoConsultaJson;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AgendaDeConsultasService agendaDeConsultas;

    @Test
    @DisplayName("deveria devolver codigo http 400 quando informacoes estao invalidos")
    @WithMockUser
    void agendar_cenario1() throws Exception {
        var res = mvc.perform(post("/consultas"))
                .andReturn().getResponse();

        assertThat(res.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("deveria devolver codigo http 200 quando informacoes estao corretas")
    @WithMockUser
    void agendar_cenario2() throws Exception {
        var data = LocalDateTime.now().plusHours(1);
        var dadosDetalhamento = new DadosDetalhamentoConsulta(null, 2L, 2L, data);
        when(agendaDeConsultas.agendar(any())).thenReturn(dadosDetalhamento);

        Especialidade especialidade = Especialidade.CARDIOLOGIA;
        var res = mvc.perform(post("/consultas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(dadosAgendamentoConsultaJson.write(
                                new DadosAgendamentoConsulta(2L, 2L , data, especialidade)
                        ).getJson()))
                )
                .andReturn().getResponse();

        assertThat(res.getStatus()).isEqualTo(HttpStatus.OK.value());

        var jsonEsperado = dadosDetalhamentoConsultaJson.write(
                dadosDetalhamento
        ).getJson();

        assertThat(res.getContentAsString()).isEqualTo(jsonEsperado);

    }
}