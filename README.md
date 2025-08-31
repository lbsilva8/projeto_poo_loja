# Sistema de Ponto de Venda (PDV) com JavaFX

Este projeto é uma aplicação de desktop completa para um Ponto de Venda (PDV), desenvolvida como trabalho final da disciplina de Programação Orientada a Objetos (POO). O sistema possui uma interface gráfica moderna construída com **JavaFX** e um back-end robusto que se conecta ao **Google Firebase** em tempo real.

A aplicação gerencia um sistema de autenticação seguro com senhas criptografadas, controla um inventário de produtos e, o mais importante, implementa um sistema de **autorização com permissões granulares**, permitindo que um gerente defina capacidades específicas para cada usuário atendente.

## Conceitos de POO e Arquitetura Aplicados

O projeto foi estruturado para demonstrar a aplicação prática de diversos conceitos essenciais de engenharia de software:

-   **Orientação a Objetos:**
    -   **Herança e Abstração:** A hierarquia de classes `Usuario` -> `Gerente` e `Atendente` para modelar diferentes tipos de usuários a partir de uma base comum.
    -   **Polimorfismo:** A aplicação lida com diferentes tipos de controllers de forma padronizada através da interface `IController`, e a lógica de negócio trata `Gerente` e `Atendente` como `Usuario`.
    -   **Encapsulamento:** Todos os atributos das classes de modelo são privados, com acesso controlado através de métodos públicos.

-   **Segurança:**
    -   **Criptografia de Senhas:** As senhas dos usuários são sempre armazenadas no banco de dados como um **hash seguro**, utilizando a biblioteca **jBCrypt**. A senha em texto plano nunca é salva.

-   **Interfaces:** Definição de contratos com `IAutenticacao`, `ICrud<T>` e `IController`, garantindo um padrão consistente para autenticação, persistência de dados e navegação entre telas.

-   **Tratamento de Exceções:** Criação de exceções de negócio personalizadas (ex: `ProdutoNaoEncontradoException`, `AcessoNegadoException`) para um controle de erros claro e específico na aplicação.

-   **Padrões de Design (Design Patterns):**
    -   **MVC (Model-View-Controller):** A arquitetura da interface gráfica separa claramente a View (`.fxml`), o Controller (classes `*Controller.java`) e o Model (camada de `service`).
    -   **Repository:** A camada `repository` abstrai toda a complexidade do acesso ao Firebase.
    -   **DTO (Data Transfer Object):** O uso de `VendaDTO` para desacoplar o modelo de domínio do modelo de persistência.
    -   **Dependency Injection:** A injeção de dependências (manual) nos serviços e controllers para aumentar a flexibilidade e testabilidade.

## Principais Funcionalidades

O sistema possui funcionalidades dinâmicas baseadas nas permissões de cada usuário:

-   **Autenticação Segura:** Login com senhas criptografadas e bloqueio de usuários inativos.
-   **Tela Inicial Dinâmica:** Uma tela de boas-vindas que se transforma no painel principal da aplicação após o login.
-   **Registro de Vendas:** Interface para registrar vendas, com busca de produtos e aplicação de descontos.
-   **Visualização de Estoque:** Usuários com a permissão `VISUALIZAR_ESTOQUE` podem ver a lista de produtos e suas quantidades.
-   **Gerenciamento de Estoque (Permissão):** Usuários com a permissão `GERENCIAR_ESTOQUE` podem adicionar, remover e atualizar o preço dos produtos.
-   **Cadastro de Produtos (Permissão):** Usuários com a permissão `CADASTRAR_PRODUTO` podem adicionar novos itens ao inventário.
-   **Gerenciamento de Usuários (Permissão):** Usuários com a permissão `GERENCIAR_USUARIOS` podem:
    -   Cadastrar novos atendentes.
    -   Ativar e inativar contas de atendentes.
    -   Gerenciar as permissões individuais de cada atendente através de uma interface de checkboxes.

## Arquitetura do Sistema

O projeto segue uma arquitetura em camadas clara:
-   **App.java:** Ponto de entrada que inicia a aplicação JavaFX.
-   **View (.fxml):** Define a estrutura visual e os componentes de cada tela.
-   **Controller:** Recebe eventos da View, orquestra chamadas para os Serviços e atualiza a View.
-   **Service:** Contém a lógica de negócio e as regras do sistema.
-   **Repository:** Abstrai e encapsula toda a comunicação com o Firebase.

## Tecnologias Utilizadas

-   **Linguagem:** Java 17+
-   **Interface Gráfica:** JavaFX 17+
-   **Banco de Dados:** Google Firebase Realtime Database
-   **Segurança:** jBCrypt 0.4

## Configuração e Execução

Para executar o projeto localmente, siga os passos abaixo:

#### 1. Pré-requisitos
-   JDK 17 ou superior instalado.
-   **JavaFX SDK** baixado e descompactado em uma pasta no computador.
-   Uma IDE de sua preferência (testado com IntelliJ IDEA).

#### 2. Configuração do Firebase
1.  Crie um projeto no [Firebase Console](https://console.firebase.google.com/).
2.  No menu lateral, vá em **Build > Realtime Database** e crie um novo banco de dados no modo de teste.
3.  No seu projeto Firebase, clique na engrenagem (Configurações do projeto) > **Contas de serviço**.
4.  Clique em **"Gerar nova chave privada"** e salve o arquivo JSON.
5.  Renomeie o arquivo para `serviceAccountKey.json` e mova-o para a pasta `src` do projeto.
6.  Na classe `database/FirebaseConfig.java`, substitua a string `"SUA_URL_DO_BANCO_DE_DADOS_AQUI"` pela URL do seu Realtime Database.

#### 3. Execução via IDE (IntelliJ)
1.  Configure as bibliotecas do JavaFX em `File > Project Structure > Libraries`.
2.  Crie uma configuração de execução para a classe `App.java`.
3.  Adicione as seguintes **VM options** na configuração de execução (substitua o caminho):
    ```
    --module-path "C:\caminho\para\seu\javafx-sdk\lib" --add-modules javafx.controls,javafx.fxml --enable-native-access=javafx.graphics
    ```
4.  Execute a classe `App.java`.

#### 4. Execução via JAR Executável
O projeto pode ser empacotado em um único `.jar` através da ferramenta de **Artifacts** do IntelliJ (`File > Project Structure > Artifacts`), garantindo que todas as dependências sejam incluídas.

## Autoras

-   Andreísy Neves Ferreira
-   Isabella Paranhos Meireles
-   Lorena da Silva Borges
