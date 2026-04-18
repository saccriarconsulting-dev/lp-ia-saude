---
name: technical-capacity-planning
description: Team dimensioning, bottleneck mapping, and allocation strategy based on seniority and stack complexity.
allowed-tools: Read, Glob, Grep
---

# Technical Capacity Planning (CrIAr Consulting)

> Metodologia para dimensionar times com base na realidade técnica, não em contagem de cabeças.

## 🧮 A Regra de Equivalência Técnica

"2 Juniors ≠ 1 Sênior." O DM deve usar esta tabela de capacidade real:

| Perfil | Capacidade Efetiva | Fator de Risco | Quando Usar |
|--------|-------------------|----------------|-------------|
| **Sênior** | 1.0x (Referência). | Baixo. Autogerenciável. | Arquitetura, integrações, decisões críticas. |
| **Pleno** | 0.6x do Sênior. | Médio. Precisa de revisão. | Desenvolvimento core com supervisão. |
| **Junior** | 0.3x do Sênior. | Alto. Curva de aprendizado. | Tarefas bem definidas, testes, suporte. |

### Exemplo Prático
- Demanda estimada: 160h de esforço sênior.
- Com 1 Sênior + 2 Plenos = 1.0 + 0.6 + 0.6 = 2.2x → ~73h de calendário.
- Com 1 Sênior + 3 Juniors = 1.0 + 0.3 + 0.3 + 0.3 = 1.9x → ~84h + risco de revisão.

---

## 🗺️ Mapeamento de Gargalos (9 Fontes)

Quando o Throughput cai, investigue sistematicamente:

| # | Fonte | Indicador | Ação |
|---|-------|-----------|------|
| 1 | **Requisito** | Histórias sem critério de aceite. | Devolver para PO/Analista. |
| 2 | **Arquitetura** | Decisões técnicas em aberto. | Escalar para SE/Tech Lead. |
| 3 | **Ambiente** | Staging instável ou sem acesso. | Solicitar infra ao cliente (via CS). |
| 4 | **Qualidade/QA** | Ausência de testes ou regressão manual. | Investir em automação. |
| 5 | **People Bottleneck** | 1 pessoa concentra conhecimento. | **Realocar** ou fazer pair programming. |
| 6 | **Integração** | API de terceiro instável. | Documentar bloqueio e isolar dependência. |
| 7 | **Aprovação do Cliente** | Aceite demorado. | CS/AM pressiona o stakeholder. |
| 8 | **Débito Técnico** | Código legado travando evolução. | Planejar sprint de refatoração. |
| 9 | **Pipeline/DevOps** | CI/CD lento ou quebrando. | Priorizar automação de build/deploy. |

---

## 📋 Dimensionamento por Tipo de Demanda

| Tipo de Demanda | Composição Ideal | Risco Principal |
|----------------|-----------------|-----------------|
| **Projeto Greenfield** | 1 Sênior (Arch) + 2 Plenos. | Subestimar complexidade. |
| **Sustentação** | 1 Pleno + 1 Junior (rotativo). | Acúmulo de débito técnico. |
| **Integração Complexa** | 2 Sêniores (mínimo). | Dependência de terceiros. |
| **Refatoração de Legado** | 1 Sênior (avaliação) + 1-2 Plenos. | Ausência de testes. |

---

## 🛠️ Capacity Checklist

- [ ] O time está dimensionado pela **capacidade real** (não por headcount)?
- [ ] Os **gargalos** ativos estão mapeados e com ação corretiva?
- [ ] Há **concentração de conhecimento** em uma única pessoa?
- [ ] A **alocação** está alinhada com a complexidade da stack do projeto?
