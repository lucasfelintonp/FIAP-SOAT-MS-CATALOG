# CI / Workflows — Secrets e permissões necessárias

Este documento lista os *secrets* e configurações que você precisa adicionar no repositório `FIAP-SOAT-MS-CATALOG` para que os workflows funcionem corretamente.

Resumo rápido (copiar/colar para "New repository secret")

- `SONAR_TOKEN` -> token do Sonar (SonarCloud ou SonarQube)
- `SONAR_HOST_URL` -> URL do Sonar (ex.: https://sonarcloud.io)
- `SONAR_PROJECT_KEY` -> projectKey do projeto no Sonar

Observações importantes

- `GITHUB_TOKEN` já é provisionado automaticamente pelo GitHub Actions; não é necessário criar manualmente. Contudo, verifique as permissões do workflow nas configurações do repositório (veja abaixo).
- Removi integrações com Slack conforme solicitado — nenhum secret de Slack é requerido atualmente.

Detalhes de cada secret

1. SONAR_TOKEN

- O que: token de autenticação do Sonar (ou do SonarCloud).
- Onde gerar: SonarCloud -> My Account -> Security -> Generate Token, ou SonarQube -> Tokens.
- Uso: autenticar análise Sonar e consultar Quality Gate via API.

2. SONAR_HOST_URL

- O que: URL base da instância Sonar (ex.: `https://sonarcloud.io` ou `https://sonarqube.yourcompany`).
- Uso: parâmetro `-Dsonar.host.url` e chamadas para a API (`/api/qualitygates/project_status`).

3. SONAR_PROJECT_KEY

- O que: identificador (projectKey) do projeto no Sonar.
- Onde ver: nas configurações do projeto no Sonar ou no `sonar-project.properties` se existir.

Como adicionar um secret

1. Abra o repositório no GitHub.
2. Vá em Settings → Secrets and variables → Actions.
3. Clique em "New repository secret".
4. Cole o nome exatamente (case-sensitive) e o valor. Salve.

Permissões de workflow (recomendado)

- Repository → Settings → Actions → General → Workflow permissions:
  - Se quiser publicar imagens no GHCR com `GITHUB_TOKEN`, selecione "Read and write permissions".
  - Se preferir segurança adicional, crie um PAT com scopes mínimos e salve como `GHCR_PAT` e ajuste os workflows para usá-lo.

Proteção de branch

- Em Settings → Branches, configure protections (por ex. em `main`): exigir checks (ex.: `Integration`) antes do merge e exigir PR aprovado por X revisores.

Testando após configuração

1. Crie os secrets conforme acima.
2. Abra um PR com uma mudança trivial (ex.: README) — o workflow `Integration` deve rodar.
3. Se os secrets `SONAR_*` estiverem presentes, a etapa de Sonar rodará e fará polling do Quality Gate.
4. Fazer merge em `main` (ou push em `main`) acionará o workflow `release.yml` que constrói e publica a imagem no GHCR.
