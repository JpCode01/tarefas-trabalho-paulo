package com.faculdade.tarefas;

import com.faculdade.tarefas.memento.HistoricoTarefa;
import com.faculdade.tarefas.model.Tarefa;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** MEMENTO: estado inicial -> salvar -> alterar -> restaurar -> estado inicial. */
class MementoTest {

    @Test
    void restauraOEstadoSalvo() {
        Long idTarefa = 1L;
        HistoricoTarefa historico = new HistoricoTarefa();  // CARETAKER
        Tarefa tarefa = new Tarefa.Builder()                // ORIGINATOR
                .titulo("Estudar Java")
                .descricao("Estudar Builder")
                .build();

        // 1) salvar o estado inicial
        historico.guardar(idTarefa, tarefa.criarMemento()); // Tarefa cria, Histórico guarda

        // 2) alterar
        tarefa.setDescricao("Estudar Builder e Decorator");
        assertEquals("Estudar Builder e Decorator", tarefa.getDescricao());

        // 3) restaurar
        tarefa.restaurar(historico.recuperarUltimo(idTarefa)); // Histórico entrega, Tarefa restaura

        // 4) voltou ao estado inicial
        assertEquals("Estudar Builder", tarefa.getDescricao());
    }

    @Test
    void semVersaoSalvaNaoHaNadaParaRestaurar() {
        assertNull(new HistoricoTarefa().recuperarUltimo(99L));
    }
}
