package com.example.bot.memo.command;

public class CallbackData {
    private String commandName;
    private CallbackType callbackType;

    public CallbackData() {
    }

    public CallbackData(String commandName, CallbackType callbackType) {
        this.commandName = commandName;
        this.callbackType = callbackType;
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


}
