# API E-commerce

API REST de um sistema de **e-commerce**, desenvolvida com **Java 21 + Spring Boot**.

A aplicação oferece autenticação de usuários com **JWT**, gerenciamento de produtos, carrinho de compras vinculado ao usuário autenticado e criação de pedidos.

A persistência é feita com **Spring Data JPA**, as migrations são controladas pelo **Flyway** e a documentação da API é gerada automaticamente via **Swagger (springdoc-openapi)**.

O projeto suporta dois ambientes:

- **dev** → H2 em memória
- **prod** → MySQL via Docker

---

# Visão geral

Esta API fornece os recursos principais de um fluxo de e-commerce:

- Autenticação de usuários (JWT)
- Cadastro e consulta de produtos
- Carrinho de compras vinculado ao usuário autenticado
- Finalização de pedidos
- Histórico de pedidos do usuário autenticado

A aplicação roda por padrão em:

http://localhost:8080

---

# Stack

- Java 21
- Spring Boot 3.5.x
- Spring Web
- Spring Data JPA
- Spring Security
- JWT
- Bean Validation
- Flyway (migrations)
- H2 Database (dev)
- MySQL (prod)
- MapStruct
- Lombok
- springdoc-openapi (Swagger)

---

# Arquitetura

A aplicação segue uma arquitetura em camadas:

Controller  
↓  
Service  
↓  
Repository  
↓  
Database  

Estrutura principal do domínio:

src/main/java/io/github/.../domain

- auth
- usuario
- produto
- carrinho
- pedido

Cada domínio possui sua própria organização:

- controller
- service
- repository
- dto
- mapper
- entity

Essa estrutura facilita manutenção, testes e evolução do sistema.

---

# Autenticação

A API utiliza **Spring Security + JWT**.

Fluxo de autenticação:

1. Usuário se registra
2. Usuário faz login
3. A API retorna um **JWT**
4. O token deve ser enviado nos endpoints protegidos

Exemplo de header:

Authorization: Bearer <token>

Endpoints protegidos exigem autenticação.

---

# Perfis (dev / prod)

O projeto utiliza **Spring Profiles** para separar ambientes.

## dev (padrão)

Banco de dados em memória utilizando **H2**.

URL:

jdbc:h2:mem:ecommerce-db;MODE=MySQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE

H2 Console:

http://localhost:8080/h2-console

Profile ativo por padrão:

spring.profiles.active=dev

---

## prod

Banco de dados **MySQL**, utilizado via Docker.

URL esperada:

jdbc:mysql://mysql:3306/ecommerce

Nesse ambiente:

spring.jpa.show-sql=false

---

# Como rodar

## Rodar em modo dev

Com o profile padrão `dev`, basta iniciar a aplicação.

Via Maven:

mvn spring-boot:run

A aplicação sobe em:

http://localhost:8080

---

# H2 Console

Acesse:

http://localhost:8080/h2-console

Use:

jdbc:h2:mem:ecommerce-db

---

# Rodar em modo prod (Docker)

Este modo sobe a API com **MySQL via Docker Compose**.

Na raiz do projeto:

docker compose up --build

---

# Serviços

API

http://localhost:8080

MySQL

localhost:3306

---

# Credenciais MySQL (Docker)

Database

ecommerce

User

ecommerce

Password

ecommerce

Root password

root

---

# Parar containers

docker compose down

---

# Remover volumes (apaga banco)

docker compose down -v

---

# Migrations (Flyway)

O **Flyway** executa automaticamente as migrations ao iniciar a aplicação.

Durante o startup da API você verá logs como:

Successfully validated migrations  
Successfully applied migrations  

Isso garante que o banco esteja sempre atualizado.

---

# Principais endpoints

## Auth

POST /auth/register  
POST /auth/login  
GET /auth/me  

---

## Produtos

GET /produtos  
GET /produtos/{id}

---

## Carrinho

POST /carrinhos  
GET /carrinhos/{id}  
POST /carrinhos/{id}/itens  
DELETE /carrinhos/{id}/itens/{produtoId}

---

## Pedidos

POST /carrinhos/{id}/pedido  
GET /pedidos/me  
GET /pedidos/me/{id}

---

# Swagger / OpenAPI

Documentação da API disponível em:

http://localhost:8080/swagger-ui/index.html

Também pode funcionar em:

/swagger-ui.html

---

# Configurações principais

Porta da aplicação

8080

Configurações relevantes:

spring.jpa.open-in-view=false  
spring.jpa.hibernate.ddl-auto=validate  
flyway.enabled=true

---

# Checklist de validação

- Backend sobe em **dev** com H2
- H2 console acessível em `/h2-console`
- Swagger abre em `/swagger-ui/index.html`
- Flyway aplica migrations sem erro
- Docker Compose sobe **API + MySQL**
- Autenticação JWT funcionando
- Usuário consegue criar carrinho
- Usuário consegue finalizar pedido
- Usuário consegue visualizar seus pedidos
