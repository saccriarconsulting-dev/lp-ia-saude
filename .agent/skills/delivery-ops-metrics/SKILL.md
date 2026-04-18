---
name: delivery-ops-metrics
description: Software delivery health metrics, bottleneck detection, and operational quality dashboards.
allowed-tools: Read, Glob, Grep
---

# Delivery Ops Metrics (CrIAr Consulting)

> Framework para medir a saúde da entrega de software e identificar degradação antes que ela impacte o cliente.

## 🏆 Métrica-Rei: Índice de Sucesso de Entregas > 95%

Esta é a métrica global da CrIAr. Todas as demais métricas alimentam esta:

| Métrica | O Que Mede | Alerta (🚨) | Saudável (✅) |
|---------|-----------|-------------|--------------|
| **Success Rate** | % de entregas aceitas sem retrabalho. | < 95% | ≥ 95% |
| **Lead Time** | Tempo total do pedido ao deploy. | > 2x a estimativa. | Dentro do planejado. |
| **Cycle Time** | Tempo de desenvolvimento efetivo. | > 70% do Lead Time. | < 50% do Lead Time. |
| **Throughput** | Entregas por sprint/semana. | Queda > 20% em 2 sprints. | Estável ou crescente. |
| **Taxa de Retrabalho** | % de itens devolvidos por bug/escopo. | > 15% | < 10% |
| **Deploy Failure Rate** | % de deploys com rollback. | > 10% | < 5% |
| **MTTR** | Tempo médio de recuperação de incidente. | > 4h (produção). | < 1h |
| **Bugs por Release** | Quantidade de defeitos por entrega. | Crescente por 3 releases. | Estável ou decrescente. |

---

## 🔍 Diagnóstico de Gargalos

Quando uma métrica entra em **Alerta**, o DM investiga a origem:

| Sintoma | Causa Provável | Ação |
|---------|---------------|------|
| Lead Time alto | Requisito mal refinado ou aprovação lenta. | Revisar refinamento e stakeholder SLA. |
| Retrabalho alto | Critérios de aceite vagos ou QA fraco. | Reforçar Definition of Done. |
| Throughput caindo | Gargalo de pessoa ou dependência bloqueante. | **Realocar** recursos entre projetos. |
| Deploy falhando | Pipeline frágil ou falta de testes. | Revisar CI/CD e cobertura de testes. |

---

## 📊 Cadência de Leitura

- **Diária:** Verificar se há bloqueios ativos em sprints.
- **Semanal:** Revisar Throughput e Cycle Time.
- **Quinzenal:** Analisar tendências de Retrabalho e Deploy Failure.
- **Mensal:** Relatório consolidado de Success Rate para o Diretor.

---

## 🛠️ Checklist de Saúde

- [ ] O **Success Rate** está acima de 95%?
- [ ] Há alguma **tendência de degradação** em 2+ sprints consecutivos?
- [ ] Os **gargalos** foram identificados e há ação corretiva em andamento?
- [ ] O relatório mensal foi entregue ao Diretor Comercial/Operacional?
