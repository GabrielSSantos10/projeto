package br.edu.ifg.luziania.model.bo.GerenciamentoDeSenhas;

import br.edu.ifg.luziania.model.dao.PacienteDAO;
import br.edu.ifg.luziania.model.entity.GerenciamentoDeSenhas.Paciente;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.LinkedList;
import java.util.Queue;

@ApplicationScoped
public class AtendimentoBO {
    @Inject
    PacienteDAO pacienteDAO;

    private final Queue<Paciente> filaPacientes = new LinkedList<>();
    private Paciente pacienteAtual = null; // <- aqui está o controle do paciente chamado

    public void realizarAtendimento(String nome, String cpf) {
        Paciente paciente = new Paciente(nome, cpf);
        paciente.setNome(nome);
        paciente.setCpf(cpf);
        pacienteDAO.salvar(paciente);

        filaPacientes.add(paciente);
    }

    public Paciente chamarPaciente() {
        pacienteAtual = filaPacientes.poll(); // guarda quem foi chamado
        return pacienteAtual;
    }

    public boolean isFilaVazia() {
        return filaPacientes.isEmpty();
    }

    public void marcarPacienteComoAusente() {
        pacienteAtual = null; // apenas "descarta" o atual
        // Não mexe na fila!
    }
}

