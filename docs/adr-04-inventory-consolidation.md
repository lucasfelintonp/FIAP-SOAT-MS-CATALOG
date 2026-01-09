# ADR 04 — Consolidação do contexto de Inventory no microserviço Catalog

## Contexto

O microserviço precisa gerenciar produtos, categorias e inventário (estoque). Surgiu a questão de organizar estes bounded contexts em microserviços separados ou consolidados. A análise revelou forte acoplamento: produtos consomem itens do inventário através de receitas (hambúrguer precisa de pão, carne, alface), e operações críticas como pedidos precisam validar disponibilidade e descontar estoque atomicamente. Separar geraria overhead de comunicação via HTTP, transações distribuídas complexas e latência em operações frequentes.

## Decisão

Consolidar os bounded contexts de **Product**, **Category** e **Inventory** em um único microserviço (MS-CATALOG), mantendo-os como módulos separados com suas próprias camadas `domain`, `application` e `infrastructure`. A comunicação entre contextos é feita via chamadas locais (in-process), preservando a separação lógica através da Clean Architecture.

## Motivação

- **Alto acoplamento de domínio**: Inventário existe em função dos produtos (tabela `inventory_products` relaciona produtos com ingredientes consumidos).
- **Transações atômicas**: Operações críticas (ex: processar pedido) precisam validar produtos E descontar estoque atomicamente. Separar exigiria Saga Pattern com compensações.
- **Performance**: Chamadas locais (~5-20ms) vs HTTP entre serviços (~50-200ms) — ganho de 10-40x em operações frequentes.
- **Coesão funcional**: Do ponto de vista de negócio, catálogo é a fonte de verdade sobre "o que podemos vender" (produtos, categorias e disponibilidade).
- **Simplicidade operacional**: 1 repositório, pipeline e deployment vs 3 de cada. Logs centralizados e transações ACID nativas.

## Alternativas consideradas

1. **Microserviços separados (MS-PRODUCTS, MS-INVENTORY, MS-CATEGORIES)** — rejeitado por exigir Saga Pattern, latência de rede em operações críticas e 3x overhead operacional.
2. **Monolito sem separação de módulos** — rejeitado por não demonstrar DDD e dificultar separação futura.
3. **Módulos DDD no mesmo serviço** — escolhido por manter separação lógica, performance de chamadas locais, transações ACID e simplicidade operacional.

## Implicações operacionais

- **Estrutura de pacotes**: Cada contexto (`product/`, `category/`, `inventory/`) mantém camadas `domain`, `application` e `infrastructure` separadas.
- **Banco de dados**: Tabelas relacionadas no mesmo schema com FKs entre `products` e `inventory_products`, permitindo joins eficientes e transações ACID.
- **APIs REST**: Endpoints expostos para cada contexto (`/api/v1/products`, `/api/v1/categories`, `/api/v1/inventory`).
- **Comunicação**: Chamadas diretas entre contextos via injeção de dependência (in-process), sem overhead de rede.
- **Deployment**: Single deployment unit com pipeline único, logs e métricas centralizados.

## Plano de implementação

1. Criar estrutura de pacotes para cada bounded context com camadas separadas.
2. Implementar entidades de domínio e migrations (V1 para products/categories, V2 para inventory).
3. Criar tabela `inventory_products` com FKs relacionando os contextos.
4. Implementar use cases que operam entre contextos via injeção de dependência.
5. Expor APIs REST para cada contexto e documentar nesta ADR.
6. Monitorar performance para identificar necessidade de separação futura.

## Consequências

**Positivas**: Performance superior (10-40x), transações ACID nativas, simplicidade operacional (1 deploy vs 3), debugging facilitado com logs centralizados, desenvolvimento ágil para mudanças cross-context, custo reduzido de infraestrutura.

**Negativas**: Deploy acoplado (mitigado com pipeline rápido), escalabilidade acoplada (mitigado com escala horizontal do serviço completo, suficiente para escopo atual), possível refatoração futura (bounded contexts bem definidos facilitam extração se necessário).
