package com.kanxz.movie.spider;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kanxz.movie.generator.entity.Movie;
import com.kanxz.movie.generator.entity.MovieDetail;
import com.kanxz.movie.generator.entity.MovieTag;
import com.kanxz.movie.generator.entity.Tag;
import com.kanxz.movie.generator.mapper.MovieDetailMapper;
import com.kanxz.movie.generator.mapper.MovieMapper;
import com.kanxz.movie.generator.mapper.MovieTagMapper;
import com.kanxz.movie.generator.mapper.TagMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Component
@Slf4j
public class MovieProcessor implements PageProcessor {

    @Autowired
    private MovieMapper movieMapper;
    @Autowired
    private MovieDetailMapper movieDetailMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private MovieTagMapper movieTagMapper;

    @Override
    public void process(Page page) {
        //上下文 可以传递一些数据
        Map<String, Object> extras = page.getRequest().getExtras();
        //根据类型分别解析——列表页和详情页
        String level = extras.get("level").toString();
        if (level.equals("list")) {
            parseList(page);
        } else if (level.equals("detail")) {
            parseDetail(page);
        }

    }

    /**
     * 解析详情页
     */
    private void parseDetail(Page page) {
        //豆瓣ID
        String doubanID = page.getUrl().regex("\\d+").get();
        Html html = page.getHtml();
        //电影名
        String movieName = html.$("#content > h1 > span:first-child").xpath("///allText()").get();
        //电影导演
        String director = html.$("div#info > span:first-child a").xpath("///allText()").get();
        //获取语言
        String str = html.xpath("//*[@id=\"info\"]/text()").get();
        //使用正则获取语言
        List<String> lang_list = ReUtil.getAllGroups(Pattern.compile("\\S*语"), str);
        //拼接
        StringBuilder language_temp = new StringBuilder(lang_list.get(0));
        for (int i = 1; i < lang_list.size(); i++) {
            language_temp.append("/").append(lang_list.get(i));
        }
        String language = language_temp.toString();
        //获取上映日期
//        String releaseTime = html.$("span[property=\"v:initialReleaseDate\"]")
//                .xpath("///allText()").get().substring(0, 10);
        String releaseTime = html.$("span[property=\"v:initialReleaseDate\"]")
                .xpath("///allText()").regex("\\d{4}-?\\d{0,2}-?\\d{0,2}").get();
        //获取电影时长
        String len = html.$("span[property=\"v:runtime\"]")
                .xpath("///allText()").regex("\\d*").get();
        int movieLength = 0;
        if (!StrUtil.isEmpty(len)) {
            movieLength = Integer.parseInt(len);
        }
        // 获取电影简介
        String intro = html.$("#link-report").xpath("///allText()").get();

        MovieDetail movieDetail = new MovieDetail();
        movieDetail.setDoubanId(doubanID);
        movieDetail.setName(movieName);
        movieDetail.setDirector(director);
        movieDetail.setLanguage(language);
        movieDetail.setIntro(intro);

        if (releaseTime.length() == 10) {
            movieDetail.setReleaseTime(LocalDate.parse(releaseTime, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        } else if (releaseTime.length() == 7) {
            movieDetail.setReleaseTime(LocalDate.parse(releaseTime, DateTimeFormatter.ofPattern("yyyy-MM")));
        } else if (releaseTime.length() == 4) {
            movieDetail.setReleaseTime(LocalDate.parse(releaseTime, DateTimeFormatter.ofPattern("yyyy")));

        }
        if (movieLength != 0) {
            movieDetail.setLength(movieLength);
        }
        try {
            movieDetailMapper.insert(movieDetail);
        } catch (Exception e) {
            log.info("数据已存在");
        }

        //标签
        List<String> tags = html.$("span[property=\"v:genre\"]").xpath("///allText()").all();
        for (String tag : tags) {
            Tag tmp = new Tag();
            tmp.setTagName(tag);
            try {
                tagMapper.insert(tmp);
            } catch (Exception e) {
                log.info("数据已存在");
            }

            //获取tag
            QueryWrapper<Tag> wrapper = new QueryWrapper<>();
            wrapper.eq("tag_name", tag);
            Tag selectOne = tagMapper.selectOne(wrapper);

            //获取movie
            QueryWrapper<Movie> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("douban_id", doubanID);
            Movie movie = movieMapper.selectOne(wrapper1);

            //电影标签关系
            MovieTag movieTag = new MovieTag();
            movieTag.setMovieId(movie.getId());
            movieTag.setTagId(selectOne.getId());
            try {
                movieTagMapper.insert(movieTag);
            } catch (Exception e) {
                log.info("数据已存在");
            }

        }

    }

    /**
     * 解析列表页
     */
    private void parseList(Page page) {
        Json json = page.getJson();

        //获取电影信息
        List<String> ids = json.jsonPath("subjects[*].id").all();
        List<String> names = json.jsonPath("subjects[*].title").all();
        List<String> scores = json.jsonPath("subjects[*].rate").all();
        List<String> picUrls = json.jsonPath("subjects[*].cover").all();

        for (int i = 0; i < names.size(); i++) {
            Movie movie = new Movie();
            movie.setDoubanId(ids.get(i));
            movie.setName(names.get(i));
            movie.setPicUrl(picUrls.get(i));
            movie.setScore(Float.valueOf(scores.get(i)));
            try {
                movieMapper.insert(movie);
            } catch (Exception e) {
                log.info("数据已存在");
            }
        }

        //获取详情页URL
        List<String> detail_urls = json.jsonPath("subjects[*].url").all();
        for (String detail_url : detail_urls) {
            Request request = new Request(detail_url);
            HashMap<String, Object> map = new HashMap<>();
            map.put("level", "detail");
            request.setExtras(map);
            page.addTargetRequest(request);
        }

    }

    @Override
    public Site getSite() {
        return Site.me().setSleepTime(10000);
    }

}
