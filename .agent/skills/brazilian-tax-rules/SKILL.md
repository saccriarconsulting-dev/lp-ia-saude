---
name: brazilian-tax-rules
description: Regras Fiscais Brasileiras e Retenções (Tax / Billing Analyst). Regimes tributários (Simples/Presumido), NF-e serviços e a matemática punitiva de retenções de IRRF/CSRF em B2B de TI.
capabilities: [Tax Compliance B2B, NFS-e, Retenções Tributárias, ISS, PIS, COFINS]
---

# Regras Fiscais Brasil e Retenções B2B (Tax & Billing Analyst)

No Brasil corporativo, a emissão da NF de software (Serviços) nunca é uma linha reta. Faturar R$ 10.000,00 nem sempre significa que o banco da CrIAr vai receber 10k. Metade da receita vaza nas entrelinhas das Retenções na Fonte e nos tributos indiretos (ISS, PIS, COFINS).

## 1. Regimes Tributários Aplicados (TI/Software)

A carga da empresa depende do guarda-chuva jurídico em que ela opera.
- **Simples Nacional (Anexo III ou V para Software):** Toda a guia paga num boleto unificado (DAS). Porém as alíquotas sobem agressivamente com o ganho de faturamento base. (Importante a matemática do "Fator R" de Custo de Folha, visando empurrar do Anexo V para o Anexo III, que é mais barato).
- **Lucro Presumido:** Tributação por estimativa federal (IRPJ / CSLL presume margem de ~32% sobre o software) + Custo Cheio nos municipais (ISS) + Federais Diretos (PIS/COFINS = 3,65%). É o padrão de consultorias após quebrarem o limite do Simples.

## 2. Retenções Tributárias (O Dinheiro Descontado "na Fonte")

Quando a CrIAr emite NFs de Consultoria e Construção de Software para Bancos e Megacorporações (Enterprise), a regra exige Retenção Tributária. O cliente DECONTA e paga o imposto pela CrIAr, depositando no banco apenas "o líquido".

Se o Billing Analyst esquecer de inserir isso no fechamento, o cliente glosa a NF ou paga errado, afetando o fluxo de caixa:
*Nota: as alíquotas variam de acordo com o CNAE Exato de serviço de computação faturado.*
1. **ISS:** Geralmente até 5%. Retido e pago ao município do TOMADOR (cliente) ou PRESTADOR, variando conforme Lei Complementar 116 e cadastro de CPOM dependendo da cidade do cliente B2B. A malha mais perigosa.
2. **IRRF (Imposto de Renda PF na Fonte):** Serviços tipicamente corporativos exigem recolhimento primário pela tesouraria de origem na casa dos 1,5%.
3. **PIS/COFINS/CSLL (vulgo CSRF ou retenção da pauta 4,65%):** Segurado e descontado no ato do pgto de consultoria se a CrIAr não for Simples Nacional.

## 3. O Fato Gerador e Fluxo de Emissão

O *Billing Analyst* valida o evento final:
1. **Approval do Go-live ou Aprovação Mensal do CS.** Não emite NF sem o "Aceite Formal" do cliente anexado (Auditoria contra risco de inadimplência/descarte de serviços cancelados).
2. O CNAE (Classificação da Atividade Econômica) embutido no XML da prefeitura dita as taxas supracitadas na emissão. Errou o Classificador = Multa e Passivo. 
