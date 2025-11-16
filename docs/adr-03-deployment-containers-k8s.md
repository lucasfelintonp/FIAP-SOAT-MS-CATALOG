```markdown
# ADR 03 — Deploy: containers e orquestração com Kubernetes para integração e produção

Status: Proposto

Data: 2025-11-15

## Contexto

O projeto será executado em containers e integrado com um cluster orquestrado para homologação/produção. Para testes locais e integração contínua, é necessário alinhar com as práticas do workspace (uso de `kind`/`minikube`/`k3d` para validação em cluster).

## Decisão

Construir imagens Docker a partir do artefato Maven (`fastfood-0.0.1-SNAPSHOT.jar`) e adotar deploy em Kubernetes para homologação e produção. Para desenvolvimento local, suportar Docker Compose para ciclos rápidos e `kind`/`k3d`/`minikube` para testes de orquestração.

## Motivação

- Permite validar comportamentos de orquestração (configmaps, secrets, readiness/liveness, escalonamento) antes de promoção para produção.
- Garante consistência com outros serviços que usam Kubernetes no workspace.
- Facilita integração com pipelines CI que executem testes de integração em cluster.

## Alternativas consideradas

1. Apenas Docker Compose — bom para dev rápido, mas insuficiente para validar orquestração e comportamento em cluster.
2. Serverless (Lambda) — não aplicável ao microserviço atual que requer estado e conexões persistentes.

## Implicações operacionais

- Necessidade de builds de imagem no CI e registro (ECR/registry privado) e políticas de versionamento/tagging.
- Scripts de deploy e manifests (`k8s/` ou helm charts) e documentação para operação.
- Requisitos para observabilidade (metrics, tracing), configuração de recursos (requests/limits) e políticas de segurança (network policies, secrets).

## Plano de implementação

1. Adicionar `Dockerfile` (ou ajustar existente) com multi-stage build para gerar imagem enxuta.
2. Incluir pipeline CI job que:
   - builda o artefato via `./mvnw package`
   - constrói e publica a imagem no registry do projeto
   - executa testes unitários e testes de integração em `kind` (quando ativado)
3. Criar manifest básico Kubernetes em `infra/k8s/` e exemplos `values.yaml`/`helm` para deploy.
4. Documentar no `README` e em `docs/` como executar localmente com Docker Compose e como provisionar cluster `kind` para integração.
5. Revisar após primeira implantação em staging e ajustar recursos/limits e healthchecks.

```
