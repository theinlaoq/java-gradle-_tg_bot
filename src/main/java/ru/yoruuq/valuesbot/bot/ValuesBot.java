package ru.yoruuq.valuesbot.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.yoruuq.valuesbot.exception.ServiceException;
import ru.yoruuq.valuesbot.service.ValuesRateService;

import java.time.LocalDate;


@Component
public class ValuesBot extends TelegramLongPollingBot {

    private static final Logger LOG = LoggerFactory.getLogger(ValuesBot.class);

    private static final String START = "/start";
    private static final String USD = "/usd";
    private static final String EUR = "/eur";
    private static final String HELP = "/help";


    @Autowired
    private ValuesRateService valuesRateService;

    public ValuesBot(@Value("${bot.token}") String botToken){
        super(botToken);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }

        String message = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();
        switch (message){
            case START -> {
                String userName = update.getMessage().getChat().getUserName();
                startCommand(chatId, userName);
            }
            case USD -> usdCommand(chatId);
            case EUR -> eurCommand(chatId);
            case HELP -> helpCommand(chatId);
            default -> unknownCommand(chatId);
        }
    }

    @Override
    public String getBotUsername() {
        return "{your bot name}";
    }


    private void startCommand(Long chatId, String userName){
        String text = """
                Welcome %s!
                
                Here you can know official value rates today by CBRF
                
                Main commands:
                /usd - usd rate
                /eur - eur rate
                
                Additional commands:
                /help - get info
                """;
        String formattedText = String.format(text, userName);
        sendMessage(chatId, formattedText);
    }

    private void usdCommand(Long chatId){
        String formattedText;
        try{
            String usd = valuesRateService.getUSDRate();
            String text = "usd exchange rate on %s is %s rub";
            formattedText = String.format(text, LocalDate.now(), usd);
        }catch (ServiceException e){
            LOG.error("error with getting usd rate", e);
            formattedText = "can not get usd rate, try later...";
        }
        sendMessage(chatId, formattedText);
    }

    private void eurCommand(Long chatId){
        String formattedText;
        try{
            String eur = valuesRateService.getEURORate();
            String text = "eur exchange rate on %s is %s rub";
            formattedText = String.format(text, LocalDate.now(), eur);
        }catch (ServiceException e){
            LOG.error("error with getting eur rate", e);
            formattedText = "can not get eur rate, try later...";
        }
        sendMessage(chatId, formattedText);
    }
    private void helpCommand(Long chatId){
        String text = """
                bot info
                /usd - usd exchange rate
                /eur - eur exchange rate
                """;
        sendMessage(chatId, text);
    }

    private void unknownCommand(Long chatId){
        String text = "unknown command, try use /help to get more info";
        sendMessage(chatId, text);
    }

    private void sendMessage(Long chatId, String text){
        String chatIdStr = String.valueOf(chatId);
        SendMessage sendMessage = new SendMessage(chatIdStr, text);
        try {
            execute(sendMessage);
        }catch (TelegramApiException e){
            LOG.error("Error with sending message", e);
        }
    }
}
