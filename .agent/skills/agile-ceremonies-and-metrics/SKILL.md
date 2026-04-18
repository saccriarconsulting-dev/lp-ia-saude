---
name: agile-ceremonies-and-metrics
description: Agile Ceremonies and Metrics. Metodologia pragmática para governança de Sprints, Velocity, Burndown e cadência rítmica de entregas para o Project Manager.
capabilities: [Agile, Scrum, Kanban, Sprint Governance, Metrics, Burndown, Cycle Time]
---

# Cerimônias Ágeis e Métricas de Delivery (Project Manager)

Agilidade sem governança métrica é apenas caos iterativo. A CrIAr Consulting não vende "sprints infinitas", ela vende previsibilidade (95% de ETA garantido) ancorada na "Regra do Dobro". O papel do PM nessas cerimônias é remover névoa e proteger o fluxo.

## 1. Cadência das Cerimônias

### Sprint Planning (O Contrato da Iteração)
- **Foco:** O que entregaremos ao final destas 2 semanas e como?
- **Gatilho CrIAr:** Usar Story Points (Fibonacci) para esforço bruto, mas converter taticamente verificando a "Regra do Dobro" (se a feature leva 3 dias reais, a banda alocada na Sprint deve suportar 6 dias de capacity para cobrir riscos).
- **Anti-pattern:** Começar a codão sem Critério de Aceite (DoD) assinado pelo Business Analyst.

### Daily Standup (O Alarme Tático)
- **Foco:** Impedimentos (Blockers).
- **Gatilho CrIAr:** Qualquer impeditivo relatado na Daily deve virar uma task de bloqueio com o DM ou C-Level acionado em menos de 15 minutos pós-reunião. A Daily não é "status report de chefe", é desobstrução.

### Sprint Review (O Palco)
- **Foco:** Vender a entrega ao cliente / stakeholders. Demostrar software funcionando (não PPTs).
- **Gatilho CrIAr:** O CS e o Billing Analyst ficam em background anotando o momento exato em que o cliente diz "OK, aprovado" para gerar o faturamento da milestone.

### Sprint Retrospective (O Feedback de Sangue)
- **Foco:** O que deu certo, o que quebrou, o que automatizar no próximo ciclo.
- **Gatilho CrIAr:** A retro não é sessão de terapia. Cada problema levantado PRECISA gerar 1 ticket no backlog da próxima sprint para garantir melhoria (ex: Quebrou a pipeline? Ticket = Implementar GitHub Actions Caching).

## 2. Métricas de Saúde (A Verdade Nu Crua)

1. **Velocity (Velocidade):** O desvio padrão da equipe. Se a equipe entrega 40 pontos em média, não prometa 50. Use a média móvel das últimas 3 sprints.
2. **Burndown Chart:** O gráfico deve descer gradualmente. Se ele for plano o tempo todo e desabar "tudo de uma vez" na quinta-feira, significa que os devs estão codando em silos sem fazer merges pequenos. Risco de quebra no último dia.
3. **Cycle Time & Lead Time:** Desde o dia que a task foi pra "In Progress" até o "Done". O objetivo do PM é apertar o gargalo (ex: a task fica 3 dias em *Code Review* esperando o Tech Lead? Aí está o problema de Cycle Time). 
