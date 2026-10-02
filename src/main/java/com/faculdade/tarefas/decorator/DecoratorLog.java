package com.faculdade.tarefas.decorator;

import com.faculdade.tarefas.model.Tarefa;

/**
 * DECORATOR: adiciona o comportamento "registrar log".
 * Mesma estrutura do DecoratorEmail, por isso os dois podem ser combinados:
 *
 *   new DecoratorLog(new DecoratorEmail(new OperacaoBasica()))
 */
public class DecoratorLog implements OperacaoTarefa {

    private final OperacaoTarefa operacao;

    public DecoratorLog(OperacaoTarefa operacao) {
        this.operacao = operacao;
    }

    @Override
    public String executar(Tarefa tarefa) {
        String resultado = operacao.executar(tarefa);                   // comportamento original
        String log = "Log registrado para a tarefa '" + tarefa.getTitulo() + "'"; // novo
        System.out.println("[LOG] " + log);
        return resultado + " | " + log;
    }
}
