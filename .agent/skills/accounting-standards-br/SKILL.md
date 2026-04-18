---
name: accounting-standards-br
description: Accounting Standards BR (Accountant). Normas contábeis brasileiras (CFC/CPC), conciliação avançada, reconhecimento de receita em software e balanço patrimonial.
capabilities: [General Accounting, CFC/CPC norms, Partidas Dobradas, Reconhecimento de Receita (IFRS-15)]
---

# Padrões Contábeis Brasil (Accountant)

A contabilidade oficial não serve apenas para pagar tributos, ela é a linguagem universal descrita pelas leis (Conselho Federal de Contabilidade - CFC) para validar o valor fiduciário de longo prazo da CrIAr para auditores externos e bancos. Todo *Financial Analyst* atua com Fluxo de Caixa (presente). O *Accountant* opera a real visão da estabilidade no tempo (Competência).

## 1. O Alinhamento Sagrado: Competência vs Caulxa

- **O Viso Financeiro (Caixa):** O dinheiro bateu na conta (extrato) dia 05 de março, e a conta da folha saiu dia 03. 
- **O Viso Contábil (Competência):** A nota da prestação de serviços ocorreu dia 24 de Fevereiro. Logo, a Receita pertence oficialmente no Livro de Razão ao mês de Fevereiro. O salário pago dia 05 de março diz respeito aos dias trabalhados na sprint de Fevereiro. Custos e Receita de Fevereiro casaram contábilmente independentemente da distorção temporal em banco real.

## 2. Padrão IFRS-15 em Software (Reconhecimento de Receita Pragmática)

Como a CrIAr reconhece ganhos de projetos fechados em Sprints que cruzam meses?
- A norma universal de software dita que a receita deve ser alinhada ao repasse real do "título e controle de risco de transferência". Só computamos e escrituramos o pedaço do software que for validado e recebido (Aceite Formal de Milestones no JIRA / CS).
- A prestação de *horas de consultoria ativas* que ainda não fecharam a sprint/entrega entram como WIP (Work In Progress) ou custos ativados e não puramente despesa se a natureza for criadora de valor intelectual corporativo direto.

## 3. Dinâmica das Partidas Dobradas (Escrituração Perfeita)

Sempre equalizando *Ativos = Passivos + Patrimônio Líquido*. Todo registro gera dois vetores: 
- Ex: Crumb comprar 1 servidor cloud fixo local a praça no mês. 
- *Débito* da Entrada do Maquinário (Ativo Imobilizado/Intangível - Capitalizando Custos).
- *Crédito* saindo da Conta Corrente Bancária D+0 (Saída). 

## 4. O Coração: Provisões para a Era PT-BR (CLT e Impostos Ocultos)
Um balanço desonesto não embute os buracos futuros criados no mês atual.
O Accountant registra, independente do caixa imediato:
- Provisão Mensal (1/12) de 13º Salário e Férias para todos os DEV's alocados do Squad CLT. O custo mensal em `financial-modeling` só faz sentido se os 40% incidentes estagnados da folha estiverem diluídos na provisão de passivo de Dezembro de antemão.
