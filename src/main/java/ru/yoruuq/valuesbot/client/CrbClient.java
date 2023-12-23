package ru.yoruuq.valuesbot.client;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yoruuq.valuesbot.exception.ServiceException;

import java.io.IOException;

@Component
public class CrbClient {
    @Autowired
    private OkHttpClient client;

    @Value("${crb.current.values.rate}")
    private String url;


    public String getRatesXML() throws ServiceException{
        Request request = new Request.Builder()
                .url(url)
                .build();


        try (Response response = client.newCall(request).execute()){
            ResponseBody body = response.body();
            return body == null ? null : body.string();
        } catch (IOException e) {
            throw new ServiceException("Error with getting values rate", e);
        }
    }
}
