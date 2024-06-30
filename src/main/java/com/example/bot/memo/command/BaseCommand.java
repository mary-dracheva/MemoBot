package com.example.bot.memo.command;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class BaseCommand extends BotCommand {
    private String fullDescription;

    public BaseCommand(String name, String description, String fullDescription) {
        super(name, description);
        this.fullDescription = fullDescription;
    }

    public BaseCommand(String name, String description) {
        super(name, description);
        this.fullDescription = description;
    }

    public void execute(AbsSender sender, long chatId) {
        SendMessage sendMsg = new SendMessage();
        sendMsg.setChatId(chatId);
        sendMsg.setText(fullDescription);
        sendMessage(sender, sendMsg);
    }

    protected void sendMessage(AbsSender sender, SendMessage msg) {
        try {
            sender.execute(msg);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
    }
}
