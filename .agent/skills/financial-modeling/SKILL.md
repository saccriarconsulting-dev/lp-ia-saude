---
name: financial-modeling
description: Financial Modeling B2B. DRE, Fluxo de Caixa Direto, e Projetado. Modelagem financeira corporativa para empresas de consultoria e tecnologia com foco em Margem de Contribuição Operacional e EBITDA.
capabilities: [Financial Modeling, P&L, DRE, Margem de Projeto, Ebitda, Cash Flow Forecasting]
---

# Financial Modeling B2B (Financial Controller)

Em uma consultoria de TI (CrIAr Consulting), o produto é estruturalmente baseado em horas/complexidade entregue (cérebros humanos). Sendo assim, a arquitetura financeira difere drasticamente do varejo de produtos físicos. O Controller precisa blindar a margem contra o "vazamento de escopo" que sangra o P&L (Profit and Loss).

## 1. DRE (Demonstrativo do Resultado do Exercício) em TI

O Controller estrutura a saúde da empresa fatiando-a de cima para baixo:
- **Receita Bruta (Top Line):** Total das NFs emitidas (T&M ou Preço Fechado).
- **(-) Impostos Diretos e Diferidos:** ISS, PIS, COFINS, CPB (dependendo do regime).
- **Receita Líquida:** O dinheiro que efetivamente ficou na empresa.
- **(-) CSP (Custo do Serviço Prestado):** Folha de pagamento do squad alocado, ferramentas AWS, SLAs de suporte.
- **= Lucro Bruto / Margem de Contribuição Direta:** *Na CrIAr o sarrafo desta métrica por projeto/cliente é NUNCA negativar, mantendo 5% mínimo sob "Regra do Dobro".*
- **(-) Despesas Operacionais (SG&A):** Salários do Administrativo, Jurídico, Vendas (CAC), Marketing, Aluguéis.
- **= EBITDA:** Geração operacional de Caixa (Proxy do Valor Real do Negócio).
- **= Lucro Líquido (Bottom Line):** APós depreciação, amortização e tributos de IR/CSLL.

## 2. A "Regra do Dobro" (Veto de Risco Financeiro)

As consultorias morrem no Fluxo de Caixa porque fecham contratos com Custo (CSP) muito próximo da Receita Bruta. 
- O Controller da CrIAr não aprova budget onde a folha do Squad consuma 80% do Contrato.
- Ao orçar X dias, joga-se X vezes 2 ("Buffer Financeiro"). O custo oculto está no refatoramento e na triagem de bugs pós deploy. O modelo garante que, mesmo no pior cenário estourando a sprint, a Margem ainda tangencie os 5%. Se houver go-live sem bugs, o buffer extra turbina o EBITDA puro daquele *Quarter*.

## 3. Burn Rate vs Cash Runway

Métricas mortais de sobrevivência que o Controller presta contas ao CEO:
- **Burn Rate:** Quanto a empresa queima de caixa livre mensalmente em despesas fixas para manter a luz acesa, os servidores ligados e a folha sem atraso.
- **Cash Runway:** Se 100% da receita (Contratos vigentes) parar de entrar amanhã, quantos meses de caixa em banco a empresa possui até quebrar? O Cash Runway saudável projetado é de no mínimo 6 meses rotativos.
