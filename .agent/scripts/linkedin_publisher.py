import os
import requests
import argparse
from pathlib import Path

# Tentativa de carregamento amigável via python-dotenv, caso presente
try:
    from dotenv import load_dotenv
    load_dotenv()
except ImportError:
    pass

# =========================================
# CONFIGURAÇÕES DA API CrIAr
# =========================================
ACCESS_TOKEN = os.getenv("LINKEDIN_ACCESS_TOKEN")
ORGANIZATION_URN = os.getenv("LINKEDIN_ORGANIZATION_URN")
BASE_FOLDER = Path(__file__).resolve().parent.parent.parent / "Docs" / "Marketing" / "Sprint_1"

HEADERS = {
    "Authorization": f"Bearer {ACCESS_TOKEN}",
    "Content-Type": "application/json",
    "X-Restli-Protocol-Version": "2.0.0"
}

def clean_copy_for_linkedin(md_content):
    """ Filtra o pacote de edição, isolando apenas o texto puro da rede social """
    delimiter = "## ✍️ Parte 2: O Texto da Publicação (Copy)"
    if delimiter in md_content:
        # Pega tudo depois da quebra, ignorando as dicas de Canva
        copy_text = md_content.split(delimiter)[1].strip()
        # Remove * (itálicos soltos do markdown que quebram o parser basico caso desejado)
        return copy_text
    return md_content.strip()

def upload_image(image_path):
    """ Fase 1: Registra a imagem (Assets) na API V2 do LinkedIn """
    print(f"[*] Registrando Imagem: {image_path.name}...")
    register_url = "https://api.linkedin.com/v2/assets?action=registerUpload"
    
    register_payload = {
        "registerUploadRequest": {
            "recipes": ["urn:li:digitalmediaRecipe:feedshare-image"],
            "owner": ORGANIZATION_URN,
            "serviceRelationships": [{"relationshipType": "OWNER", "identifier": "urn:li:userGeneratedContent"}]
        }
    }
    
    reg_response = requests.post(register_url, headers=HEADERS, json=register_payload)
    if reg_response.status_code != 200:
        print("[!] Erro fatal no registro de imagem:", reg_response.text)
        return None
        
    data = reg_response.json()
    upload_url = data['value']['uploadMechanism']['com.linkedin.digitalmedia.uploading.MediaUploadHttpRequest']['uploadUrl']
    asset_urn = data['value']['asset']
    
    # Faz Upload Físico em raw binary
    print("[*] Subindo binários (Upload)...")
    with open(image_path, 'rb') as img:
        upload_resp = requests.put(upload_url, data=img, headers={"Authorization": f"Bearer {ACCESS_TOKEN}"})
        if upload_resp.status_code != 201:
            print("[!] Erro no Upload físico:", upload_resp.text)
            return None
            
    return asset_urn

def create_post(text_content, asset_urn=None):
    """ Fase 2: Publica o texto final empacotado e amarrado à imagem """
    share_url = "https://api.linkedin.com/v2/ugcPosts"
    
    share_media_category = "IMAGE" if asset_urn else "NONE"
    media_array = [{"status": "READY", "media": asset_urn}] if asset_urn else []
    
    post_payload = {
        "author": ORGANIZATION_URN,
        "lifecycleState": "PUBLISHED",
        "specificContent": {
            "com.linkedin.ugc.ShareContent": {
                "shareCommentary": {
                    "text": text_content
                },
                "shareMediaCategory": share_media_category,
                "media": media_array
            }
        },
        "visibility": {
            "com.linkedin.ugc.MemberNetworkVisibility": "PUBLIC"
        }
    }
    
    print("[*] Disparando Payload Oficial para o Feed...")
    resp = requests.post(share_url, headers=HEADERS, json=post_payload)
    if resp.status_code == 201:
        print("✅ SUCESSO ABSOLUTO! Publicação criada na página!")
        print("ID da Publicação:", resp.headers.get("x-restli-id"))
    else:
        print("❌ FALHA NA PUBLICAÇÃO:", resp.text)

def run_publisher(post_number):
    if not ACCESS_TOKEN or not ORGANIZATION_URN:
         print("❌ ERRO: Acesso negado. As variáveis locais no '.env' não foram encontradas.")
         print("Por favor, cole as chaves extraídas do Portal de Desenvolvedores do LinkedIn.")
         return
         
    print(f"--- 🚀 CrIAr LinkedIn Publisher | Targeting Post #{post_number} ---")
    
    # Procura a pasta correspondente ao número
    target_folder = None
    for folder in sorted(BASE_FOLDER.iterdir()):
        if folder.is_dir() and f"Post_{post_number}" in folder.name:
            target_folder = folder
            break
            
    if not target_folder:
        print(f"❌ Pasta para o Post_{post_number} não localizada em {BASE_FOLDER}")
        return
        
    print(f"[*] Escaneando artefatos na pasta: {target_folder.name}")
    md_files = list(target_folder.glob("*.md"))
    img_files = list(target_folder.glob("*.png")) + list(target_folder.glob("*.jpg"))
    
    if not md_files:
        print("❌ Arquivo de Copy (.md) não encontrado.")
        return
        
    raw_copy = md_files[0]
    final_text = clean_copy_for_linkedin(raw_copy)
    print("\n[Preview Copy]")
    print(final_text[:100] + "...\n")
    
    asset_urn = None
    if img_files:
        print(f"[*] Arte Detectada ({img_files[0].name}). Iniciando protocolo MultiMedia...")
        asset_urn = upload_image(img_files[0])
    
    create_post(final_text, asset_urn)


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Automatizador de Posts do LinkedIn")
    parser.add_argument("--post", type=int, required=True, help="Número do post do Sprint 1 (ex: --post 1)")
    args = parser.parse_args()
    
    run_publisher(args.post)
