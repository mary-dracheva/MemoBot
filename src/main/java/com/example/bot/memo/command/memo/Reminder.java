package com.example.bot.memo.command.memo;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Reminder {
    private LinkedList<MemoInfo> list;

    public Reminder() {
        this.list = new LinkedList<>();
    }

    public void add(MemoInfo memo) {
        int idx = (int) list.parallelStream().takeWhile(r ->
                r.getDateTime().isBefore(memo.getDateTime())).count();
        list.add(idx, memo);
        System.out.println(">>: " + list.size());
        System.out.println(">>Memo to remind:\n" + memo.getMessage() + "\n" + memo.getDateTime());
    }

    public List<MemoInfo> readyToSend() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime checkTime = now.minusNanos(now.getNano()).minusSeconds(now.getSecond());
        List<MemoInfo> readed =  list.parallelStream().takeWhile(r -> {
            LocalDateTime time = r.getDateTime();
            return time.isBefore(checkTime) || time.isEqual(checkTime);
        }).collect(Collectors.toList());

        if (!readed.isEmpty()) {
            list.removeAll(readed);
            System.out.println("Check time: " + checkTime);
            System.out.println("<<: " + list.size());
        }
        return readed;
    }
}
