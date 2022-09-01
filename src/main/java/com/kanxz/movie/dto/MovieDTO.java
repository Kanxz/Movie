package com.kanxz.movie.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class MovieDTO {

    private Long id;
    private String name;
    private String score;
    private String doubanId;
    private String director;
    private String language;
    private String releaseTime;
    private String length;
    private String picUrl;
    private String tags;

}
