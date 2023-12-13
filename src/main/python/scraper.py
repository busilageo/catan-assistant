from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from bs4 import BeautifulSoup
import requests
import sys
import time
import re



# Open the colonist.io game
page_to_scrape = webdriver.Chrome()
game_code = sys.argv[1].split('/')[-1]
page_to_scrape.get(f"https://colonist.io/{game_code}")

# Get id of the game
game_id = sys.argv[2]

# URL for post requests
url = f"http://localhost:8080/api/games/{game_id}/request"

# Wait and press the "Accept cookies" button
try:
    wait = WebDriverWait(page_to_scrape, 15)
    button = wait.until(EC.element_to_be_clickable((By.CSS_SELECTOR, "#qc-cmp2-ui > div.qc-cmp2-footer.qc-cmp2-footer-overlay.qc-cmp2-footer-scrolled > div > button.css-47sehv > span")))
    button.click()
except Exception as e:
    print("Accept Error: ", str(e))
    sys.stdout.flush()

# Wait for the chat to load
try:
    wait = WebDriverWait(page_to_scrape, 120)
    wait.until(EC.presence_of_element_located((By.CLASS_NAME, "message-post")))
except Exception as e:
    print("Error First Loading: ", str(e))
    sys.stdout.flush()

# Announce that scraper is ready
requests.post(url=url, data="Ready!")

# Converts and prints/sends the chat messages
def print_new_messages(html_messages):
    ok = ""
    for html_message in html_messages:
        html_content = html_message.get_attribute("outerHTML")
        html_content = re.sub(r'<img (.*?)>', r'<img \1></img>', html_content)
        soup = BeautifulSoup(html_content, 'html.parser')
        desired_span = soup.find('span', class_ = '')
        
        line = ""

        for child in desired_span:
            if hasattr(child, 'name') and child.name == "img":
                alt_text = child.get("alt")
                if alt_text:
                    line += alt_text + " "
            else:
                text = child.text
                if text:
                    line += text + " "
            if hasattr(child, 'src') and ("placed a" in line):
                img_src = child.get("src")
                if img_src:
                    line += img_src + " "
        
        line = re.sub(r'\s+', ' ', line)
        
        data = line
        if not line:
            data = "New Round"
            
        response = requests.post(url=url, data=data) 
        if response.status_code != 200:
            print(f'POST request failed with status code {response.status_code}')
        #if line:
        #    print(line.strip())
        #    ok = line
        #else:
        #    print("New Round")
        #sys.stdout.flush()
        ok = line
        if (ok.strip() == "trophy"):
            break
    return ok

# Check if chat has got a new message
prev_messages = set()
def has_chat_updated(driver, previous_message_count):
    current_message_count = len(page_to_scrape.find_elements(By.CLASS_NAME, "message-post"))
    return current_message_count > previous_message_count

# Main loop
while True:
    # Check if page has lost connection and refresh it
    try:
        button = page_to_scrape.find_element(By.CSS_SELECTOR, "#top-notification-container > div > a")
        button.click()
        print("Clicked the Refresh button.")
        sys.stdout.flush()
    except:
        pass
    
    # Gets only the new messages
    def check_for_updates():
        current_messages = page_to_scrape.find_elements(By.CLASS_NAME, "message-post")
        new_messages = [message for message in current_messages if message not in prev_messages]
        return new_messages
    new_messages = check_for_updates()
    
    # Update the previous messages to include the new ones
    for message in new_messages:
        prev_messages.add(message)
    
    # Print new messages
    if new_messages:
        if (print_new_messages(new_messages).strip() == "trophy"):
            print("Game ended")
            break
    
    # A delay just to be safe, in case messages are getting spammed
    time.sleep(2)

page_to_scrape.quit()