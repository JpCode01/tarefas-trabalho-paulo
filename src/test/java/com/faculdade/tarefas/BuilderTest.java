package com.faculdade.tarefas;

import com.faculdade.tarefas.model.Prioridade;
import com.faculdade.tarefas.model.Status;
import com.faculdade.tarefas.model.Tarefa;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** BUILDER: criar a tarefa passo a passo e validar no build(). */
class BuilderTest {

    @Test
    void criaTarefaComOsValoresInformados() {
        Tarefa tarefa = new Tarefa.Builder()
                .titulo("Estudar Design Patterns")
                .descricao("Estudar Builder, Decorator e Memento")
                .prioridade(Prioridade.ALTA)
                .status(Status.PENDENTE)
                .build();

        assertEquals("Estudar Design Patterns", tarefa.getTitulo());
        assertEquals("Estudar Builder, Decorator e Memento", tarefa.getDescricao());
        assertEquals(Prioridade.ALTA, tarefa.getPrioridade());
        assertEquals(Status.PENDENTE, tarefa.getStatus());
    }

    @Test
    void usaValoresPadraoQuandoNaoInformados() {
        Tarefa tarefa = new Tarefa.Builder().titulo("Só o título").build();

        assertEquals(Prioridade.MEDIA, tarefa.getPrioridade());
        assertEquals(Status.PENDENTE, tarefa.getStatus());
    }

    @Test
    void buildFalhaSemTitulo() {
        assertThrows(IllegalArgumentException.class, () -> new Tarefa.Builder().build());
    }
}
