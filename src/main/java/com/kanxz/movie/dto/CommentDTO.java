package com.kanxz.movie.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
public class CommentDTO {

    private Long id;
    private String username;
    private String movieName;
    private String comments;
    private LocalDateTime createTime;
}
