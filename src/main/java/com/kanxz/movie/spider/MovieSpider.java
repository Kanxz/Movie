package com.kanxz.movie.spider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;

import java.util.HashMap;

@Component
public class MovieSpider {

    private static final String url = "https://movie.douban.com/j/search_subjects?type=movie&tag=%E6%9C%80%E6%96%B0&page_limit=100&page_start=1";

    @Autowired
    private MovieProcessor movieProcessor;

    public void doSpider() {

        Request request = new Request(url);
        HashMap<String, Object> map = new HashMap<>();
        map.put("level", "list");
        request.setExtras(map);

        Spider.create(movieProcessor)
                .addRequest(request)
                .thread(3)
                .start();
    }

}
