import telebot
from telebot.types import ReplyKeyboardMarkup, KeyboardButton

bot = telebot.TeleBot("8942067798:AAFU01Yqjo4KJi3GYX07JUYbyK1d8SGjU-Q")

@bot.message_handler(commands=['start'])
def send_welcome(message):
    markup = ReplyKeyboardMarkup(resize_keyboard=True)
    # Екі түйме
    markup.add(KeyboardButton("📸 Камераны көру"), KeyboardButton("📍 Локацияны алу"))
    bot.reply_to(message, "Басқару тақтасы:", reply_markup=markup)

@bot.message_handler(func=lambda message: True)
def handle_commands(message):
    if message.text == "📸 Камераны көру":
        bot.reply_to(message, "Камера сигналы жіберілді...")
    elif message.text == "📍 Локацияны алу":
        bot.reply_to(message, "Локация сұралуда...")

bot.infinity_polling()

