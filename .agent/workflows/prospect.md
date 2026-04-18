# Workflow: Prospecção Técnica (/prospect)

Este workflow automatiza a pesquisa inicial e qualificação de uma conta para o SDR/BDR.

## Fase 1: Inteligência de Conta (OSINT)
- **Ação**: O agente usa `search_web` para pesquisar a empresa.
- **Busca por**:
    - Vagas de TI abertas (para identificar a stack).
    - Presença em App Store / Play Store.
    - Notícias recentes sobre tecnologia ou expansão.
    - Perfis de liderança técnica no LinkedIn.

## Fase 2: Diagnóstico de Fit Técnico
- O agente `sdr-bdr` cruza os dados encontrados com a **Matriz de Veto** da CrIAr.
- Avalia o risco de sucesso e a curva de aprendizado necessária.

## Fase 3: Estruturação para CRM
- O agente gera um bloco de texto formatado para o **HubSpot Free** contendo:
    - **Dor Técnica Provável**.
    - **Stack Observada**.
    - **Nível de Maturidade**.
    - **Sinal de Urgência**.
    - **Veredito**: (Qualified ✅ / Discarded ❌).

## Fase 4: Abordagem Inicial
- Sugestão de uma mensagem de abordagem "problem-first" personalizada para o decisor identificado.

---

> [!TIP]
> Use este comando para economizar 30-40 minutos de pesquisa manual por lead.
