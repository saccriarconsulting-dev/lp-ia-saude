---
name: llm-security-owasp
description: OWASP Top 10 para Large Language Models. Vetores de ataque focados contra implementações de IA e estratégias de mitigação arquitetural.
capabilities: [LLM Security, OWASP, Prompt Injection, Data Leakage, AI Governance, Guardrails]
---

# Segurança em LLMs e OWASP Top 10 (AI Strategist / CISO)

Implementar Inteligência Artificial no lado corporativo (com dados vivos dos clientes) introduz uma superfície de ataque completamente nova. A CrIAr Consulting não pluga APIs de OpenAI ou Gemini sem aplicar as proteções arquiteturais do OWASP Top 10 para LLMs.

## 1. Mapeamento de Ataques Principais (OWASP LLM)

1. **Prompt Injection (LLM01):** O atacante insere instruções ocultas no input que "vazam" das diretrizes do seu `system prompt`, fazendo a IA executar ações maliciosas ou revelar dados restritos.
2. **Insecure Output Handling (LLM02):** A aplicação confia cegamente no texto retornado pela IA (ex: executando um script SQL gerado via prompt ou renderizando um script no browser = XSS provocado por IA).
3. **Training Data Poisoning (LLM03):** Se fizermos Fine-Tuning de IA com dados não validados da internet ou documentação aberta da empresa, o atacante envenena os dados originais.
4. **Sensitive Information Disclosure (LLM06):** A IA regurgita segredos, senhas ou PII (dados pessoais) de um cliente para outro, ferindo a LGPD.

## 2. Padrões de Arquitetura Segura (Guardrails)

- **Sanitização Dupla:** Todo output de um LLM corporativo passa por uma sanitização clássica (regex/encoders) antes de ser consumido por um usuário ou API de banco.
- **Isolamento de RAG (Retrieval-Augmented Generation):** Uma IA só pode acessar o banco de vetores que pertença EXCLUSIVAMENTE ao Role/Hierarchy do usuário requisitante (IAM Data Isolation). Se o usuário não tem permissão para ler relatórios financeiros no ERP, o Agent também não pode ter.
- **LLM Firewall / Guardrails:** Inserir um modelo menor apenas para vigiar o input e o output. (ex: LlamaGuard validando os retornos do GPT-4 na porta de saída).

## 3. Privacidade como Código (Data Leakage Prevention)

Nenhum dado real e sensível (PII, CPFs, Tokens) deve viajar no payload em plain-text para endpoints de LLMs externos. Utilizam-se mascaradores dinâmicos que embaralham informações confidenciais antes do trânsito corporativo, reconstituindo o output internamente na volta.
