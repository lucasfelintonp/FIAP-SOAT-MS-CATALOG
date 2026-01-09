```markdown
# ADR 01 — Escolha de linguagem e arquitetura: Java + Spring Boot + Clean Architecture

## Contexto

O microserviço `ms-catalog` é responsável por expor e manter o catálogo de produtos, categorias e inventário (CRUD, busca, categorias, atributos). O repositório já contém um projeto Java com Maven, e as demais equipes do workspace usam Java/Spring como padrão. É desejável uma arquitetura que facilite testabilidade, manutenção e integração com outros serviços (auth, orders, etc).

## Decisão

Adotar Java (versão alinhada ao mono-repo / build tool) com Spring Boot e organizar o código segundo os princípios da Clean Architecture: camadas `domain`, `application` (casos de uso), `adapters`/`infrastructure` (persistência, drivers, APIs). Utilizar contratos (interfaces) entre camadas e injeção de dependência via Spring.

## Motivação

- Consistência com outros serviços no workspace, reduzindo curva de manutenção.
- Ampla maturidade de bibliotecas (Spring, JPA, Micrometer) e integração com ferramentas de observabilidade e CI.
- Clean Architecture aumenta a testabilidade do domínio e permite trocar implementações de infra (por ex., RDS -> outro DB) com menor impacto.

## Alternativas consideradas

1. Kotlin + Spring Boot — benefício em verbosidade; foi rejeitado para evitar introduzir uma nova linguagem no time neste momento.
2. Node.js / NestJS — mais rápido para prototipação, porém perde em robustez de tipagem e alinhamento com o restante do repositório.
3. Arquitetura monolítica dentro do módulo — simplifica inicialmente, mas compromete isolamento e escalabilidade.

## Implicações operacionais

- Estrutura de diretórios padronizada (`domain`, `application`, `adapters`, `infrastructure`).
- Definir guidelines de revisão de código para manter as fronteiras arquiteturais.
- Necessidade de ferramentas de build e verificação (`./mvnw`), testes unitários e de integração com banco em memória para CI.
- Configuração de observabilidade (Micrometer/Prometheus) e logs estruturados (JSON) desde o início.

## Plano de implementação

1. Criar esqueleto de módulos/pacotes refletindo as camadas.
2. Definir contratos (interfaces) para repositórios e portas, implementar adaptadores JPA/Hibernate.
3. Introduzir Flyway/liquibase para migrações de esquema e configurar perfis `dev|test|prod`.
4. Escrever testes unitários para `domain` e testes de integração com H2 para `infrastructure`.
5. Documentar convenções no README do módulo e em `docs/`.

```
