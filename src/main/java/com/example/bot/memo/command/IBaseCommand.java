package com.example.bot.memo.command;

import org.telegram.telegrambots.meta.bots.AbsSender;

public interface IBaseCommand {
    void execute(AbsSender sender, long chatId, String args);
    void callback(AbsSender sender, long chatId, CallbackData data);
    String getName();
    String getDescription();

}
