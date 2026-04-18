---
name: b2b-billing-integration
description: B2B billing and CRM integration protocols for Brazilian SaaS/Consulting (Asaas, Stripe, Iugu).
allowed-tools: Read, Glob, Grep
---

# B2B Billing & Integration Architecture

> Desenho de soluções de faturamento e governança corporativa de MRR/Boleto para integrações B2B brasileiras.

## 🎯 Objetivo da Skill
Capacitar a engenharia da IA a desenhar as soluções de cobrança de serviços de tecnologia (como o Audit Express Health) através de integrações via APIs de Gateways nacionais.

---

## 💸 Gateways de Pagamento e Usos 

| Gateway | Ponto Forte Técnico | Caso de Uso na Vida Real (CrIAr) |
| :--- | :--- | :--- |
| **Asaas** | Emissão nativa de Boleto/Pix + NFe municipal embutida. Setup rápido. | Ideal para o pagamento *One-Off* (Setup Base do Audit Express - R$ 1.990). |
| **Stripe** | Subscrições dinâmicas (MRR), integrações globais e Webhooks confiáveis. | Ideal para o *Assinatura Contínua* de (R$ 790/mês). Cartão B2B. |
| **Iugu** | Operações Split e marketplaces regulatórios. Reconciliação financeira forte. | Ideal para faturamento complexo enterprise quando o CFO exige conta transitória. |

---

## ⚙️ The Technical Billing Flow (Arquitetura Padrão)

**1. Subscription Creation:**
A aplicação escuta o Webhook do Gateway confirmando a aprovação do pagamento para liberar a *Auditoria no Backoffice*.
- NUNCA confie apenas no retorno síncrono do Frontend na API. 
- Apenas Webhooks asinccronos assinados (HMAC) mudam o estado do cliente no banco (ex: Supabase `status: PAID`).

**2. Retentivas e Dunning:**
A integração **deve** possuir régua de cobrança automática: D-3, D0, D+3. 
- *Eventos vitais:* `invoice.payment_failed` (suspender conta pós-carência) vs. `invoice.paid` (restituir acesso).

**3. Tributação em Serviço (Nota Fiscal Eletrônica Nacional):**
- Integrações que não consideram emissão automatizada de RPS/NFS-e quebram na escala. Garantir que as prefeituras locais (via APIs como eNotas/Focus) sejam instanciadas no momento que a transação for concluída.
