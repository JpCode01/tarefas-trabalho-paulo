package com.faculdade.tarefas.controller;

import com.faculdade.tarefas.model.Tarefa;
import com.faculdade.tarefas.service.TarefaService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * CONTROLLER: cuida só da entrada e saída HTTP.
 * Recebe a requisição, chama o Service e devolve a resposta. Sem regra de negócio.
 */
@RestController
@RequestMapping("/tarefas")
public class TarefaController {

    private final TarefaService service;

    public TarefaController(TarefaService service) {
        this.service = service;
    }

    // ===================== CRUD =====================

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Tarefa criar(@RequestBody Tarefa dados) {
        return service.criar(dados);
    }

    @GetMapping
    public List<Tarefa> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Tarefa buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PutMapping("/{id}")
    public Tarefa atualizar(@PathVariable Long id, @RequestBody Tarefa dados) {
        return service.atualizar(id, dados);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        service.excluir(id);
    }

    // ===================== MEMENTO =====================

    @PostMapping("/{id}/salvar")
    public Tarefa salvarVersao(@PathVariable Long id) {
        return service.salvarVersao(id);
    }

    @PostMapping("/{id}/restaurar")
    public Tarefa restaurarVersao(@PathVariable Long id) {
        return service.restaurarVersao(id);
    }

    // ===================== DECORATOR =====================

    @PostMapping("/{id}/executar")
    public Map<String, String> executar(@PathVariable Long id,
                                        @RequestParam(defaultValue = "false") boolean email,
                                        @RequestParam(defaultValue = "false") boolean log) {
        return Map.of("resultado", service.executar(id, email, log));
    }
}
