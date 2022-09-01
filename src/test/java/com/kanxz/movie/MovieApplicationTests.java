package com.kanxz.movie;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kanxz.movie.generator.entity.Movie;
import com.kanxz.movie.generator.mapper.MovieMapper;
import com.kanxz.movie.generator.mapper.MovieTagMapper;
import com.kanxz.movie.generator.mapper.PermissionMapper;
import com.kanxz.movie.generator.mapper.UserMapper;
import com.kanxz.movie.service.MovieService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@SpringBootTest
class MovieApplicationTests {

    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private MovieMapper movieMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MovieTagMapper movieTagMapper;
    @Autowired
    private MovieService movieService;


    @Test
    public void testGetRecommendMovieList() {
        movieService.getRecommendMovieList(7L, 1);
    }


    @Test
    public void testGetMovieList() {
        Page<Movie> moviePage = new Page<>(1, 18);
        IPage<Movie> movieList = movieMapper.getMovieList(moviePage, "英语", "/*");
        List<Movie> movies = movieList.getRecords();
        for (Movie movie : movies) {
            System.out.println(movie.getName());
        }
    }

    @Test
    void testPasswordEncoder() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode("123456");
        System.out.println(encode);
    }

    @Test
    void testSelectPermsByUserId() {
        List<String> perms = permissionMapper.selectPermsByUserId(1L);
        for (String perm : perms) {
            System.out.println(perm);
        }
    }

}
