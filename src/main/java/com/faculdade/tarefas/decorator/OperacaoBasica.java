package com.faculdade.tarefas.decorator;

import com.faculdade.tarefas.model.Tarefa;

/**
 * DECORATOR - OBJETO ORIGINAL.
 * Faz só o comportamento básico e não conhece os decorators.
 */
public class OperacaoBasica implements OperacaoTarefa {

    @Override
    public String executar(Tarefa tarefa) {
        return "Tarefa '" + tarefa.getTitulo() + "' executada";
    }
}
