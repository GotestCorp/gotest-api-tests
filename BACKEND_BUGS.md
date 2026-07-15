# Defeitos de backend encontrados pela suíte de API

Defeitos reais da API goTest detectados pelos testes automatizados. Os testes
correspondentes são mantidos **vermelhos de propósito** (não relaxamos os asserts)
para que voltem a passar automaticamente quando o backend corrigir o comportamento.

> Validado ao vivo em **2026-06-15** — suíte completa: 47 testes, 2 falhas (ambas abaixo).
> Revalidado em **2026-07-14** (nova versão do sistema) — 47 testes, 1 falha: **BUG-2 foi corrigido pelo backend** (suite inexistente agora responde corretamente, teste `nonExistingSuite_shouldReturn404` passou). BUG-1 continua aberto.
> Reconfirmado ao vivo em **2026-07-15** — ver nota de falso-positivo no BUG-1 abaixo.

---

## BUG-1 — Criação de teste aceita título vazio (deveria ser 400/422)

- **Endpoint:** `POST /projects/{projectId}/suites/{suiteId}/tests` (multipart)
- **Cenário:** payload válido, porém com `title` = `""` (string vazia).
- **Esperado:** `400 Bad Request` ou `422 Unprocessable Entity` (título é obrigatório).
- **Obtido:** `200 OK` — o caso de teste é criado com título vazio.
- **Impacto:** permite dados inconsistentes; ausência de validação de campo obrigatório.
- **Teste:** `CreateTestCaseTest.emptyTitle_shouldReturn400`
- **Nota (2026-07-15) — falso-positivo nas pipelines de 2026-07-15T01:0x–01:3x:** o teste
  apareceu como **verde** nessas execuções, mas não porque o backend passou a validar o
  título. O próprio teste tinha um bug: quando o 200 do BUG-1 dispara, ele nunca capturava
  o `id` criado pra limpeza (`@AfterMethod` só deleta se `createdTestCaseId != null`), então
  um registro órfão com `title=""` ficou parado na suite fixture (APIT). Nas execuções
  seguintes, tentar criar outro teste com `title=""` esbarrava numa regra de **nome
  duplicado** e o backend respondia `422 "This test name is already in use"` — um código
  que satisfaz o assert (`isIn(400, 422)`) sem ter relação nenhuma com validação de título
  obrigatório. Revalidado ao vivo em 2026-07-15: apagando o registro órfão e repetindo a
  chamada, o backend voltou a aceitar título vazio com `200` — **BUG-1 confirmado ainda
  aberto**. O teste foi corrigido para capturar o `id` sempre que a criação "funcionar"
  (200/201), independente do assert, evitando que a suite fixture acumule lixo e mascare
  o bug de novo no futuro.

## BUG-2 — Suite inexistente retorna 500 (deveria ser 403/404) — ✅ CORRIGIDO em 2026-07-14

- **Endpoint:** `POST /projects/{projectId}/suites/{suiteId}/tests` (multipart)
- **Cenário:** `suiteId` = `00000000-0000-0000-0000-000000000000` (UUID inexistente).
- **Esperado:** `404 Not Found` (ou `403 Forbidden`).
- **Obtido (2026-06-15):** `500 Internal Server Error` — erro não tratado em vez de resposta de cliente.
- **Obtido (2026-07-14):** teste passou — backend agora responde com código de cliente adequado.
- **Impacto:** erro 5xx vazando para o cliente em entrada inválida; falta de tratamento.
- **Teste:** `CreateTestCaseTest.nonExistingSuite_shouldReturn404`
- **Relacionado:** mesmo padrão observado em `GetProjectByIdTest.invalidId` (500 em id inválido) — esse ainda está aceitando 500 como válido no assert (ver teste), não foi revalidado como corrigido.

---

### Status dos testes
Os dois testes acima permanecem como _expected failures_ até a correção no backend.
Quando o backend ajustar os status codes, os testes passarão sem nenhuma alteração de código.
