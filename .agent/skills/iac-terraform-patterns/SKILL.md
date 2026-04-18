---
name: iac-terraform-patterns
description: IaC Terraform Patterns. Padrões arquiteturais e templates corporativos para infraestrutura como código, focados em segurança, modularidade, e reuso (DevOps Engineer).
capabilities: [Terraform, IaC, Terragrunt, State Management, Cloud Infrastructure, SecOps]
---

# IaC Terraform Patterns (DevOps / Cloud Engineer)

A fundação cloud da CrIAr Consulting não é operada pelo console da AWS/GCP (ClickOps). É criada via código (IaC - Infrastructure as Code). O uso rigoroso de Terraform garante a replicabilidade, o controle de versão peer-revewed e a recuperação de desastres.

## 1. Padrões de Qualidade CrIAr (The Golden Rules)
- **Nenhum State Local:** Estado do Terraform é sagrado. Configurado com S3 Bucket (versioned) + DynamoDB para State Locking (ou equivalentes GCP/Azure).
- **Hardcodes são Proibidos:** Se tiver ARN, Subnet ID ou Account ID hardcoded no `.tf` principal, a MR é negada. Usar `data sources` ou `.tfvars`.
- **DRY (Don't Repeat Yourself):** Se uma infra de container é necessária para cliente A e cliente B, constroem-se "Módulos Oficiais". Ocasionalmente usando `Terragrunt` para orquestração de módulos secos.

## 2. Abstração de Ambientes (Isolamento)

Sempre isole por folders de forma estrutural (e evite Terraform Workspaces quando a governança de acesso for pesada):

```text
/infrastructure
  /modules        # Os blocos padrão da fábrica
    /rds-psql-hardening
    /eks-cluster
  /environments   # A infra implantada nos clientes
    /dev
    /staging
    /prod
      main.tf
      terraform.tfvars
```

## 3. Integração Contínua em Infraestrutura (GitOps CI)

Nenhuma pipeline roda `terraform apply` em main às cegas. As gates obrigatórios do DevOps Engineer são:

| Passo Pipeline | Ferramenta | Propósito |
|----------------|------------|-----------|
| **1. Linting** | `terraform fmt -check` | Manter padrão unificado de linguagem. |
| **2. Scanners**| `tfsec` ou `checkov` | Encontrar baldes S3 abertos, falta de IAM limits, etc (Check de Segurança). |
| **3. Plan** | `terraform plan` | Postar o output da modificação nos comentários da PR / MR via bot para auditoria visual humana. |
| **4. Apply** | `terraform apply -auto-approve` | Rodar em main APENAS após o Merge Request ser aprovado (CISO/Tech Lead). |

## 4. Gerenciamento de Segredos de Banco / Cloud

- O Terraform não gera chaves expostas no `.tfstate`.
- **Regra de Ouro:** Configurar provedores para ejetar as senhas do `aws_rds_cluster` e injetar diretamente no **AWS Secrets Manager / Vault**. A aplicação conectará consumindo a API de segredos, evadindo completamente qualquer risco de code leak.
