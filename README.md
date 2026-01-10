# 📦 FASTFOOD - MS Catalog

[![SonarCloud](https://sonarcloud.io/api/project_badges/measure?project=FIAP-SOAT-MS-PRODUCTS&metric=alert_status)](https://sonarcloud.io/dashboard?id=FIAP-SOAT-MS-PRODUCTS)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=FIAP-SOAT-MS-PRODUCTS&metric=coverage)](https://sonarcloud.io/dashboard?id=FIAP-SOAT-MS-PRODUCTS)


Microserviço de Catálogo do sistema de autoatendimento Fastfood, implementado em Java 23 com Spring Boot e estruturado conforme os princípios da Clean Architecture. Responsável pela gestão completa do catálogo de produtos, categorias e inventário. Fornece uma API REST documentada (OpenAPI) para CRUD de produtos, consultas por categoria e gerenciamento de estoque. Projetado para deployment em containers/Kubernetes, com foco em observabilidade e testes automatizados.

## 📚 Sumário

- [⚙️ Tecnologias](#️-tecnologias)
- [✅ Funcionalidades](#-funcionalidades)
- [🧩 Pré-requisitos](#-pré-requisitos)
  - [Para execução local](#para-execução-local)
  - [Para execução com Docker](#para-execução-com-docker)
  - [Para execução com Kubernetes](#para-execução-com-kubernetes)
- [⚡ Quick Start](#-quick-start)
  - [Opção 1: Kubernetes (Recomendado para testes completos)](#opção-1-kubernetes-recomendado-para-testes-completos)
  - [Opção 2: Docker Compose (Mais simples)](#opção-2-docker-compose-mais-simples)
  - [Opção 3: Localmente (Mais rápido para desenvolvimento)](#opção-3-localmente-mais-rápido-para-desenvolvimento)
- [🧪 Testes e Execução Local](#-testes-e-execução-local)
  - [Endpoints importantes](#endpoints-importantes)
  - [Como executar](#como-executar)
- [🔧 Troubleshooting](#-troubleshooting)
  - [Pod não inicia](#pod-não-inicia)
  - [Banco de dados não conecta](#banco-de-dados-não-conecta)
  - [Limpeza de recursos Kubernetes](#limpeza-de-recursos-kubernetes)
  - [Comandos úteis para debug](#comandos-úteis-para-debug)
- [🔍 Comparação dos métodos de execução](#-comparação-dos-métodos-de-execução)
- [☸️ Arquitetura Kubernetes](#️-arquitetura-kubernetes)
  - [📦 Recursos principais](#-recursos-principais)
  - [🗄️ Banco de dados](#️-banco-de-dados)
  - [🔐 Configuração e segurança](#-configuração-e-segurança)
- [🏛️ Decisões Arquiteturais (ADRs)](#🏛️-decisões-arquiteturais-adrs)
- [👤 Membros do projeto](#-membros-do-projeto)

## ⚙️ Tecnologias

| Tecnologia         | Versão                         | Referência                             |
| ------------------ | ------------------------------ | -------------------------------------- |
| Java               | 23                             | https://jdk.java.net/23/               |
| Spring Boot        | 4.0.0                          | https://spring.io/projects/spring-boot |
| Spring Web         | latest (via dependência Maven) | https://spring.io/guides/gs/rest-service |
| Spring Data JPA    | latest (via dependência Maven) | https://spring.io/projects/spring-data-jpa |
| Spring Boot Actuator | latest (via dependência Maven) | https://spring.io/guides/gs/actuator-service |
| Spring Validation  | latest (via dependência Maven) | https://spring.io/guides/gs/validating-form-input |
| Spring WebFlux     | latest (via dependência Maven) | https://spring.io/guides/gs/reactive-rest-service |
| PostgreSQL         | 17.5                           | https://www.postgresql.org/            |
| Flyway             | latest (via dependência Maven) | https://flywaydb.org/                  |
| MapStruct          | latest (via dependência Maven) | https://mapstruct.org/                 |
| Lombok             | latest (via dependência Maven) | https://projectlombok.org/             |
| Mockito            | latest (via dependência Maven) | https://site.mockito.org/              |
| SpringDoc OpenAPI  | latest (via dependência Maven) | https://springdoc.org/                 |
| Maven              | Wrapper incluído (`./mvnw`)    | https://maven.apache.org/              |

## ✅ Funcionalidades

- **Gestão de Produtos**: CRUD completo de produtos com nome, descrição, preço, categoria e ingredientes
- **Gestão de Categorias**: organização hierárquica de produtos por categorias
- **Gestão de Inventário**: controle de estoque e disponibilidade de produtos
- **Consultas avançadas**: busca por categoria, filtros por disponibilidade e preço
- **API REST documentada**: especificação OpenAPI/Swagger para fácil integração
- **Testes automatizados**: testes unitários e de integração com banco em memória
- **Docker**: configuração de Docker Compose para uso local
- **Kubernetes**: manifests prontos para deploy em cluster K8s

## 🧩 Pré-requisitos

### Para execução local

- [Java 23](https://jdk.java.net/)
- [Maven](https://maven.apache.org/) (ou use `./mvnw`)
- [PostgreSQL](https://www.postgresql.org/)

### Para execução com Docker

- [Docker](https://www.docker.com/)
- [Docker Compose](https://docs.docker.com/compose/)

### Para execução com Kubernetes

- [kubectl](https://kubernetes.io/docs/tasks/tools/install-kubectl/)
- [Minikube](https://minikube.sigs.k8s.io/docs/start/) (recomendado)
- Ou qualquer cluster Kubernetes local (kind, k3s, MicroK8s, etc.)

## 📊 SonarCloud - Qualidade de Código

O projeto está integrado com o **SonarCloud** para análise contínua de qualidade:

### Métricas Monitoradas

- **Cobertura de Código**: Mínimo de 80% de cobertura definido pelos testes automatizados
- **Code Smells**: Identificação de más práticas
- **Bugs**: Detecção de possíveis bugs
- **Vulnerabilidades**: Análise de segurança
- **Duplicação**: Código duplicado
- **Maintainability**: Índice de manutenibilidade

### Visualizar Resultados

Acesse o dashboard do SonarCloud em:
- https://sonarcloud.io/project/overview?id=FIAP-SOAT-MS-PRODUCTS

ADICIONAR EVIDENCIA

## ⚡ Quick Start

### Opção 1: Kubernetes (Recomendado para testes completos)

Iniciará aplicação com banco de dados e persistência.

**Windows:**

```powershell
git clone https://github.com/morgadope/FIAP-SOAT-MS-CATALOG.git

cd FIAP-SOAT-MS-CATALOG

cd k8s\local

.\deploy-local.ps1
```

**Linux/Mac:**

```bash
git clone https://github.com/morgadope/FIAP-SOAT-MS-CATALOG.git

cd FIAP-SOAT-MS-CATALOG

cd k8s/local

chmod +x deploy-local.sh

./deploy-local.sh
```

Após o deploy, configure o port-forward para acessar a aplicação:

```bash
kubectl port-forward -n fastfood svc/catalog 8080:8080
```

### Opção 2: Docker Compose (Mais simples)

Iniciará aplicação com banco de dados e persistência, necessário o arquivo `.docker.env`.

```bash
git clone https://github.com/morgadope/FIAP-SOAT-MS-CATALOG.git

cd FIAP-SOAT-MS-CATALOG
```

Crie o `.docker.env` com base no `.docker.env.example`

```bash
docker compose --env-file .docker.env up --build -d
```

### Opção 3: Localmente (Mais rápido para desenvolvimento)

Iniciará somente aplicação, é necessária a configuração do banco de dados e inserção no `.env`.

```bash
git clone https://github.com/morgadope/FIAP-SOAT-MS-CATALOG.git

cd FIAP-SOAT-MS-CATALOG
```

Crie um arquivo `.env` na raiz com base no `.env.example` (caso exista)

```bash
./mvnw clean spring-boot:run
```

**Para todos os casos:**

Acesse a aplicação em: http://localhost:8080

Acesso ao Swagger: http://localhost:8080/swagger-ui/index.html

## 🧪 Testes e Execução Local

### Endpoints importantes

#### Products
- `GET /api/v1/products` - Listar produtos
- `GET /api/v1/products/{id}` - Buscar produto por ID
- `POST /api/v1/products` - Criar produto
- `PUT /api/v1/products/{id}` - Atualizar produto
- `DELETE /api/v1/products/{id}` - Deletar produto (soft delete)

#### Categories
- `GET /api/v1/categories` - Listar categorias
- `GET /api/v1/categories/{id}` - Buscar categoria por ID
- `POST /api/v1/categories` - Criar categoria
- `PUT /api/v1/categories/{id}` - Atualizar categoria
- `DELETE /api/v1/categories/{id}` - Deletar categoria

#### Inventory
- `GET /api/v1/inventory` - Listar inventário
- `POST /api/v1/inventory` - Adicionar ao inventário
- `PATCH /api/v1/inventory/discount-items-by-products` - Descontar itens

### Como executar

As instruções seguem com JSONs de exemplo para facilitar os testes.

1. **Criar uma categoria** no endpoint `POST /api/v1/categories`:

```json
{
  "name": "Bebidas",
  "description": "Bebidas geladas e quentes"
}
```

2. **Criar um produto** no endpoint `POST /api/v1/products`:

```json
{
  "name": "Milk Shake de Chocolate",
  "description": "Delicioso milk shake de chocolate",
  "price": 12.50,
  "category_id": "<CATEGORY_ID>",
  "available": true
}
```

3. **Listar produtos** no endpoint `GET /api/v1/products` para verificar os produtos cadastrados

4. **Buscar produtos por categoria** no endpoint `GET /api/v1/products?category_id=<CATEGORY_ID>`

5. **Atualizar produto** no endpoint `PUT /api/v1/products/{id}`:

```json
{
  "name": "Milk Shake de Chocolate Premium",
  "price": 15.00,
  "available": true
}
```

6. **Adicionar ao inventário** no endpoint `POST /api/v1/inventory`:

```json
{
  "product_id": "<PRODUCT_ID>",
  "quantity": 50
}
```

7. **Deletar produto** no endpoint `DELETE /api/v1/products/{id}` quando necessário

## 🔧 Troubleshooting

### Pod não inicia

```bash
# Verificar status dos pods
kubectl get pods -n fastfood

# Ver logs do pod catalog
kubectl logs -f deployment/catalog -n fastfood

# Verificar eventos
kubectl describe pod <POD_NAME> -n fastfood
```

### Banco de dados não conecta

```bash
# Verificar se o PostgreSQL está rodando
kubectl get pods -n fastfood | grep postgres

# Ver logs do PostgreSQL
kubectl logs -f deployment/postgres-deployment -n fastfood

# Verificar configurações
kubectl describe configmap catalog-config -n fastfood
kubectl describe secret catalog-secret -n fastfood
```

### Limpeza de recursos Kubernetes

**Windows:**

```powershell
cd k8s\local

.\delete-local.ps1
```

**Linux/Mac:**

```bash
cd k8s/local

./delete-local.sh
```

### Comandos úteis para debug

```bash
# Verificar status geral
kubectl get all -n fastfood

# Ver logs da aplicação em tempo real
kubectl logs -f deployment/catalog -n fastfood

# Ver logs do PostgreSQL
kubectl logs -f deployment/postgres-deployment -n fastfood

# Verificar eventos do namespace
kubectl get events -n fastfood --sort-by='.lastTimestamp'

# Executar comando dentro do pod
kubectl exec -it deployment/catalog -n fastfood -- /bin/sh

# Verificar persistência de dados
kubectl get pvc -n fastfood
kubectl get pv
```

## 🔍 Comparação dos métodos de execução

| Método                  | Vantagens                                                          | Desvantagens                                             | Uso recomendado             |
| ----------------------- | ------------------------------------------------------------------ | -------------------------------------------------------- | --------------------------- |
| **Local (Spring Boot)** | ⚡ Rápido para desenvolvimento<br>🔧 Debug fácil<br>🔄 Hot reload  | 🐳 Não testa containers<br>📦 Não testa infraestrutura   | Desenvolvimento inicial     |
| **Docker Compose**      | 🐳 Testa containers<br>📦 Simples de usar<br>🔄 Recriação rápida   | 🌐 Não testa orquestração<br>📊 Não testa escalabilidade | Testes de integração        |
| **Kubernetes**          | ☸️ Testa orquestração<br>📊 Escalabilidade<br>🔧 Configuração real | ⏱️ Mais complexo<br>🔄 Deploy mais lento                 | Produção e testes avançados |

## ☸️ Arquitetura Kubernetes

O projeto inclui os seguintes componentes Kubernetes:

### 📦 Recursos principais

- **Namespace**: `fastfood` - Isolamento dos recursos
- **Deployment**: `catalog` - Aplicação do microserviço (1 réplica)
- **Service**: `catalog` - Exposição da aplicação (ClusterIP na porta 8080)

### 🗄️ Banco de dados

- **Deployment**: `postgres-deployment` - PostgreSQL
- **Service**: `postgres-service` - Acesso interno ao banco
- **PVC**: `postgres-pvc` - Volume persistente para dados

### 🔐 Configuração e segurança

- **ConfigMap**: `catalog-config` - Configurações da aplicação
- **Secret**: `catalog-secret` - Credenciais do banco de dados

## 🏛️ Decisões Arquiteturais (ADRs)

As decisões arquiteturais e registros de design do projeto estão na pasta `docs/`. Abaixo estão os ADRs já criados:

- [ADR 01 — Java + Spring (Clean Architecture)](docs/adr-01-java-spring-clean-arch.md)
- [ADR 02 — Database PostgreSQL/Aurora](docs/adr-02-database-postgresql-aurora.md)
- [ADR 03 — Deployment Containers/K8s](docs/adr-03-deployment-containers-k8s.md)
- [ADR 04 — Inventory Consolidation](docs/adr-04-inventory-consolidation.md)
- [ADR 05 — Renaming Products to Catalog](docs/adr-05-renaming-products-to-catalog.md)

## 👤 Membros do projeto

- Diego de Salles — RM362702
- Lucas Felinto — RM363094
- Maickel Alves — RM361616
- Pedro Morgado — RM364209
- Wesley Alves — RM364342
