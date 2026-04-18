---
name: nps-and-churn-prevention
description: NPS and Technical Churn Prevention. Métricas de sucesso, Health Score, e metodologias para medição proativa e reativa de satisfação (NPS/CSAT) na entrega contínua B2B corporativa.
capabilities: [Customer Success, Churn Prediction, Health Score, Retention Strategies, CSAT/NPS]
---

# NPS & Churn Prevention (Account Management / CS)

Em consultoria B2B, um churn (cancelamento) dificilmente é repentino; ele dá sinais prévios meses antes através da quebra de comunicação ou atraso de projetos. A skill de Prevenção de Churn monitora esses sintomas e aplica táticas de resgate ativo (Save Plays).

## 1. O Tripé de Saúde da Conta (Health Score)

Ao invés de perguntar "O cliente está feliz?", o CS / Account Manager mede:

1. **Uso Técnico (Delivery Health):** O escopo faturado x entregue está alinhado? O Lead Time to Change diminuiu com nossa consultoria? O número de bugs injetados está subindo?
2. **Engajamento B2B:** Executivos do cliente (Sponsor) ainda aparecem nos repasses mensais (EBRs)? O cliente aprova as faturas de medição no prazo?
3. **Sentiment (NPS/CSAT):** Mensuração qualitativa. O NPS relacional a cada semestre responde se a consultoria agrega valor.

**Fórmula Básica de Health Score (Sinal Verde/Amarelo/Vermelho):**
- **Verde:** Entrega QA OK + Sponsor engajado na Daily/Weekly + Pagamento em dia.
- **Amarelo:** Bugs bloqueantes recentes OU o PM do cliente começa a microgerenciar o Tech Lead.
- **Vermelho:** Sponsor não aprova faturas OU comunicação com DM é hostil. Alerta de Churn altíssimo.

## 2. Aferição do NPS Relacional (Net Promoter Score)

- Frequência B2B ideal: Mínimo a cada 6 meses com Sponsor / Head técnico (Quarterly) ou após o final de um milestone chave.
- O foco da pesquisa não é a nota fria ("De 0 a 10"), **e sim a pergunta qualitativa**: "O que motivou a sua nota?".
- **Tratamento de Detratores (0 a 6):** Não é "mandar e-mail pedindo desculpas". É fazer *Close-The-Loop*: ligar, entender de fato o atrito (geralmente escopo que não alinhou) e propor um *Plano de Correção de Rota (PCR)*.

## 3. Playbooks de Churn Prevention (Save Plays)

Quando o Health Score ou o NPS batem um *Cenário Laranja ou Vermelho*, aciona-se um play estrutural rápido:

### Playbook 1: Ghosting do Sponsor
- **O Problema:** O líder técnico do cliente desapareceu, não homologa e nossas entregas acumulam.
- **A Tática:** Escalada multi-nível. CEO fala com CEO, não AM fala com Sponsor. É preciso reconfirmar se a iniciativa/prioridade corporativa mudou. O *Delivery Manager* é paralisado até haver green light de aprovação. O Risco de Glosa/Faturamento fica altíssimo.

### Playbook 2: Crise de Entrega (Muitos Bugs ou Atraso)
- **O Problema:** Culpa própria. Erramos no dimensionamento e a qualidade caiu.
- **A Tática:** Assumir o BO ("Mea Culpa"). Acionar o *Tech Lead* e o *Delivery Manager* para elaborar o *RCA (Root Cause Analysis)* com evidências. Trazer a transparência máxima antes mesmo de o cliente reclamar verbalmente. "Encontramos o gargalo no fluxo de CI/CD que travou as entregas ontem. Aqui o plano com SLA de 48h de mitigação".

### Playbook 3: Queda Pós-Adoção (Loss of Value)
- **A Tática:** Quando a entrega final (Go-Live) gerou efervescência, mas depois de 3 meses o cliente acha "que ficou caro de manter". Voltar ao mapa de "Regra do Dobro" e ROI de oportunidade. Redesenhar escopo de *Sustentação/Suporte (Squad Mínimo)* alocado sem perder Margem de 5%.

> *A métrica mãe é a Net Revenue Retention (NRR). CS não é apenas fazer as pessoas darem sorrisos, é fazer a CrIAr continuar gerando caixa recorrente e expansão em MRR técnico.*
