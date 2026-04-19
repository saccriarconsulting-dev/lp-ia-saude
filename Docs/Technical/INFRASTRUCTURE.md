# Infraestrutura Técnica: Funil de Leads CrIAr (Abril/2026)

Este documento serve como a "Fonte da Verdade" para manutenções futuras no sistema de captura de leads.

## 📍 Localização dos Componentes

| Componente | Localização / Link |
| :--- | :--- |
| **Landing Page (Frontend)** | `index.html` (Raiz do repositório) |
| **Lógica do Formulário** | Script no final do `index.html` (Técnica Iframe) |
| **Backend (Google Apps Script)** | `Docs/Technical/Backend/CrIAr_Lead_Backend_v1.gs` |
| **Endpoint Live (Web App URL)** | `https://script.google.com/macros/s/AKfycbwT6HzBkhi5kMJJORXD0KLyzDfnnBGMEsgaZ8X-hsu6tjSDuzKSGuseTfkpCyfIgZ4/exec` |
| **Banco de Dados (Leads)** | [Google Sheet - CrIAr Leads](https://docs.google.com/spreadsheets/d/1i_4Wo5y-DQWZ47ziaAlc2QocMbbklAAD_6081uGMfmM/) |

## ⚙️ Configurações Críticas (GitHub Pages)

Para o site funcionar corretamente, as configurações no painel do GitHub **precisam** estar assim:
- **Build and Deployment > Source**: `Deploy from a branch`
- **Branch**: `main` (Pasta: `/ (root)`)
- **Bypass Jekyll**: Arquivo `.nojekyll` presente na raiz para evitar erros de compilação.

## 🛠️ Como fazer manutenção no Backend (Google)

Se precisar alterar o corpo do e-mail ou a planilha de destino:
1. Abra o [Editor do Apps Script](https://script.google.com/home).
2. Atualize o código.
3. **MUITO IMPORTANTE**: Clique em **Implantar > Gerenciar implantações > Editar > Versão: "Nova Versão" > Implantar**.
   - *Se você apenas salvar e não gerar uma "Nova Versão", o Google continuará executando o código antigo.*

## 🔒 Segurança e Soberania
- **Autenticação**: O script roda com as permissões da sua conta (Renato), gravando na sua planilha e enviando do seu e-mail.
- **Acesso**: O Web App está configurado como "Acesso: Qualquer pessoa" para permitir que leads anônimos enviem dados, mas o código contém travas de validação de campos.
