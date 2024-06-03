package med.voll.api.domain.consulta;

import jakarta.servlet.http.PushBuilder;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.PacienteRepository;
import med.voll.api.infra.exception.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AgendaDeConsultasService {
    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    public void agendar(DadosAgendamentoConsulta dados) throws Exception {
        if (!pacienteRepository.existsById(dados.pacienteId())) {
            throw new ValidacaoException("Id do paciente informado não existe");
        }
        if (dados.medicoId() != null && !medicoRepository.existsById(dados.medicoId())) {
            throw new ValidacaoException("Id do médico informado não existe");
        }

        var paciente = pacienteRepository.getReferenceById(dados.pacienteId());
        var medico = escolherMedico(dados);
        var consulta = new Consulta(null, medico, paciente,null, dados.data());

        consultaRepository.save(consulta);
    }
    public void cancelar(DadosCancelamentoConsulta dados){
        if(!consultaRepository.existsById(dados.consultaId())){
            throw new ValidacaoException("id da consulta informada não existe");
        }
        var consulta = consultaRepository.getReferenceById(dados.consultaId());
        consulta.cancelar(dados.motivo());
    }


    private Medico escolherMedico(DadosAgendamentoConsulta dados){
        if (dados.medicoId() != null){
            return medicoRepository.getReferenceById(dados.medicoId());
        }
        if(dados.especialidade() == null){
            throw new ValidacaoException("Especialidade é obrigatória quando médico não é escolhido");
        }
        return medicoRepository.escolherMedicoAleatorioLivreNaData(dados.especialidade(), dados.data());

    }
}
