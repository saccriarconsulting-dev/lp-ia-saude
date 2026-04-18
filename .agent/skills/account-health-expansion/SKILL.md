---
name: account-health-expansion
description: Matriz de saúde da conta e identificação técnica de oportunidades de expansão (Upsell).
allowed-tools: Read, Glob, Grep
---

# Account Health & Expansion (CrIAr)

> Metodologia para monitorar a saúde do cliente e identificar o momento exato de expandir o contrato com base em indicadores reais.

## 🌡️ Matriz de Saúde (Health Score)

O CS monitora 4 pilares técnicos para calcular a nota de saúde:

| Pilar | Sinal de Perigo (Churn 🚨) | Sinal de Sucesso (Upsell 🚀) |
|-------|--------------------------|-----------------------------|
| **Incidentes** | Volume alto de bugs de severidade 1. | Zero incidentes críticos por 4 semanas. |
| **Backlog** | Acúmulo de dívida técnica sem ação. | **Backlog crescente de evolução de produto.** |
| **Gargalos** | Time travado por dependência do cliente. | **Necessidade de maior vazão de entregas.** |
| **Engajamento** | Ausência nos ritos de governança. | Stakeholder técnico pedindo novas soluções. |

---

## 🚀 Expansão Baseada em Evidências

Quando o CS identifica **Backlog Alto** ou **Gargalos**, ele deve:
1. **Auditoria de Backlog:** Quantificar o esforço pendente (em horas ou semanas).
2. **Diagnóstico de Gargalo:** Identificar se a causa é infra, falta de gente ou processo.
3. **Proposta Técnica de Expansão:** Apresentar a solução (ex: +1 Squad, Projeto de Refatoração, Automação de QA).
4. **Handoff p/ AE:** Envolver o Executivo de Contas se a expansão exigir uma nova negociação contratual complexa.

---

## 📈 Renovação Consciente

A renovação nunca deve ser "automática". O CS prepara o terreno 60 dias antes com:
- **Relatório de Estabilidade:** Dados de uptime e redução de incidentes.
- **Destaque de Modernização:** O que o código ganhou em qualidade (ex: +% cobertura de testes).
- **Roadmap Futuro:** O que faremos nos próximos 12 meses para evitar obsolescência.

---

## 🛠️ Expansion Checklist

- [ ] Identifiquei o **Gargalo Operacional** atual do cliente?
- [ ] O **Backlog** é grande o suficiente para justificar mais braço técnico?
- [ ] tenho **Dados Concretos** de entrega dos últimos 3 meses?
- [ ] O stakeholder do cliente reconhece o **Valor Técnico** entregue até aqui?
- [ ] Apresentei a expansão como **Solução p/ Produtividade** e não "venda extra"?
