```markdown
# ADR 02 — Banco de dados: PostgreSQL (Amazon Aurora / RDS) e estratégia de persistência

## Contexto

O serviço de produtos precisa armazenar informações relacionais (produto, preços, categorias, estoque mínimo) com garantia de consistência e consultas eficientes. O repositório de infra do workspace já adota RDS/Aurora como padrão para outros serviços.

## Decisão

Utilizar PostgreSQL compatível (preferencialmente Amazon Aurora PostgreSQL via infra compartilhada) como banco relacional primário. No código, usar JPA/Hibernate com DTOs e mapeamento claro do domínio; controlar o esquema com Flyway para migrações gerenciáveis.

## Motivação

-   Compatibilidade com padrões do workspace
-   Recursos do PostgreSQL (JSONB, índices GIN, ops de text search) úteis para buscas/atributos de produto.
-   Aurora provê alta disponibilidade, endpoints de leitura e facilidade operacional.

## Alternativas consideradas

1. MongoDB / NoSQL — boa para esquemas flexíveis, mas complica consultas relacionais e integridade entre domínios.
2. RDS PostgreSQL single instance — opção de menor custo, mas sem reader endpoints e menor resiliência.
3. Hybrid: primary relational + cache (Redis) — recomendado como otimização posterior para leituras intensivas.

## Implicações operacionais

-   Necessidade de gerenciar migrações via Flyway; evitar DDLs ad-hoc em produção.
-   Configurar pools de conexão (HikariCP) e limites apropriados para evitar esgotamento de conexões.
-   Gerenciamento de secrets através do Secrets Manager / SSM com encriptação; evitar credenciais em código.
-   Estratégia de backups, snapshots e runbooks de restore alinhados com políticas corporativas.

## Plano de implementação

1. Definir schema inicial e criar scripts de migração Flyway em `src/main/resources/db/migration`.
2. Implementar repositórios JPA e testes de integração usando H2 (ou Postgres em container) nos pipelines.
3. Configurar properties de conexão via variáveis/secretos e perfis `dev|test|prod`.
4. Documentar dependências e como apontar para o cluster Aurora provisionado pelo módulo infra (variáveis de ambiente e exemplos `application.yml`).
5. Considerar adição de cache (Redis) para endpoints de catálogo quando necessário.
```
