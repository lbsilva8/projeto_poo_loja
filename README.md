# Sistema de Ponto de Venda (PDV) - Back-end

Este projeto consiste no desenvolvimento do back-end para um sistema de Ponto de Venda (PDV) de uma loja física. A aplicação foi criada como parte da disciplina de Programação Orientada a Objetos (POO), aplicando os conceitos fundamentais de POO e padrões de design para criar um sistema robusto, organizado e de fácil manutenção.

O sistema gerencia usuários com diferentes níveis de permissão (Gerente e Atendente), controla um inventário de produtos e registra as vendas, utilizando o Google Firebase como banco de dados NoSQL em tempo real.

## Conceitos de POO e Arquitetura Aplicados

Este projeto foi estruturado para demonstrar a aplicação prática de diversos conceitos essenciais de engenharia de software:

-   **Orientação a Objetos:**
    -   **Herança:** A hierarquia de classes `Usuario` -> `Gerente` e `Atendente` para especializar e reutilizar código.
    -   **Polimorfismo:** O sistema trata objetos `Gerente` e `Atendente` de forma genérica como `Usuario`, mas utiliza `instanceof` para aplicar lógicas de permissão específicas.
    -   **Encapsulamento:** Todos os atributos das classes de modelo são privados e acessados através de getters e setters, protegendo o estado dos objetos.
    -   **Abstração:** Uso de classes abstratas (`Usuario`) e interfaces para definir contratos e modelos.

-   **Interfaces:** Definição de contratos com `IAutenticacao` e `ICrud<T>`, garantindo um padrão consistente para autenticação e operações de persistência.

-   **Tratamento de Exceções:** Criação de exceções de negócio personalizadas (ex: `ProdutoNaoEncontradoException`, `AcessoNegadoException`) para um controle de erros claro e específico.

-   **Padrões de Design (Design Patterns):**
    -   **MVC (Model-view-Controller):** A arquitetura foi refatorada para separar as responsabilidades, com o `SistemaController` gerenciando o fluxo da aplicação.
    -   **Repository:** A camada `repository` abstrai a complexidade do acesso ao banco de dados, desacoplando a lógica de negócio da tecnologia de persistência.
    -   **DTO (Data Transfer Object):** O uso de `VendaDTO` para desacoplar o modelo de domínio do modelo de persistência.
    -   **Dependency Injection:** A injeção de dependências no `VendaService` e no `SistemaController` para aumentar a flexibilidade e testabilidade.

-   **Banco de Dados:** Integração com um banco de dados NoSQL na nuvem (Firebase Realtime Database), com operações de leitura e escrita assíncronas.

## Funcionalidades Implementadas

O sistema possui diferentes funcionalidades baseadas no nível de acesso do usuário:

#### Atendente
-   Autenticação por usuário e senha.
-   Registro de vendas de produtos, com aplicação de descontos e seleção da forma de pagamento.

#### Gerente
-   Todas as funcionalidades de um Atendente.
-   Cadastro de novos produtos no sistema.
-   Atualização do estoque de produtos existentes.
-   Cadastro de novos usuários do tipo Atendente.

## Arquitetura do Sistema

O projeto segue uma arquitetura em camadas para garantir a separação de responsabilidades:

-   **Main:** Ponto de entrada da aplicação. Apenas inicia o Controller.
-   **Controller:** Camada responsável por interagir com o usuário, receber entradas e orquestrar as chamadas para os serviços.
-   **Service:** Camada onde reside a lógica de negócio principal do sistema (regras de autorização, validações, etc.).
-   **Repository:** Camada de acesso a dados. Abstrai e encapsula toda a comunicação com o Firebase.
-   **Model / Excecoes / Interfaces:** Camadas transversais com as definições de entidades, exceções de negócio e contratos.

## Tecnologias Utilizadas

-   **Linguagem:** Java 
-   **Banco de Dados:** Google Firebase Realtime Database

## Configuração e Execução

Para executar o projeto localmente, siga os passos abaixo:

#### 1. Pré-requisitos
-   JDK 11 ou superior instalado.
-   Uma IDE de sua preferência (IntelliJ IDEA, Eclipse, etc.).

#### 2. Configuração do Firebase
1.  Crie um projeto no [Firebase Console](https://console.firebase.google.com/).
2.  No menu lateral, vá em **Build > Realtime Database** e crie um novo banco de dados no modo de teste.
3.  No seu projeto Firebase, clique na engrenagem (Configurações do projeto) > **Contas de serviço**.
4.  Clique em **"Gerar nova chave privada"**. Um arquivo JSON será baixado.
5.  Renomeie este arquivo para `serviceAccountKey.json` e mova-o para a pasta `src` do seu projeto.
6.  Na classe `database/FirebaseConfig.java`, substitua a string `"SUA_URL_DO_BANCO_DE_DADOS_AQUI"` pela URL do seu Realtime Database.

#### 3. Execução
1.  Clone o repositório:
    ```bash
    git clone [URL_DO_SEU_REPOSITORIO]
    ```
3.  Execute a aplicação através da classe `Main` na sua IDE ou via linha de comando (após configurar o build para gerar um JAR executável).