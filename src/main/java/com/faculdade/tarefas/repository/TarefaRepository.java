package com.faculdade.tarefas.repository;

import com.faculdade.tarefas.model.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * REPOSITORY: cuida só do banco de dados (salvar, buscar, excluir).
 * O Spring Data JPA cria a implementação sozinho: não precisamos escrever SQL.
 */
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
}
