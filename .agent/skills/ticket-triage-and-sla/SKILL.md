---
name: ticket-triage-and-sla
description: Ticket Triage and SLA Management. Triagem operacional de incidentes e suportes (Nível 1/2), priorização matricial e governança de Service Level Agreements.
capabilities: [Support, SLAs, L1/L2 Troubleshooting, Incident Prioritization, Escalation Matrices]
---

# Triagem de Tickets e Governança de SLA (Support Analyst)

O Nível 1/Nível 2 da CrIAr Consulting não é um robô de respostas prontas. É o funil tático operado pelo Support Analyst para blindar o tempo de engenharia dos Squads (TL/Dev). Resolver rápido desobstrui o Hub Operacional e garante o pagamento das manutenções mensais sem quebra de SLA.

## 1. Matriz de Priorização de Incidentes (Urgency vs Impact)

Quando um chamado entra, a prioridade nunca é definida pelo Pânico do Cliente, é aferida matricialmente:

| IMPACTO / URGÊNCIA | Urgência Alta (Paga agora) | Urgência Baixa (Pode esperar o ciclo) |
|--------------------|----------------------------|--------------------------------------|
| **Impacto Alto (Sistema fora)** | P1 - Crítico (Core business travado) | P2 - Alto (Queda de redundância) |
| **Impacto Baixo (1 usuário)** | P3 - Médio (Funcionalidade isolada) | P4 - Baixo (Dúvida pontual) |

*Nota CrIAr:* **P1 exige Notificação Executiva**. Se é um P1 e afetará faturamento, o Delivery Manager é arrastado para a "Sala de Crise". 

## 2. Governança de SLA (A Promessa de Contrato)

Existem duas réguas em operação que nunca podem cruzar a linha da quebra:

1. **FRT (First Response Time):** "Sua requisição foi recebida. Estamos analisando os logs do server XYZ". A garantia de que a CrIAr sabe do problema (Geralmente < 30min para P1). 
2. **TTR (Time to Resolution):** O relógio da volta do sistema à operação nominal ou com workaround funcional (mitigado).

> A quebra dessas métricas, documentadas em contrato, pode resultar em multas contratuais (SLA Penalties). O CISO e Corporate Lawyer são diretamente alertados.

## 3. Playbook de Triage Ágil (O Funil L1 -> Squad)

Evite o "*passei pro dev só pra ele dar uma olhada*".

**Ações obrigatórias antes da escalaço (Escalation to L3):**
1. Recriar na própria máquina/ambiente do Support. Se replicar, o problema é real.
2. Analisar o Log Trace ID referenciado na chamada. 
3. Identificar o *Commit Tracker*: o erro surgiu logo pós-Deploy recente? Se sim, é incidente provocado (Rollback imediato sugerido ao DevOps).
4. O ticket é escalado ao Tech Lead com o "Ticket Envelope" completo (Passos de root cause tentados, prints, logs e browser network traces anexados). 
