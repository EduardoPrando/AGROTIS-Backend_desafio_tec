## Desafio Técnito vaga Backend - AGROTIS

#### O teste consiste em construir uma API para ser utilizada na construção de uma tela;

<img src="https://github.com/EduardoPrando/AGROTIS-Backend_desafio_tec/blob/main/external/teste.png" width="auto">

A API deve realizar as validações necessárias para garantir a integridade dos
dados e expor os serviços necessários para a listagem, busca, inserção,
atualização e deleção dos dados (CRUD);

- Verificar a necessidade de serviços adicionais para popular os ListViews
(Propriedade e Laboratório);

- Criar um repositório aberto no github e enviar o link por e-mail ao completar o
teste;

- Adicionalmente, gerar um endpoint que retorne uma lista de laboratórios com as
seguintes características:


    Campos:
        Código do Laboratório;
        Nome do Laboratório formatado em maiúsculo; e
        Quantidade de Pessoas cadastradas neste laboratório.

    Filtros:
        Faixa para Data Inicial da Pessoa (começo e fim) (opcional);
        Faixa para Data Final da Pessoa (começo e fim) (opcional);
        Busca de palavras em qualquer parte do campo Observações(opcional);
        Quantidade mínima da “Quantidade de pessoas cadastradas nestelaboratório” (obrigatório).
    
    Ordenado pela:
        “Quantidade de Pessoas cadastradas neste laboratório” (da maiorpara menor);
        Data inicial da Pessoa (da mais antiga para a mais recente – ordenado apenas caso seja informada no filtro).


### Requisitos
- Desenvolver com Java 11, Spring Boot;
- Utilizar alguma forma de persistência dos dados;
- Enviar a collection do postman com os testes realizados;
- Testes unitários para cada serviço;
- Exemplo: JSON de chamada para o serviço de cadastro de Pessoa;

````
{
    nome: 'Jon doe',
    dataInicial: '2022-02-02T17:41:44Z',
    dataFinal: '2022-02-02T17:41:44Z',
    infosPropriedade: {
        id: 12345,
        nome: 'Nome Exemplo da fazenda'
    },
    laboratorio: {
        id: 1234,
        nome: 'Laboratorio exemplo'
    },
    observacoes: 'Observacao exemplo de teste'
}
````

