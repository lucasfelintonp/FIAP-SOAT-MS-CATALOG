# ADR 05 — Renomeação do microserviço de Products para Catalog

## Contexto

O microserviço inicialmente chamado `ms-products` foi concebido para gerenciar apenas produtos do catálogo. Com a decisão de consolidar os bounded contexts de Product, Category e Inventory em um único serviço (conforme ADR 04), o nome "products" deixou de refletir adequadamente o escopo completo. O serviço agora funciona como um catálogo completo do sistema, e manter o nome antigo gerava confusão sobre suas responsabilidades reais e dificultava a comunicação entre equipes.

## Decisão

Renomear o microserviço de `FIAP-SOAT-MS-PRODUCTS` para `FIAP-SOAT-MS-CATALOG`, incluindo containers Docker, volumes, configurações Kubernetes (deployments, services, secrets) e documentação. Manter inalterados os pacotes Java, endpoints de API e nomes de tabelas para preservar a estrutura DDD e garantir retrocompatibilidade.

## Motivação

- **Clareza semântica**: O nome "catalog" representa com precisão o escopo completo (produtos, categorias e inventário), alinhado com o conceito de negócio em sistemas de e-commerce e food service.
- **Facilita compreensão**: Desenvolvedores e stakeholders entendem rapidamente as responsabilidades do serviço pelo nome.
- **Evita ambiguidade**: Elimina dúvidas sobre se categorias e inventário deveriam estar em serviços separados.
- **Manutenibilidade**: Documentação e configurações ficam mais claras e consistentes.
- **Consequência natural da ADR 04**: A consolidação dos contextos justifica um nome mais abrangente.

## Alternativas consideradas

1. **Manter como MS-PRODUCTS** — rejeitado por não refletir o escopo real e gerar confusão contínua.
2. **Criar microserviços separados** — rejeitado conforme documentado na ADR 04 (alto acoplamento, overhead operacional).
3. **Usar nome genérico (MS-DATA, MS-CORE)** — rejeitado por perder clareza sobre o domínio específico.

## Implicações operacionais

- **Infraestrutura**: Containers, volumes e arquivos Kubernetes renomeados. Health checks migrados para `/actuator/health/readiness` e `/actuator/health/liveness`. Secret do banco atualizado para `fiaptc-catalog`.
- **Documentação**: ADRs, README e guias atualizados para refletir novo nome.
- **Código preservado**: Pacotes Java (`product/`, `category/`, `inventory/`), endpoints de API (`/api/v1/products`, etc) e tabelas do banco mantidos para preservar DDD e retrocompatibilidade.
- **Ações pós-migração**: Renomear repositório Git, atualizar SonarQube, pipelines CI/CD e service discovery.

## Plano de implementação

1. Atualizar `pom.xml`, `docker-compose.yml` e arquivos Kubernetes.
2. Melhorar health checks para usar Actuator.
3. Atualizar secrets e configurações.
4. Revisar toda documentação (ADRs, README).
5. Validar compilação local e testes de integração.
6. Atualizar CI/CD e comunicar mudança às equipes.

## Consequências

**Positivas**: Nome reflete escopo real, comunicação mais clara, documentação alinhada com negócio, facilita onboarding, health checks robustos, bounded contexts DDD preservados.

**Negativas**: Coordenação de mudanças em múltiplos ambientes (mitigado com documentação detalhada), possível confusão temporária (mitigado com comunicação proativa), atualização em outros serviços (mitigado mantendo endpoints de API inalterados).
