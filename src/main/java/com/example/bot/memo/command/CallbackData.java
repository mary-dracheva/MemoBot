package com.example.bot.memo.command;

import com.example.bot.memo.command.memo.CallbackType;

public class CallbackData {
    private String commandName;
    private CallbackType callbackType;
    private String text;

    public CallbackData() {
    }

    public CallbackData(String commandName, CallbackType callbackType, String text) {
        this.commandName = commandName;
        this.callbackType = callbackType;
        this.text = text;
    }

    public CallbackData(String commandName, CallbackType callbackType) {
        this(commandName, callbackType, "");
    }

    public String getCommandName() {
        return commandName;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    public void setCallbackType(CallbackType callbackType) {
        this.callbackType = callbackType;
    }

    public CallbackType getCallbackType() {
        return callbackType;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
