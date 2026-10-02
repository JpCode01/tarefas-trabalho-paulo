package com.faculdade.tarefas.model;

import com.faculdade.tarefas.memento.MementoTarefa;
import jakarta.persistence.*;

/**
 * MODEL (o "M" do MVC): a tarefa, que é salva no banco.
 *
 * Esta classe participa de DOIS padrões:
 *  - BUILDER: a classe interna "Tarefa.Builder" (no final do arquivo) cria tarefas.
 *  - MEMENTO: a Tarefa é o ORIGINATOR (criarMemento e restaurar).
 */
@Entity
@Table(name = "tarefas")
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String descricao;

    @Enumerated(EnumType.STRING)
    private Prioridade prioridade;

    @Enumerated(EnumType.STRING)
    private Status status;

    // Exigido pelo JPA/Jackson. Para criar tarefas no nosso código, usamos o Builder.
    protected Tarefa() {
    }

    // BUILDER: construtor privado. Só o Builder consegue criar uma Tarefa.
    private Tarefa(Builder builder) {
        this.titulo = builder.titulo;
        this.descricao = builder.descricao;
        this.prioridade = builder.prioridade;
        this.status = builder.status;
    }

    // ===================== MEMENTO (Originator) =====================

    /** A própria Tarefa cria o memento: uma "foto" do estado atual. */
    public MementoTarefa criarMemento() {
        return new MementoTarefa(titulo, descricao, prioridade, status);
    }

    /** A própria Tarefa se restaura a partir do memento. */
    public void restaurar(MementoTarefa memento) {
        this.titulo = memento.getTitulo();
        this.descricao = memento.getDescricao();
        this.prioridade = memento.getPrioridade();
        this.status = memento.getStatus();
    }

    // ===================== Getters e setters =====================

    public Long getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDescricao() { return descricao; }
    public Prioridade getPrioridade() { return prioridade; }
    public Status getStatus() { return status; }

    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setPrioridade(Prioridade prioridade) { this.prioridade = prioridade; }
    public void setStatus(Status status) { this.status = status; }

    // ===================== BUILDER =====================

    /**
     * BUILDER: monta a Tarefa passo a passo.
     *
     * Problema que resolve: um construtor com vários parâmetros, como
     * new Tarefa("Estudar", "Builder", ALTA, PENDENTE), é confuso: o que é cada valor?
     * Com o Builder, cada valor tem um nome.
     *
     * Papel do Builder: guardar os valores informados e criar a Tarefa no final.
     */
    public static class Builder {

        private String titulo;
        private String descricao;
        private Prioridade prioridade;
        private Status status;

        public Builder titulo(String titulo) {
            this.titulo = titulo;
            return this; // devolve o próprio Builder para encadear as chamadas
        }

        public Builder descricao(String descricao) {
            this.descricao = descricao;
            return this;
        }

        public Builder prioridade(Prioridade prioridade) {
            this.prioridade = prioridade;
            return this;
        }

        public Builder status(Status status) {
            this.status = status;
            return this;
        }

        /**
         * build(): valida, aplica valores padrão e cria a Tarefa.
         * É o último passo: antes dele, a Tarefa ainda não existe.
         */
        public Tarefa build() {
            if (titulo == null || titulo.isBlank()) {
                throw new IllegalArgumentException("O título da tarefa é obrigatório");
            }
            if (descricao == null) descricao = "";
            if (prioridade == null) prioridade = Prioridade.MEDIA;
            if (status == null) status = Status.PENDENTE;
            return new Tarefa(this);
        }
    }
}
