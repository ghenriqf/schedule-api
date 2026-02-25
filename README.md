# Schedule API (Projeto em desenvolvimento)

## Sobre o Projeto

O Schedule é um projeto pessoal desenvolvido para fins de estudo, consistindo em uma aplicação web voltada à organização de ministérios de louvor em igrejas. Seu principal objetivo é oferecer uma plataforma prática e eficiente para facilitar o gerenciamento de escalas, eventos e atividades relacionadas ao ministério.

## Principais Funcionalidades

* Autenticação e autorização de usuários.
* Gestão completa de ministérios (Criar, atualizar, listar e excluir).
* Gerenciamento de membros participantes por ministério.
* Administração de repertório musical vinculado ao ministério.
* Atribuição de funções e cargos específicos aos membros.
* Criação de escalas e eventos com associação de participantes, músicas e funções.

## Tecnologias Utilizadas

* **Linguagem:** Java 21
* **Framework:** Spring Boot 3.5.10
* **Banco de Dados:** PostgreSQL 18
* **Gerenciador de Dependências:** Maven
* **Virtualização:** Docker

### Dependências Principais

* Lombok
* JSON Web Token (JWT)
* Flyway (Migrações de banco de dados)
* Spring Security
* Spring Data JPA

---

## Estrutura do Projeto

O código fonte segue a estrutura de pacotes baseada em domínios dentro de `src/main/java/com/ghenriqf/schedule`:

* `auth`: Gestão de autenticação, segurança e JWT.
* `common`: Classes compartilhadas (Exceções globais, DTOs genéricos).
* `function`: Gerenciamento de funções e cargos no sistema.
* `member`: Gestão de membros e usuários.
* `ministry`: Gerenciamento de ministérios.
* `music`: Controle de repertório e músicas.
* `scale`: Gestão de escalas e agendamentos.
* `ScheduleApplication.java`: Classe principal de inicialização.

### Camadas da Aplicação

1. **Controller:** Pontos de entrada da API (REST Endpoints).
2. **Service:** Contém a regra de negócio e lógica da aplicação.
3. **Repository:** Interface de comunicação com o banco de dados via Spring Data JPA.
4. **Entity:** Representação das tabelas do banco de dados.
5. **DTO (Data Transfer Object):** Objetos para tráfego de dados entre cliente e servidor.
6. **Mapper:** Responsável pela conversão entre Entidades e DTOs.

### Exemplo de Fluxo de Dados

1. O Controller recebe a requisição HTTP.
2. O Mapper converte o JSON de entrada para um DTO.
3. O Service processa as regras de negócio pertinentes.
4. O Repository persiste ou busca a Entity no banco de dados.
5. O Service retorna o resultado para o Controller, que utiliza o Mapper para devolver um DTO de resposta.

---

## Principais Endpoints

### Autenticação

| Método | Endpoint | Descrição |
| --- | --- | --- |
| POST | `/api/v1/auth/signup` | Cadastro de novo usuário |
| POST | `/api/v1/auth/login` | Autenticação de usuário |

### Ministérios

| Método | Endpoint | Descrição |
| --- | --- | --- |
| POST | `/api/v1/ministries` | Cria um novo ministério |
| GET | `/api/v1/ministries` | Lista ministérios do usuário |
| GET | `/api/v1/ministries/{id}` | Detalhes de um ministério |
| POST | `/api/v1/ministries/{id}/invite-code` | Gera código de convite |
| POST | `/api/v1/ministries/join/{inviteCode}` | Entrar em ministério via código |

### Músicas

| Método | Endpoint | Descrição |
| --- | --- | --- |
| POST | `/api/v1/ministries/{id}/music` | Cadastra música no repertório |
| GET | `/api/v1/ministries/{id}/music` | Lista músicas do ministério |
| PUT | `/api/v1/ministries/{id}/music/{musicId}` | Atualiza dados da música |
| DELETE | `/api/v1/ministries/{id}/music/{musicId}` | Remove música do repertório |

### Escalas

| Método | Endpoint | Descrição |
| --- | --- | --- |
| POST | `/api/v1/ministries/{id}/scales` | Cria uma nova escala |
| PATCH | `/api/v1/ministries/{id}/scales/{scaleId}` | Atualização parcial da escala |
| GET | `/api/v1/ministries/{id}/scales` | Lista escalas do ministério |
| POST | `/api/v1/scales/{id}/members/{memberId}` | Adiciona membro à escala |
| POST | `/api/v1/scales/{id}/music/{musicId}` | Adiciona música à escala |
