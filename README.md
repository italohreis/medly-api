
# Medly 🩺

API REST para um sistema de agendamento de consultas médicas, construído com as melhores práticas utilizando Spring Boot.

## 📖 Sobre o Projeto

Medly é um back-end robusto que fornece toda a infraestrutura para uma plataforma de agendamento médico. Ele gerencia o ciclo de vida completo de usuários (pacientes e médicos), suas agendas e agendamentos, com um foco em segurança, escalabilidade e manutenibilidade.

## ✨ Principais Funcionalidades

* **Autenticação e Autorização:** Sistema de login seguro com Tokens JWT e controle de acesso granular baseado em perfis (`ADMIN`, `DOCTOR`, `PATIENT`).
* **Gestão de Médicos:** CRUD completo para perfis de médicos, com busca por filtros e inativação (Soft Delete).
* **Gestão de Pacientes:** CRUD completo para perfis de pacientes e auto-registro.
* **Gestão de Agenda:** Médicos podem definir suas janelas de trabalho, e o sistema gera automaticamente os horários agendáveis (`TimeSlots`).
* **Ciclo de Vida de Agendamentos:** Fluxo completo de criar, listar, cancelar, concluir e deletar (Soft Delete) agendamentos.
* **Busca Avançada:** Endpoints de busca com múltiplos filtros e paginação para todos os principais recursos.
* **Pronto para Docker:** Configuração completa com `Dockerfile` e `docker-compose` para um ambiente de desenvolvimento e produção consistente.
* **Documentação:** Documentação da API gerada automaticamente com Springdoc OpenAPI (Swagger UI).


## 🚀 Tecnologias Utilizadas

**Linguagem & Frameworks**

-   Java 21

-   Spring Boot

-   Spring Security

-   Spring Data JPA (Hibernate)


**Banco de Dados & Migrações**

-   PostgreSQL

-   Flyway


**Autenticação & Segurança**

-   JSON Web Tokens (JWT)


**Mapeamento de Objetos**

-   MapStruct


**Documentação da API**

-   Springdoc OpenAPI (Swagger UI)


**Containerização & Deploy**

-   Docker

-   Docker Compose


## 🏁 Como Rodar o Projeto (Localmente com Docker)


**Pré-requisitos:**

-   [Docker](https://www.docker.com/products/docker-desktop/) e Docker Compose instalados e em execução.


**Passos:**

1.  **Clone o repositório:**

    ```
    git clone https://github.com/italohreis/medly.git
    cd medly
    ```

2.  **Crie o arquivo de variáveis de ambiente:** Na raiz do projeto, crie um arquivo chamado `.env`. Ele guardará suas senhas e configurações. Use o modelo no arquivo `.env_example` na raíz do projeto.


3.  **Suba os contêineres:** Execute o seguinte comando na raiz do projeto. Ele irá construir a imagem da sua API e iniciar os contêineres da aplicação e do banco de dados.

    ```
    docker-compose up --build
    ```

4.  **Pronto!** Após a inicialização, o ambiente estará disponível:

    -   **API Medly:** `http://localhost:8080`
 
    - Documentação Interativa (Swagger UI): http://localhost:8080/swagger-ui.html


----------

### Para Parar a Aplicação

Para parar todos os serviços, volte ao terminal onde os contêineres estão rodando e pressione `Ctrl + C`. Para garantir que os contêineres sejam removido, execute:



```
docker-compose down
```

## 🏗️ Estrutura da API (Visão Geral)

* `/auth`: Endpoints de autenticação (login).
* `/users`: Endpoints para gestão do próprio perfil de usuário.
* `/patients`: CRUD de pacientes.
* `/doctors`: CRUD de médicos.
* `/schedule`: Endpoints para gerenciar a agenda (janelas e horários).
* `/appointments`: Endpoints para o ciclo de vida dos agendamentos.

---  
