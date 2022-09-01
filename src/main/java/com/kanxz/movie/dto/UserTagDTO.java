package com.kanxz.movie.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class UserTagDTO {

    private Long userId;
    private Long movieId;
    private List<String> tags;
    private int score;
}
