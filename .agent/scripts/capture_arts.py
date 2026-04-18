import os
from playwright.sync_api import sync_playwright

HTML_PATH = "file:///C:/Users/renato.torelli/Antigravity%20Projects/CrIAr%20Consulting/Docs/Marketing/CriAr_Arte_Social_Cards.html"
OUT_DIRS = [
    "Docs/Marketing/Sprint_1/Sem_1_Post_1_Manifesto",
    "Docs/Marketing/Sprint_1/Sem_1_Post_2_Inovacao",
    "Docs/Marketing/Sprint_1/Sem_2_Post_3_VibeCoding",
    "Docs/Marketing/Sprint_1/Sem_2_Post_4_OWASP",
    "Docs/Marketing/Sprint_1/Sem_3_Post_5_NIST",
    "Docs/Marketing/Sprint_1/Sem_3_Post_6_TeaserWP",
    "Docs/Marketing/Sprint_1/Sem_4_Post_7_LancamentoWP",
    "Docs/Marketing/Sprint_1/Sem_4_Post_8_AuditExpress"
]

def main():
    # Caminho raiz
    root_dir = os.path.dirname(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
    
    with sync_playwright() as p:
        browser = p.chromium.launch()
        page = browser.new_page()
        print(f"Accessing {HTML_PATH}")
        page.goto(HTML_PATH)
        
        # Reset scale for 1080x1080 High Res capture
        page.evaluate("() => { document.querySelectorAll('.linkedin-post').forEach(el => { el.style.transform = 'none'; el.style.marginBottom = '20px'; }); }")
        
        posts = page.locator('.linkedin-post').all()
        for i, post in enumerate(posts):
            if i >= len(OUT_DIRS): break
            dir_path = os.path.join(root_dir, OUT_DIRS[i].replace("/", os.sep))
            out_file = os.path.join(dir_path, f"card_post_{i+1}.png")
            print(f"[*] Gerando arte VIP: {out_file}")
            post.screenshot(path=out_file)
            
        browser.close()
        print("✅ Captura concluída em formato Real Pixel.")

if __name__ == "__main__":
    main()
