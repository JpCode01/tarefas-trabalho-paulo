package com.faculdade.tarefas.service;

import com.faculdade.tarefas.decorator.DecoratorEmail;
import com.faculdade.tarefas.decorator.DecoratorLog;
import com.faculdade.tarefas.decorator.OperacaoBasica;
import com.faculdade.tarefas.decorator.OperacaoTarefa;
import com.faculdade.tarefas.memento.HistoricoTarefa;
import com.faculdade.tarefas.memento.MementoTarefa;
import com.faculdade.tarefas.model.Tarefa;
import com.faculdade.tarefas.repository.TarefaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * SERVICE: contém as regras da aplicação. É aqui que os 3 padrões são USADOS:
 *  - criar()                         -> BUILDER
 *  - salvarVersao() / restaurarVersao() -> MEMENTO
 *  - executar()                      -> DECORATOR
 */
@Service
public class TarefaService {

    private final TarefaRepository repository;
    private final HistoricoTarefa historico; // CARETAKER do Memento

    public TarefaService(TarefaRepository repository, HistoricoTarefa historico) {
        this.repository = repository;
        this.historico = historico;
    }

    // ===================== CRUD =====================

    /** BUILDER: monta a tarefa com os dados recebidos. O build() valida. */
    public Tarefa criar(Tarefa dados) {
        try {
            Tarefa tarefa = new Tarefa.Builder()
                    .titulo(dados.getTitulo())
                    .descricao(dados.getDescricao())
                    .prioridade(dados.getPrioridade())
                    .status(dados.getStatus())
                    .build();

            return repository.save(tarefa);
        } catch (IllegalArgumentException erro) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, erro.getMessage());
        }
    }

    public List<Tarefa> listar() {
        return repository.findAll();
    }

    public Tarefa buscar(Long id) {
        return repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarefa não encontrada: id=" + id));
    }

    public Tarefa atualizar(Long id, Tarefa dados) {
        Tarefa tarefa = buscar(id);
        tarefa.setTitulo(dados.getTitulo());
        tarefa.setDescricao(dados.getDescricao());
        if (dados.getPrioridade() != null) tarefa.setPrioridade(dados.getPrioridade());
        if (dados.getStatus() != null) tarefa.setStatus(dados.getStatus());
        return repository.save(tarefa);
    }

    public void excluir(Long id) {
        buscar(id); // lança erro 404 se não existir
        repository.deleteById(id);
        historico.limpar(id);
    }

    // ===================== MEMENTO =====================

    /** 1) A Tarefa (originator) cria o memento. 2) O Histórico (caretaker) guarda. */
    public Tarefa salvarVersao(Long id) {
        Tarefa tarefa = buscar(id);
        MementoTarefa memento = tarefa.criarMemento();
        historico.guardar(id, memento);
        return tarefa;
    }

    /** 1) O Histórico entrega o último memento. 2) A Tarefa se restaura com ele. */
    public Tarefa restaurarVersao(Long id) {
        Tarefa tarefa = buscar(id);
        MementoTarefa memento = historico.recuperarUltimo(id);
        if (memento == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não há versão salva para restaurar");
        }
        tarefa.restaurar(memento);
        return repository.save(tarefa);
    }

    // ===================== DECORATOR =====================

    /** Começa com a operação original e "embrulha" com os decorators pedidos. */
    public String executar(Long id, boolean email, boolean log) {
        Tarefa tarefa = buscar(id);

        OperacaoTarefa operacao = new OperacaoBasica();   // objeto original
        if (email) {
            operacao = new DecoratorEmail(operacao);      // embrulha com e-mail
        }
        if (log) {
            operacao = new DecoratorLog(operacao);        // embrulha com log
        }
        return operacao.executar(tarefa);
    }
}
