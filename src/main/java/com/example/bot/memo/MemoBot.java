package com.example.bot.memo;

import com.example.bot.memo.command.CallbackData;
import com.example.bot.memo.command.IBaseCommand;
import com.example.bot.memo.command.RemindCommand;
import com.example.bot.memo.command.ServiceCommand;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Optional;


public class MemoBot extends TelegramLongPollingBot {
    private static final String START_ID = "/start";
    private static final String START_DESCR = "send the welcome message and describe the bot' targets";
    private static final String START_FULLDESCR = "Welcome to the ReminderBot! It can remind you about smth by resending" +
            " your message in the specified date and time to you. To see the command list please use /help.";
    private static final String HELP_ID = "/help";
    private static final String HELP_DESCR = "shows all commands. Use /help [command] for more info";
    private static final String REMIND_ID = "/remind_me";
    private static final String REMIND_DESCR = "resends the specified message in specified date and time to you";

    private final String USERNAME;
    private final HashMap<String, IBaseCommand> commandRegister;

    public MemoBot(String token, String username) {
        super(token);

        this.USERNAME = username;
        this.commandRegister = new HashMap<>();
        this.commandRegister.put(START_ID, new ServiceCommand(START_ID, START_DESCR, START_FULLDESCR));
        this.commandRegister.put(REMIND_ID, new RemindCommand(REMIND_ID, REMIND_DESCR));
        ServiceCommand helpCommand = new ServiceCommand(HELP_ID, HELP_DESCR);
        this.commandRegister.put(HELP_ID, helpCommand);
        helpCommand.setFullDescription(describeCommands());
        System.out.println("I'm REMEMBER BOT! Nice to meet you;)");
    }

    private String describeCommands() {
        StringBuilder str = new StringBuilder();
        return commandRegister.values().stream().reduce(
                str,
                (s, cmd) -> s
                        .append(cmd.getName()).append(" ")
                        .append(cmd.getDescription()).append("\n").append("\n"),
                StringBuilder::append).toString();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            String callbackText = update.getCallbackQuery().getData();
            CallbackData data = JsonUtil.fromJson(callbackText);
            Optional<IBaseCommand> optCommand = getCommand(data.getCommandName());
            optCommand.ifPresent(iBaseCommand -> {
                long chatId = update.getCallbackQuery().getMessage().getChatId();
                iBaseCommand.callback(this, chatId, data);
            });
        } else if (update.hasMessage()) {
            Message msg = update.getMessage();
            String text = msg.getText();
            Optional<IBaseCommand> optCommand = getCommand(text);
            if (optCommand.isPresent()) {
                IBaseCommand command = optCommand.get();
                String args = getArgs(text, command);
                command.execute(this, msg.getChatId(), args);
            } else if (!tryInputMemo(msg)){
                System.out.println("Non_command msg:");
                System.out.println(msg.getText());
            }
        }
    }

    private boolean tryInputMemo(Message msg) {
        RemindCommand cmd = (RemindCommand) commandRegister.get(REMIND_ID);
        return cmd.isInputContinue(this, msg);
    }

    private Optional<IBaseCommand> getCommand(String msg) {
        IBaseCommand command = null;
        if (msg.startsWith("/")) {
            String name = msg.split(" ")[0];
            if (commandRegister.containsKey(name)) {
                command = commandRegister.get(name);
            }
        }
        return Optional.ofNullable(command);
    }

    private String getArgs(String str, IBaseCommand command) {
        return str.replace(command.getName(), "");
    }

    @Override
    public String getBotUsername() {
        return USERNAME;
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }
}
