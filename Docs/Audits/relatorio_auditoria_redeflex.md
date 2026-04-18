# Relatório de Auditoria: Audit Express Health (Teste)
**Projeto:** RedeFlex Mobile (Android)
**Agente Executo:** `@secops-consultant`
**Data/Padrão:** 2026 / OWASP MASVS V2 e V5

---

## 🛑 Dashboard de Risco Termal
**Status:** 🔴 **CRÍTICO** (Não aprovado para o Selo CrIAr)

> [!CAUTION]
> Durante a execução de uma varredura estática de apenas 2 minutos nos códigos do pacote `.zip` extraído, detectamos graves violações de segurança relativas ao armazenamento e tráfego de rede.

---

## 🔍 Descobertas Técnicas (Actionable Insights)

### 1. [VULNERABILIDADE CRÍTICA] Vazamento de Segredo no Código (Hardcoded Secret)
* **Pilar:** MASVS-STORAGE (V2)
* **Localização:** `shared/util/Utilidades.java : Linha 249`
* **Código Ofensor:** 
  ```java
  public static String secretToken = "933421BB0EFD59C15F003EBAC073E0D2938635F91F42C5D95A956E54D15A00E5";
  ```
* **Risco:** Qualquer ferramenta básica de engenharia reversa de APK extrai essa chave imediatamente. Se ela for usada para autenticação de APIs críticas ou criptografia, o atacante ganha o controle.
* **Solução:** Remover a string hardcoded. Mover segredos para serem injetados no tempo de compilação via `local.properties` (escondido do repositório) ou obter via Keystore segura.

### 2. [VULNERABILIDADE ALTA] Vazamento de Dados Sensíveis via Logging
* **Pilar:** MASVS-NETWORK (V5) / LGPD Privacy
* **Localização:** `shared/network/ApiClient.java : Linhas 99 a 101`
* **Código Ofensor:**
  ```java
  HttpLoggingInterceptor logger = new HttpLoggingInterceptor();
  logger.setLevel(HttpLoggingInterceptor.Level.BODY);
  httpBuilder.addInterceptor(logger);
  ```
* **Risco:** Injetar o nível `BODY` em produção faz com que **todo** o tráfego HTTP (incluindo senhas digitadas, tokens JWT de sessão e dados pessoais de clientes) seja gravado no Logcat do Android. Qualquer aplicativo malicioso com permissão de leitura de logs (ou um cabo USB conectado) pode capturar a sessão e fazer o *hijack* da conta.
* **Solução:** Configurar o Logging condicionalmente.
  ```java
  if (BuildConfig.DEBUG) {
      logger.setLevel(HttpLoggingInterceptor.Level.BODY);
  } else {
      logger.setLevel(HttpLoggingInterceptor.Level.NONE);
  }
  ```

### 3. [VULNERABILIDADE MÉDIA] Ausência de Configuração Segura de Rede e SSL Pinning
* **Pilar:** MASVS-NETWORK (V5)
* **Localização:** Repositório (`res/xml/network_security_config.xml` inexistente).
* **Risco:** Sem o `network_security_config.xml` configurado, o aplicativo não restringe tráfego HTTP em claro nativamente (dependendo do SDK alvo) e, mais criticamente, aprova qualquer certificado root presente no dispositivo (abrindo margem severa para ataques *Man-in-The-Middle* corporativos ou em Wi-Fis públicos).
* **Solução:** Adicionar o XML bloqueando `<domain-config cleartextTrafficPermitted="false">` e aplicar proteção TLS rigorosa.

---

### Resumo do Consultor:
O RedeFlex implementou defesas muito boas (notei a classe `SharedPreferenceEncryptedImpl` e o design do `ApiClient` Singleton), mas detalhes cruciais de ambiente (*logging* e *hardcoded tokens*) quebraram toda a cadeia de segurança. Este é o exato cenário do cliente alvo da CrIAr: tem as ferramentas, mas não tem o *SecOps* corporativo configurado.
