package com.example.bot.memo.command.memo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class MemoInfo {
    private static final String DATE_FORMAT = "dd.MM.yy";
    private static final String TIME_FORMAT = "HH:mm";
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(TIME_FORMAT);

    private long chatId;
    private LocalDateTime dateTime;
    private String message;

    public MemoInfo(long chatId, String message) {
        this.chatId = chatId;
        this.message = message;
    }

    public MemoInfo(long chatId, LocalDateTime dateTime, String message) {
        this(chatId, message);
        this.dateTime = dateTime;
    }

    public boolean hasDate() {
        return dateTime != null;
    }

    public long getChatId() {
        return chatId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getMessage() {
        return message;
    }

    public void setDate(LocalDate date) {
        dateTime = date.atStartOfDay();
    }

    public void setTime(LocalTime time) {
        dateTime = LocalDateTime.of(dateTime.toLocalDate(), time);
    }

    public void setDate(String dateStr) {
        LocalDate date = LocalDate.parse(dateStr, dateFormatter);
        setDate(date);
    }

    public void setTime(String timeStr) {
        LocalTime time = LocalTime.parse(timeStr, timeFormatter);
        setTime(time);
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
