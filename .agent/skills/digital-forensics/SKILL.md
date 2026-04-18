---
name: digital-forensics
description: Fundamentos de forense digital para auxílio ao Incident Responder em triagem e extração segura da cadeia de custódia.
capabilities: [Digital Forensics, Incident Response, Chain of Custody, Volatile Memory, Artifact Analysis]
---

# Fundamentos de Forense Digital (Incident Responder)

Ao contrário de "formatar e reinstalar", a abordagem profissional mediante a um incidente de cibersegurança envolve o congelamento do cenário criminoso/infeccioso. A CrIAr precisa saber onde o atacante mexeu para remediar de forma certeira e provir evidências sólidas caso haja interface regulatória (ANPD/DPO) e jurídica (Criminal).

## 1. Regras da Cadeia de Custódia (Chain of Custody)

Nenhum arquivo ou dump é valioso nos tribunais (ou auditorias) se não for provado que ele permaneceu intocado.
- Toda coleta (imagem de disco, logs) exige a extração de um **Hash (SHA-256)** gerado exato no momento zero do dump.
- Salvar log de auditoria documentando "Quem pegou, que horas puxou, e em que pendrive armou".

## 2. Ordem de Volatilidade (O que coletar primeiro)

Nunca remova o cabo da força de um servidor atacado sem antes avaliar a coleta em ordem decrescente de perdas voláteis:
1. **Memória RAM:** Colete processos, senhas e injetores mapeados em memória (Exibição de C2 e payloads na memória antes do kill).
2. **Conexões de Rede Vivas e Tabela de Roteamento.**
3. **Estado de Logs de Registros Locais (Windows Event Logs / Syslogs).**
4. **Disco Rígido Completo (Cold Disk Image).**

## 3. Investigação em Três Pistas Centrais

- **Conexões (Pivoteamento):** Quais IPs esse servidor está chamando nas últimas 24 hrs que o SIEM não rastreia por padrão histórico?
- **Persistência:** Ele não infectou sem querer ficar. O IR precisa checar `cronjobs`, `systemctl services`, chaves de Registro de *Run/RunOnce* do Windows e tarefas agendadas (Scheduled Tasks).
- **Contas Paralelas:** Verificação por usuários ocultos, recém-criados localmente ou contas órfãs com privilégios reavivados dentro do Active Directory.

*A missão na Resposta a Incidentes é responder às perguntas: 'Quem fez? O que tocaram? O que levaram? Estão presencialmente ainda no cluster?'*
