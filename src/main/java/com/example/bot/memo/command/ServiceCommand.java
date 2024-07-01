package com.example.bot.memo.command;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;

public class ServiceCommand extends BaseCommand {
    private String fullDescription;

    public ServiceCommand(String name, String description, String fullDescription) {
        super(name, description);
        this.fullDescription = fullDescription;
    }

    public ServiceCommand(String name, String description) {
        super(name, description);
        this.fullDescription = description;
    }

    @Override
    public void execute(AbsSender sender, long chatId, String args) {
        SendMessage sendMsg = new SendMessage();
        sendMsg.setChatId(chatId);
        sendMsg.setText(fullDescription);
        sendMessage(sender, sendMsg);
    }

    @Override
    public void callback(AbsSender sender, long chatId, CallbackData data) {
    }

    public void setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
    }
}
