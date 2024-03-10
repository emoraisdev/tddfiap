# language: pt

Funcionalidade: Mensagem

  Cenario: Registrar Mensagem
    Quando registrar uma nova mensagem
    Entao a mensagem é registrada com sucesso
    E deve ser apresentada

  Cenario: Buscar Mensagem
    Dado que uma mensagem já foi publicada
    Quando efetuar a busca da mensagemr
    Entao a mensagem é exibida com sucesso

  Cenario: Alterar Mensagem
    Dado que uma mensagem já foi publicada
    Quando efetuar a requisição para alterar a mensagemr
    Entao a mensagem é alterada com sucesso
    E deve ser apresentada

  Cenario: Remover Mensagem
    Dado que uma mensagem já foi publicada
    Quando requisitar a remoção da mensagem
    Entao a mensagem é removida com sucesso