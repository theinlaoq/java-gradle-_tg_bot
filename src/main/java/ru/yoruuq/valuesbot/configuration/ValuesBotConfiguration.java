package ru.yoruuq.valuesbot.configuration;


import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.yoruuq.valuesbot.bot.ValuesBot;

@Configuration
public class ValuesBotConfiguration {

    @Bean
    public TelegramBotsApi telegramBotsApi(ValuesBot valuesBot) throws TelegramApiException {
        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(valuesBot);
        return api;
    }

    @Bean
    public OkHttpClient okHttpClient(){
        return new OkHttpClient();
    }
}
