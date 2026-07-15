#!/usr/bin/env bash
# Renova o secret AUTH_TOKEN usado pela pipeline (.github/workflows/api-tests.yml).
#
# Os JWTs da API goTest expiram em ~24h, então esse secret precisa ser
# atualizado manualmente todo dia útil antes de contar com a pipeline.
#
# Uso:
#   ./scripts/refresh-ci-token.sh <jwt>
#   ./scripts/refresh-ci-token.sh          # pede o token via prompt (não fica no history do shell)
#
# Requer: gh CLI autenticado (gh auth login) com acesso de admin ao repo.

set -euo pipefail

REPO="GotestCorp/gotest-api-tests"
TOKEN="${1:-}"

if [ -z "$TOKEN" ]; then
  read -rsp "Cole o JWT novo: " TOKEN
  echo
fi

if [ -z "$TOKEN" ]; then
  echo "Nenhum token informado. Abortando." >&2
  exit 1
fi

# Validação leve: um JWT tem 3 partes separadas por ponto.
if [ "$(echo "$TOKEN" | tr -cd '.' | wc -c)" -ne 2 ]; then
  echo "O valor informado não parece um JWT (esperado header.payload.signature)." >&2
  exit 1
fi

gh secret set AUTH_TOKEN --repo "$REPO" --body "$TOKEN"
echo "Secret AUTH_TOKEN atualizado em $REPO."
echo "Lembrete: esse token expira em ~24h — rode este script de novo amanhã antes de depender da pipeline."
