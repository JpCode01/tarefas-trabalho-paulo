package com.faculdade.tarefas.decorator;

import com.faculdade.tarefas.model.Tarefa;

/**
 * DECORATOR - interface comum.
 * O objeto original e todos os decorators implementam esta interface.
 * Por isso eles podem ser combinados.
 */
public interface OperacaoTarefa {

    String executar(Tarefa tarefa);
}
