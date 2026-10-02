# Tarefas – MVC + Builder + Decorator + Memento

## 1. Descrição

Gerenciador de tarefas simples (Spring Boot) feito para **demonstrar os padrões Builder, Decorator e Memento** dentro de uma organização MVC.

## 2. Tecnologias

Java 21 · Spring Boot 3.3 · Spring Web · Spring Data JPA · H2 (banco em memória) · Maven · JUnit 5

## 3. Como executar

Precisa de JDK 21+ e Maven.

```bash
mvn spring-boot:run     # sobe em http://localhost:8080
mvn test                # roda os testes dos 3 padrões
```

Ou abra a pasta como projeto Maven na sua IDE e execute `TarefasApplication`.

## 4. Estrutura dos pacotes

```text
com.faculdade.tarefas
├── controller/   TarefaController                      (MVC: Controller)
├── service/      TarefaService                         (regras; usa os 3 padrões)
├── repository/   TarefaRepository                      (banco de dados)
├── model/        Tarefa, Prioridade, Status            (MVC: Model + BUILDER + MEMENTO Originator)
├── decorator/    OperacaoTarefa, OperacaoBasica,
│                 DecoratorEmail, DecoratorLog          (DECORATOR)
└── memento/      MementoTarefa, HistoricoTarefa        (MEMENTO)
```

## 5. MVC

```text
Controller  ->  Service  ->  Repository  ->  Banco (H2)
```

- **Controller:** recebe e responde HTTP. Não tem regra de negócio.
- **Service:** tem as regras e usa os padrões.
- **Repository:** só salva e busca no banco.
- **Model:** a classe `Tarefa`.

## 6. Builder (`Tarefa.Builder`)

- **Problema:** construtor com muitos parâmetros é confuso (`new Tarefa("a","b",ALTA,PENDENTE)`).
- **Solução:** cada valor tem um nome e os que faltam recebem valor padrão.
- **Quem é o Builder:** a classe interna `Tarefa.Builder`.
- **O que o `build()` faz:** valida (título obrigatório), aplica valores padrão e cria a `Tarefa`. O construtor de `Tarefa` é privado, então só o Builder cria tarefas.

```java
Tarefa tarefa = new Tarefa.Builder()
        .titulo("Estudar Design Patterns")
        .prioridade(Prioridade.ALTA)
        .build();
```

## 7. Decorator (pacote `decorator`)

```text
OperacaoTarefa (interface)
      ↓
OperacaoBasica                 <- objeto ORIGINAL
      ↓
-----------------------------
|                           |
DecoratorEmail          DecoratorLog      <- DECORATORS
```

- **Objeto original:** `OperacaoBasica`.
- **Decorators:** `DecoratorEmail` e `DecoratorLog`.
- **Como adicionam comportamento:** recebem outra `OperacaoTarefa`, chamam ela e somam o seu comportamento.
- **Por que combinam:** todos implementam a mesma interface.

```java
OperacaoTarefa operacao =
        new DecoratorLog(
            new DecoratorEmail(
                new OperacaoBasica()));
```

## 8. Memento (pacote `memento` + `Tarefa`)

| Papel | Classe | Função |
|---|---|---|
| Originator | `Tarefa` | Cria o memento (`criarMemento`) e se restaura (`restaurar`) |
| Memento | `MementoTarefa` | Guarda uma cópia do estado: título, descrição, prioridade e status |
| Caretaker | `HistoricoTarefa` | Guarda os mementos em memória, sem olhar o conteúdo |

```text
Tarefa --criarMemento()--> MementoTarefa --guardar()--> HistoricoTarefa
Tarefa <--restaurar(m)---- MementoTarefa <--recuperarUltimo()-- HistoricoTarefa
```

O histórico fica em memória: é apagado ao reiniciar o programa.

## 9. Exemplos de uso da API

```bash
# Criar (Builder)
curl -X POST localhost:8080/tarefas -H "Content-Type: application/json" \
  -d '{"titulo":"Estudar Java","descricao":"Estudar Builder","prioridade":"ALTA"}'

# Listar, ver, editar e excluir
curl localhost:8080/tarefas
curl localhost:8080/tarefas/1
curl -X PUT localhost:8080/tarefas/1 -H "Content-Type: application/json" \
  -d '{"titulo":"Estudar Java","descricao":"Estudar Builder e Decorator"}'
curl -X DELETE localhost:8080/tarefas/1

# Memento: salvar e restaurar
curl -X POST localhost:8080/tarefas/1/salvar
#   (edite a tarefa com PUT)
curl -X POST localhost:8080/tarefas/1/restaurar

# Decorator: executar com e-mail e log
curl -X POST "localhost:8080/tarefas/1/executar?email=true&log=true"
```

Erros: `404` (tarefa não existe) e `400` (sem título, ou sem versão salva para restaurar).

## 10. Fluxo dos três padrões

```text
POST /tarefas            Controller -> Service.criar() -> [BUILDER: build()] -> Repository

POST /tarefas/1/salvar   Controller -> Service -> Tarefa.criarMemento() -> HistoricoTarefa.guardar()
PUT  /tarefas/1          (altera a tarefa)
POST /tarefas/1/restaurar  Controller -> Service -> HistoricoTarefa.recuperarUltimo() -> Tarefa.restaurar()  [MEMENTO]

POST /tarefas/1/executar Controller -> Service -> monta Log(Email(Basica)) -> executar()  [DECORATOR]
```

Testes para a apresentação: `BuilderTest`, `DecoratorTest` e `MementoTest`.
