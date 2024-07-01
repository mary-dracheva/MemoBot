package com.example.bot.memo.command;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public abstract class BaseCommand extends BotCommand implements IBaseCommand {
    public BaseCommand(String name, String description) {
        super(name, description);
    }

    public abstract void execute(AbsSender sender, long chatId, String args);

    public abstract void callback(AbsSender sender, long chatId, CallbackData data);

    public String getName() {
        return getCommand();
    }

    protected void sendMessage(AbsSender sender, SendMessage msg) {
        try {
            sender.execute(msg);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
