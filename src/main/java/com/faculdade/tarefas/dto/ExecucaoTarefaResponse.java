package com.faculdade.tarefas.dto;

import com.faculdade.tarefas.model.Tarefa;

public record ExecucaoTarefaResponse(
        String resultado,
        Tarefa tarefa) {
}
