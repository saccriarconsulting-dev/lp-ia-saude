# Ficha Comercial: Audit Express Health 🛡️
**Produto by CrIAr Consulting** | **Versão:** 1.0 (2026)

---

## 🎯 Proposta de Valor
*"Não deixe sua intuição tecnológica comprometer seu registro profissional. Transforme o Vibe Coding amador em confiança clínica certificada."*

**Público-Alvo:** Psicólogos, Clínicas e Profissionais de Saúde (médio/pequeno porte).
**Problema:** A popularização de assistentes de IA (Claude Code, Cursor) permitiu a criação de soluções próprias, mas introduziu riscos invisíveis gravíssimos (vazamento de API Keys, Prompt Injection, tratamento irregular sob LGPD).
**Solução:** Uma auditoria rápida (48h), focada com precisão cirúrgica no código fonte e na arquitetura, resultando no **Selo CrIAr de Segurança**.

---

## 🔬 Escopo da Auditoria (O que fazemos)

A análise divide-se em 3 pilares técnicos e regulatórios:

### 1. Security Scan (Código e Infraestrutura)
*   **Detecção de Segredos:** Varredura em busca de *API Keys*, Senhas em Texto Claro e *Tokens* no código ou repositório público/privado (ex: chaves da OpenAI, Anthropic, Supabase).
*   **Verificação de Criptografia:** Auditoria de transporte (*in transit* - TLS 1.3) e armazenamento (*at rest* - AES-256) dos dados dos pacientes.
*   **Controle de Acesso:** Verificação rápida de fluxos de autenticação (JWT) para impedir que dados vazem para outros inquilinos (pacientes visualizando dados errados).

### 2. Governança de IA (Base NIST AI RMF / OWASP)
*   **Análise contra Injeção de Prompt (Prompt Injection):** Testes para garantir que inputs maliciosos não forcem o LLM a revelar o System Prompt ou dados de terceiros (o risco nº 1 da OWASP Top 10 LLM 2025).
*   **Privacidade na Borda:** Verificação do modelo usado. Os dados alimentados estão sendo usados para treinamento da IA (Opt-out verificado)? 

### 3. Compliance Digital (LGPD & Conselhos Profissionais)
*   **Anonimização Básica:** A aplicação mascarilha PII (Personal Identifiable Information) antes de enviar para processamento de IA externo?
*   **Transparência:** Verificação dos termos de aceite (consentimento explícito para uso de IA segundo a lei).

---

## 📦 Entregáveis

O cliente não recebe um relatório complexo com jargão técnico inútil para psicólogos. A entrega é direta:

1. **Dashboard de Risco Termal ("Sinal de Trânsito"):** 
   - 🔴 Crítico (Impede lançamento / Perigo LGPD)
   - 🟡 Médio (Requer adequação próxima)
   - 🟢 Seguro.
2. **Relatório de Correção Rápida (Actionable Insights):** Linha do código e como arrumar (copie e cole a correção).
3. **O Selo CrIAr de Segurança Digital:** Um ativo visual (ícone SVG/PNG) acompanhado de certificado digital assinado pela CrIAr, para ser exposto no site da clínica ou interface do software.

---

## ⏱️ SLA e Dinâmica Operacional
* **SLA de Entrega:** **48 horas úteis** a partir do recebimento do acesso ao repositório ou envio dos arquivos da aplicação.
* **Modelo Operacional:** Auditoria remota estática (SAST/DAST leves + Revisão Humana de Especialista SecOps). O cliente adquire o pacote, fornece o acesso ao GitHub/GitLab (ou envia o .zip) e recebe o dossiê.

---

## 💰 Precificação e Esteira de Upsell (A Estratégia CrIAr)

A precificação segue o modelo de escada de valor (*Value Ladder*). O objetivo não é lucrar de forma abusiva na primeira interação, mas sim criar um relacionamento consultivo de longo prazo com a clínica.

### Opção 1: Setup Base (A Porta de Entrada)
- **Foco:** Diagnóstico Estático V1 ("Tirar a foto atual").
- **Escopo:** Auditoria completa da versão atual da ferramenta. Entrega do Dashboard de Risco Termal e Relatório de Correção.
- **Investimento Sugerido:** **R$ 1.990,00** (Pagamento único).
- **Incluso:** Chamada de 45 minutos (Handoff) para explicar o risco aos proprietários.

### Opção 2: Pacote de Reassessment (O Check de Sucesso)
- **Foco:** Validação pós-correção.
- **Escopo:** O cliente compra o Setup Base, sua equipe corrige os erros apontados, e a CrIAr realiza uma reavaliação oficial para emitir o **Selo CrIAr de Segurança** final.
- **Investimento Sugerido:** **R$ 2.890,00** (Setup Base + 1 Reassessment incluído validificado em até 30 dias).
- **Início do Vínculo de Confiança:** Demonstra que a CrIAr não quer apenas apontar o erro, mas garantir que a solução esteja cravada na segurança.

### Opção 3: Assinatura de Avaliações Periódicas (O Longo Prazo)
- **Foco:** C-Levels Críticos ou Ferramentas com integração contínua (onde o código muda toda semana).
- **Escopo:** Ao invés de uma foto estática, a CrIAr monitora o projeto do cliente. Avaliação mensal contínua pós-deploy (verificando novos vazamentos de chaves ou quebras do NIST) associada a um Health Status em tempo real.
- **Investimento Sugerido:** **R$ 790,00 / mês** (Retenção CrIAr).
- **Vantagem:** Previsibilidade de faturamento MRR (Monthly Recurring Revenue) para a CrIAr e blindagem legal contínua (exigida pela LGPD) para a clínica parceira.
