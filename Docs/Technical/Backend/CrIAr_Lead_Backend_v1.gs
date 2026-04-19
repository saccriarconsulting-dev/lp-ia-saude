/**
 * CrIAr Consulting — Backend Serverless de Captura de Leads
 * Plataforma: Google Apps Script (gratuito, soberano)
 *
 * O que este script faz:
 * 1. Recebe POST com { firstName, email } da Landing Page
 * 2. Grava os dados em uma Google Sheet (banco de dados do lead)
 * 3. Envia e-mail automático ao lead com o link do Whitepaper
 * 4. Retorna JSON { status: "success" } para o frontend redirecionar
 *
 * INSTRUÇÕES DE INSTALAÇÃO no final do arquivo.
 */

// ═══════════════════════════════════════════════
// CONFIGURAÇÃO — Altere apenas aqui se necessário
// ═══════════════════════════════════════════════
const CONFIG = {
  SHEET_ID: "1i_4Wo5y-DQWZ47ziaAlc2QocMbbklAAD_6081uGMfmM",
  SHEET_NAME: "Leads",
  WHITEPAPER_URL: "https://saccriarconsulting-dev.github.io/lp-ia-saude/whitepaper.html",
  SENDER_NAME: "CrIAr Consulting",
  EMAIL_SUBJECT: "Seu Guia de Governança Segura — CrIAr Consulting",
};

// ═══════════════════════════════════════════════
// HANDLER PRINCIPAL — Recebe o POST da Landing Page
// ═══════════════════════════════════════════════
function doPost(e) {
  try {
    // Lê dados vindos de formulário HTML nativo (application/x-www-form-urlencoded)
    const firstName = (e.parameter.firstName || "").trim();
    const email = (e.parameter.email || "").trim();

    if (!firstName || !email) {
      return buildResponse({ status: "error", message: "Campos obrigatórios ausentes." });
    }

    // 1. Gravar na planilha
    saveToSheet(firstName, email);

    // 2. Enviar e-mail com o Whitepaper
    sendWhitepaperEmail(firstName, email);

    return buildResponse({ status: "success" });

  } catch (err) {
    return buildResponse({ status: "error", message: err.toString() });
  }
}

// Permite requisições OPTIONS (preflight CORS)
function doGet(e) {
  return buildResponse({ status: "ok", message: "CrIAr Lead API ativa." });
}

// ═══════════════════════════════════════════════
// GRAVAÇÃO NA PLANILHA
// ═══════════════════════════════════════════════
function saveToSheet(firstName, email) {
  // Conecta diretamente pelo ID da planilha fornecida
  const ss = SpreadsheetApp.openById(CONFIG.SHEET_ID);
  let sheet = ss.getSheetByName(CONFIG.SHEET_NAME);

  // Cria a aba automaticamente se não existir
  if (!sheet) {
    sheet = ss.insertSheet(CONFIG.SHEET_NAME);
    sheet.appendRow(["Data/Hora", "Nome", "E-mail", "Origem"]);
    sheet.getRange("1:1").setFontWeight("bold");
  }

  const timestamp = Utilities.formatDate(new Date(), "America/Sao_Paulo", "dd/MM/yyyy HH:mm:ss");
  sheet.appendRow([timestamp, firstName, email, "Landing Page — Whitepaper IA Saúde"]);
}

// ═══════════════════════════════════════════════
// DISPARO DE E-MAIL AUTOMÁTICO
// ═══════════════════════════════════════════════
function sendWhitepaperEmail(firstName, email) {
  const logoUrl = "https://saccriarconsulting-dev.github.io/lp-ia-saude/logo.png";
  
  const htmlBody = `
    <div style="font-family: 'Outfit', 'Segoe UI', Arial, sans-serif; max-width: 600px; margin: 0 auto; background: #f9f9f9; padding: 20px;">
      <div style="background: #ffffff; border-radius: 16px; overflow: hidden; box-shadow: 0 4px 12px rgba(0,0,0,0.05); border: 1px solid #eee;">
        <!-- Header with Logo -->
        <div style="background: #0e1e40; padding: 40px 30px; text-align: center;">
          <img src="${logoUrl}" alt="CrIAr Consulting" style="width: 180px; height: auto;" onerror="this.style.display='none'">
        </div>

        <div style="padding: 40px 35px;">
          <h2 style="color: #0e1e40; margin: 0 0 20px; font-size: 24px; font-weight: 700;">Olá, ${firstName}!</h2>

          <p style="font-size: 16px; color: #444; line-height: 1.7; margin-bottom: 25px;">
            É um prazer recebê-lo na nossa rede. Como prometido, aqui está o seu acesso exclusivo ao material que ajudará a elevar o nível de segurança e governança na sua prática profissional.
          </p>

          <div style="background: #f0f4f8; border-radius: 12px; padding: 25px; margin-bottom: 30px; text-align: center;">
            <p style="font-size: 15px; color: #0e1e40; font-weight: 600; margin-bottom: 15px;">Guia de Adoção Segura de I.A. na Saúde</p>
            <a href="${CONFIG.WHITEPAPER_URL}"
               style="background: #0e1e40; color: #ffffff; padding: 18px 40px; border-radius: 8px;
                      text-decoration: none; font-weight: 600; font-size: 16px; display: inline-block;">
              Acessar Conteúdo Agora
            </a>
          </div>

          <p style="font-size: 14px; color: #666; line-height: 1.6;">
            Este material foi desenvolvido para converter inovação em segurança real. Se tiver qualquer dúvida sobre a implementação das diretrizes, responda a este e-mail. Nossa equipe técnica terá prazer em ajudar.
          </p>

          <div style="margin-top: 40px; padding-top: 25px; border-top: 1px solid #f0f0f0;">
             <p style="font-size: 14px; color: #0e1e40; font-weight: 700; margin: 0;">Equipe CrIAr Consulting</p>
             <p style="font-size: 12px; color: #888; margin: 4px 0 0;">Governança, Segurança e Inovação em T.I.</p>
          </div>
        </div>
      </div>
      
      <p style="font-size: 11px; color: #aaa; text-align: center; margin-top: 20px;">
        Este é um envio automático solicitado através de nossa Landing Page.<br>
        CrIAr Consulting © 2026. Todos os direitos reservados.
      </p>
    </div>
  `;

  MailApp.sendEmail({
    to: email,
    subject: CONFIG.EMAIL_SUBJECT,
    htmlBody: htmlBody,
    name: CONFIG.SENDER_NAME,
  });
}

// ═══════════════════════════════════════════════
// UTILITÁRIO — Resposta JSON com CORS
// ═══════════════════════════════════════════════
function buildResponse(payload) {
  return ContentService
    .createTextOutput(JSON.stringify(payload))
    .setMimeType(ContentService.MimeType.JSON);
}
