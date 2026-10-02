package com.faculdade.tarefas.memento;

import org.springframework.stereotype.Component;

import java.util.*;

/**
 * CARETAKER: apenas GUARDA os mementos. Não sabe o que tem dentro deles.
 *
 * O histórico fica em memória: um Map onde a chave é o id da tarefa
 * e o valor é uma pilha de versões (a última salva é a primeira a sair).
 */
@Component
public class HistoricoTarefa {
    //   nome:  João
    // (chave: valor)
    // (idTarefa: Stack<MementoTarefa>)

    private final Map<Long, Deque<MementoTarefa>> versoes = new HashMap<>();

    /** Guarda uma versão da tarefa. */
    public void guardar(Long idTarefa, MementoTarefa memento) {
        versoes.computeIfAbsent(idTarefa, id -> new ArrayDeque<>()).push(memento);
    }

    /** Devolve a última versão guardada (e a remove). Devolve null se não houver. */
    public MementoTarefa recuperarUltimo(Long idTarefa) {
        Deque<MementoTarefa> pilha = versoes.get(idTarefa);
        if (pilha == null || pilha.isEmpty()) {
            return null;
        }
        return pilha.pop();
    }

    /** Apaga as versões de uma tarefa excluída. */
    public void limpar(Long idTarefa) {
        versoes.remove(idTarefa);
    }
}
