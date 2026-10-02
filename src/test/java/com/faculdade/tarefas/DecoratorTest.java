package com.faculdade.tarefas;

import com.faculdade.tarefas.decorator.DecoratorEmail;
import com.faculdade.tarefas.decorator.DecoratorLog;
import com.faculdade.tarefas.decorator.OperacaoBasica;
import com.faculdade.tarefas.decorator.OperacaoTarefa;
import com.faculdade.tarefas.model.Tarefa;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** DECORATOR: cada decorator mantém o comportamento original e soma o seu. */
class DecoratorTest {

    private final Tarefa tarefa = new Tarefa.Builder().titulo("Estudar Java").build();

    @Test
    void operacaoBasicaFazSoOBasico() {
        OperacaoTarefa operacao = new OperacaoBasica();

        String resultado = operacao.executar(tarefa);

        assertTrue(resultado.contains("executada"));
        assertFalse(resultado.contains("E-mail"));
    }

    @Test
    void decoratorMantemOOriginalEAdicionaOSeu() {
        OperacaoTarefa operacao = new DecoratorEmail(new OperacaoBasica());

        String resultado = operacao.executar(tarefa);

        assertTrue(resultado.contains("executada"));      // original continua
        assertTrue(resultado.contains("E-mail enviado")); // comportamento novo
    }

    @Test
    void decoratorsPodemSerCombinados() {
        OperacaoTarefa operacao =
                new DecoratorLog(
                        new DecoratorEmail(
                                new OperacaoBasica()));

        String resultado = operacao.executar(tarefa);

        assertTrue(resultado.contains("executada"));
        assertTrue(resultado.contains("E-mail enviado"));
        assertTrue(resultado.contains("Log registrado"));
    }
}
