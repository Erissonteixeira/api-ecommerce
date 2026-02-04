# api-ecommerce (backend)

API do projeto **api-ecommerce**, construída em **Java 21 + Spring Boot**, com persistência via **JPA**, migrations com **Flyway**, documentação via **Swagger (springdoc-openapi)** e suporte a dois ambientes:

- **dev**: H2 em memória
- **prod**: MySQL via Docker

---

## Visão geral

Esta API fornece os recursos principais do e-commerce:

- Produtos
- Carrinho
- Pedido

A aplicação roda por padrão em:

- **API:** `http://localhost:8080`

---

## Stack

- Java 21
- Spring Boot 3.5.x
- Spring Web
- Spring Data JPA
- Bean Validation
- Flyway (migrations)
- H2 (dev)
- MySQL (prod)
- MapStruct
- Lombok
- springdoc-openapi (Swagger UI)

---

## Perfis (dev / prod)

O projeto usa **profiles** do Spring:

### dev (padrão)

- Banco: **H2 em memória**
- URL: `jdbc:h2:mem:ecommerce-db;MODE=MySQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE`
- H2 Console: `http://localhost:8080/h2-console`
- `spring.jpa.show-sql=true`

> Perfil ativo por padrão:
> `spring.profiles.active=dev`

### prod

- Banco: **MySQL**
- Conexão esperada (container): `jdbc:mysql://mysql:3306/ecommerce?...`
- `spring.jpa.show-sql=false`
- Ideal para subir via **Docker Compose**

---

## Como rodar

### 1) Rodar em modo dev (H2 em memória)

Com o profile padrão `dev`, basta iniciar a aplicação.

Você pode rodar pela IDE (Run) ou via Maven.

Exemplo (se estiver usando Maven direto):

```bash
mvn spring-boot:run
```

A aplicação sobe em:

- http://localhost:8080

### H2 Console

- http://localhost:8080/h2-console

---

## 2) Rodar em modo prod (MySQL via Docker)

Este modo sobe a API com o profile:

- `SPRING_PROFILES_ACTIVE=prod`

e um **MySQL 5.7** via Docker Compose.

Na raiz do projeto (onde está o `docker-compose.yml`):

```bash
docker compose up --build
```
## Serviços

- API: http://localhost:8080
- MySQL: localhost:3306

## Credenciais do MySQL (Docker)

- Database: `ecommerce`
- User: `ecommerce`
- Password: `ecommerce`
- Root password: `root`

## Parar containers

```bash
docker compose down
```
## Remover volumes (apaga os dados do banco)

```bash
docker compose down -v
```
## Migrations (Flyway)

O **Flyway** está habilitado nos profiles `dev` e `prod` e executa as migrations automaticamente ao iniciar a aplicação.

Durante a inicialização da API, você deve ver mensagens no log como:

- `Successfully validated ... migrations`
- `Successfully applied ... migrations`

```
## Swagger / OpenAPI

A documentação da API é disponibilizada via **springdoc-openapi**.

Acesse:

- http://localhost:8080/swagger-ui/index.html

(Em alguns setups também funciona em `/swagger-ui.html`)
````
## Configurações principais

- Porta padrão: `8080`
- `spring.jpa.open-in-view=false`
- `spring.jpa.hibernate.ddl-auto=validate`
- Flyway habilitado
```
## Checklist rápido de validação

- [ ] Backend sobe em **dev** com H2 (porta 8080)
- [ ] Acessa H2 console em `/h2-console`
- [ ] Swagger abre em `/swagger-ui/index.html`
- [ ] Flyway aplica migrations sem erro
- [ ] Docker Compose sobe `api + mysql` em **prod**
```


