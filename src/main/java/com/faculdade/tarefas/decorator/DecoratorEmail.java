package com.faculdade.tarefas.decorator;

import com.faculdade.tarefas.model.Tarefa;

/**
 * DECORATOR: adiciona o comportamento "enviar e-mail".
 *
 * 1. Recebe outra OperacaoTarefa (o original ou outro decorator);
 * 2. Implementa a mesma interface;
 * 3. No executar(), chama a operação recebida e ADICIONA o seu comportamento.
 *
 * O "envio" só imprime no console (não é um e-mail de verdade).
 */
public class DecoratorEmail implements OperacaoTarefa {

    private final OperacaoTarefa operacao; // objeto "embrulhado"

    public DecoratorEmail(OperacaoTarefa operacao) {
        this.operacao = operacao;
    }

    @Override
    public String executar(Tarefa tarefa) {
        String resultado = operacao.executar(tarefa);                       // comportamento original
        String email = "E-mail enviado sobre a tarefa '" + tarefa.getTitulo() + "'"; // novo
        System.out.println("[EMAIL] " + email);
        return resultado + " | " + email;
    }
}
