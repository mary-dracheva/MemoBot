package com.example.bot.memo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class MemoBotApplication {
    private static final String BOT_NAME = "smpl_reminder_bot";
    private static final String BOT_TOKEN = "7088573793:AAGEsp0bNtnrNjdDUtacHmXqaKFjQ4igFyk";

	public static void main(String[] args) {
		SpringApplication.run(MemoBotApplication.class, args);
		System.out.println("Hello, Mary!!");

        try {
            TelegramBotsApi telegramApi = new TelegramBotsApi(DefaultBotSession.class);
            MemoBot bot = new MemoBot(BOT_TOKEN, BOT_NAME);
			telegramApi.registerBot(bot);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

}
