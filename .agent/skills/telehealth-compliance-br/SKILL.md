---
name: telehealth-compliance-br
description: Telehealth compliance BR. Normas técnicas e éticas do CFP (Resoluções 01/2009 e 11/2018) para plataformas de saúde e prontuários eletrônicos na LGPD.
allowed-tools: Read, Glob, Grep
---

# Telehealth Compliance BR (Gestão em Saúde Digital)

> Restrições técnico-éticas do Conselho Federal de Psicologia (CFP) e CFM para aplicações SaaS e Plataformas de Telessaúde operantes no Brasil.

## 🎯 Objetivo da Skill
Capacitar o agente SecOps e o DPO (Data Protection Officer) com os minúsculos limites legais do atendimento em saúde digital no Brasil. Garantir que as arquiteturas recomendadas não apenas passem no MASVS e LGPD, mas na inspeção formal de Conselhos de Classe.

---

## ⚖️ Marco Regulatório Crítico
As aplicações (Vibe Coding, Prontuários, Dashboards Clínicos) **PRECISAM** possuir os seguintes requisitos arquiteturais:

### 1. Sigilo Terapêutico em Nível de Banco de Dados
- **CFP Resolução nº 11/2018:** É obrigatório atestar a quebra zero do meio de comunicação. 
- *Aplicação Tech:* Plataformas de vídeo ou prontuários na AWS não podem ter criptografia centralizada com a chave controlada pelo proprietário ("Zero-Knowledge Encryption" é o ideal).

### 2. Temporalidade e Destruição de Prontuário
- Os profissionais têm a guarda do prontuário por pelo menos **5 anos**.
- *Aplicação Tech:* O sistema deve ter travas contra **deleção acidental (Soft Delete obrigatório)** de arquivos de pacientes. Contudo, em casos de "Direito ao Esquecimento" (LGPD), deve haver script validado de purga completa para encerramento de vínculo que supere os prazos de guarda em conflito.

### 3. Opt-in para Tratamento Algorítmico (LLM)
- Uso de IA em saúde (resumos gerados por Claude/ChatGPT) **não pode** ser ativado pelo médico sem notificar o banco subjacente e o consentimento explícito do paciente de que a gravação/transcrição está ocorrendo.

---

## 🛡️ Checklist DPO CFP/CFM
- [ ] A aplicação hospeda os dados de saúde no território nacional ou nuvem certificada (ISO 27001)?
- [ ] Os prompts submetidos aos modelos (OpenAI) possuem máscara automática de CPF, nome e CID (Classificação Internacional de Doenças)?
- [ ] A plataforma tem log inalterável de quem acessou e quando acessou o prontuário para provar compliance em auditorias do conselho regional (CRP)?
