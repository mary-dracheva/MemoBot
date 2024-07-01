package com.example.bot.memo.command;

import com.example.bot.memo.JsonUtil;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.time.LocalDate;
import java.util.*;

public class RemindCommand extends BaseCommand {
    private HashMap<Long, MemoInfo> tempMemos;

    public RemindCommand(String name, String description) {
        super(name, description);
        this.tempMemos = new HashMap<>();
    }

    @Override
    public void execute(AbsSender sender, long chatId, String args) {
        MemoInfo memo = new MemoInfo(chatId, args);
        tempMemos.put(chatId, memo);
        askWhenRemind(sender, chatId);
    }

    @Override
    public void callback(AbsSender sender, long chatId, CallbackData data) {
        switch (data.getCallbackType()) {
            case DATE -> askDate(sender, chatId);
            case TIME -> askTime(sender, chatId);
            case TODAY -> {
                tempMemos.get(chatId).setDate(LocalDate.now());
                askTime(sender, chatId);
            }
        }
    }

    private void askWhenRemind(AbsSender sender, long chatId) {
        SendMessage msg = new SendMessage(String.valueOf(chatId), "When should I remind you?");
        setButtons(msg, Arrays.asList(CallbackType.TODAY, CallbackType.DATE));
        sendMessage(sender, msg);
    }

    private void askDate(AbsSender sender, long chatId) {
        SendMessage response = new SendMessage(String.valueOf(chatId), "Please, input date");
        setInline(response, "dd.MM.yy");
        sendMessage(sender, response);
    }

    private void askTime(AbsSender sender, long chatId) {
        SendMessage response = new SendMessage(String.valueOf(chatId), "Please, input time");
        setInline(response, "HH:mm");
        sendMessage(sender, response);
    }

    private void setInline(SendMessage response, String pattern) {
        ReplyKeyboardMarkup menu = new ReplyKeyboardMarkup();
        response.setReplyMarkup(menu);
        KeyboardButton btn = new KeyboardButton("OK");
        KeyboardRow row = new KeyboardRow(List.of(btn));
        menu.setInputFieldPlaceholder(pattern);
//        menu.setIsPersistent(false);
        menu.setOneTimeKeyboard(true);
        menu.setKeyboard(List.of(row));

    }

    public boolean isInputContinue(AbsSender sender, Message msg) {
        long chatId = msg.getChatId();
        if (tempMemos.containsKey(chatId)) {
            String text = msg.getText();
            MemoInfo memo = tempMemos.get(chatId);
            if (memo.hasDate()) {
                memo.setTime(text);
                //TODO: finish input send memo to Reminder
                System.out.println("Save memo: " + memo.getMessage() + "\n" + memo.getDateTime());
            } else {
                memo.setDate(text);
                askTime(sender, chatId);
            }
            return true;
        }
        return false;
    }

    private void setButtons(SendMessage msg, List<CallbackType> btnTypes) {
        List<InlineKeyboardButton> buttons = btnTypes.stream().map(t -> {
            InlineKeyboardButton btn = new InlineKeyboardButton(t.getTitle());
            CallbackData data = new CallbackData(getCommand(), t);
            btn.setCallbackData(JsonUtil.toJson(data));
            return btn;
        }).toList();
        List<List<InlineKeyboardButton>> keyboard = Collections.singletonList(buttons);
        InlineKeyboardMarkup menu = new InlineKeyboardMarkup(keyboard);
        msg.setReplyMarkup(menu);
    }
}

