package com.example.bot.memo.command.memo;

import com.example.bot.memo.JsonUtil;
import com.example.bot.memo.command.BaseCommand;
import com.example.bot.memo.command.CallbackData;
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
    private Reminder reminder;
    private AbsSender sender;

    public RemindCommand(String name, String description) {
        super(name, description);
        this.tempMemos = new HashMap<>();
        this.reminder = new Reminder();
        runChecker();
    }

    private void runChecker () {
        Runnable task = () -> {
            while (true) {
                List<MemoInfo> memos = reminder.readyToSend();
                memos.forEach(memo -> sendMessage(sender,
                        new SendMessage(String.valueOf(memo.getChatId()), memo.getMessage())));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        Thread thread = new Thread(task);
        thread.start();

    }

    @Override
    public void execute(AbsSender absSender, long chatId, String args) {
        MemoInfo memo = new MemoInfo(chatId, args);
        tempMemos.put(chatId, memo);
        sender = absSender;
        if (args.isEmpty()) {
            askText(chatId);
        } else {
            askWhenRemind(chatId);
        }

    }

    @Override
    public void callback(AbsSender absSender, long chatId, CallbackData data) {
        sender = absSender;

        switch (data.getCallbackType()) {
            case DATE -> askDate(chatId);
            case TODAY -> {
                tempMemos.get(chatId).setDate(LocalDate.now());
                askTime(chatId);
            }
        }
    }

    private void askText(long chatId) {
        SendMessage msg = new SendMessage(String.valueOf(chatId), "What should I remind about?");
        sendMessage(sender, msg);
    }

    private void askWhenRemind(long chatId) {
        SendMessage msg = new SendMessage(String.valueOf(chatId), "When should I remind you?");
        setButtons(msg, Arrays.asList(CallbackType.TODAY, CallbackType.DATE));
        sendMessage(sender, msg);
    }

    private void askDate(long chatId) {
        SendMessage response = new SendMessage(String.valueOf(chatId), "Please, input date");
        setInline(response, "dd.MM.yy");
        sendMessage(sender, response);
    }

    private void askTime(long chatId) {
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
        menu.setOneTimeKeyboard(false);
        menu.setResizeKeyboard(true);
        menu.setSelective(true);
        menu.setKeyboard(List.of(row));
    }

    public boolean tryContinueInput(AbsSender sender, Message msg) {
        long chatId = msg.getChatId();
        boolean isInput = tempMemos.containsKey(chatId);
        if (isInput) {
            MemoInfo memo = tempMemos.get(chatId);
            handleInputToMemo(memo, msg);
        }
        return isInput;
    }

    private void handleInputToMemo(MemoInfo memo, Message msg) {
        String text = msg.getText();
        if (memo.getMessage().isEmpty()) {
            memo.setMessage(msg.getText());
            askWhenRemind(memo.getChatId());
        } else if (memo.hasDate()) {
            memo.setTime(text);
            reminder.add(tempMemos.remove(memo.getChatId()));
        } else {
            memo.setDate(text);
            askTime(memo.getChatId());
        }
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

