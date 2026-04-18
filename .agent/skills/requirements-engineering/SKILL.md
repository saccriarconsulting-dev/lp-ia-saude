---
name: requirements-engineering
description: Agile Requirements Engineering. Mapeamento de regras de negócio, elicitação corporativa, princípios INVEST e Behavior-Driven Development (BDD) para o Business Analyst.
capabilities: [Requirements, User Stories, BDD, Gherkin, Epics, INVEST]
---

# Engenharia de Requisitos Ágil (Business Analyst)

O Business Analyst é o elo, não uma impressora de ordens. O cliente sempre vai entregar a "solução" na cabeça dele. A Engenharia de Requisitos consiste em ouvir a "falsa solução", escavar até a verdadeira "dor" e documentar a "real funcionalidade".

## 1. Do Problema à Épica
- **Nível 1 (The Problem):** A dor raiz do negócio (ex: "Faturamento está demorando muito").
- **Nível 2 (Theme/Epic):** A caixa macro (ex: "Automação de Checkout").
- **Nível 3 (User Story):** O bloco átomo codificável (ex: "Emissão unificada de NF pós-confirmação PIX").

## 2. A Construção de User Stories Válidas (O Teste INVEST)

O BA nunca solta para o TL/Dev uma história que não passe no crivo INVEST:
- **I (Independent):** Não deve depender intrinsecamente do término de outra story (ou devem ser englobadas / planejadas serialmente).
- **N (Negotiable):** A story é uma promessa de conversa, não uma pedra escrita em sangue. O TL pode mudar os "comos".
- **V (Valuable):** Gera um real benefício do ponto de vista do business (não é task técnica de banco, isso é chore).
- **E (Estimable):** Está clara o suficiente para o time alocar "Pontos de Esforço" ou as incertezas são baixas (< 2x gap).
- **S (Small):** Cabe no espaço de uma única sprint.
- **T (Testable):** O QA analyst deve bater os olhos e saber o que considerar como SUCESSO. Se não há DoR (Definition of Ready) o QA não consegue fechar a DoD (Definition of Done).

## 3. Comandos de Gherkin (BDD Framework)

Para alinhamento mortal com QA e Devs, os requisitos críticos são escritos com Behavior-Driven Development.

**Sintaxe Padrão na CrIAr:**
- **Given** (Dado que o estado do sistema é X)
- **When** (Quando o usuário realizar a ação Y)
- **Then** (Então o sistema deve obrigar/produzir Z)

**Exemplo (Aprovação de Reembolso CFO):**
```gherkin
Feature: Alçada de Aprovação CFO em reembolsos de alto valor
  Scenario: Reembolso acima de R$ 10.000,00 trava para alçada
    Given Que o usuário logado está com o role "Gerente"
    When Ele enviar uma despesa de R$ 11.000,00
    Then O sistema deverá registrar status "Pendente_CFO"
    And Deverá disparar Webhook para o Slack do financeiro (#aprovacoes-exec)
```
Isso desmistifica o negócio e já vira espinha dorsal para os testes E2E do QA Analyst.
