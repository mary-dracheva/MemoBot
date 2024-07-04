package com.example.bot.memo.command.memo;

public enum CallbackType {
    //TEXT("Text"),
    TODAY("Today"),
    DATE("Input date");
    //TIME("Input time");

    private final String title;

    CallbackType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
