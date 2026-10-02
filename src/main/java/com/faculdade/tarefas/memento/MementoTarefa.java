package com.faculdade.tarefas.memento;

import com.faculdade.tarefas.model.Prioridade;
import com.faculdade.tarefas.model.Status;

/**
 * MEMENTO: guarda uma cópia do estado da Tarefa em um momento.
 *
 * Estado salvo: título, descrição, prioridade e status.
 * Não tem setters: depois de criado, não pode ser alterado.
 */
public class MementoTarefa {

    private final String titulo;
    private final String descricao;
    private final Prioridade prioridade;
    private final Status status;

    public MementoTarefa(String titulo, String descricao, Prioridade prioridade, Status status) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.prioridade = prioridade;
        this.status = status;
    }

    public String getTitulo() { return titulo; }
    public String getDescricao() { return descricao; }
    public Prioridade getPrioridade() { return prioridade; }
    public Status getStatus() { return status; }
}
