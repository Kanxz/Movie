package com.kanxz.movie.controller;


import com.kanxz.movie.common.api.CommonResult;
import com.kanxz.movie.dto.CarouselDTO;
import com.kanxz.movie.generator.entity.Carousel;
import com.kanxz.movie.service.CarouselService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author kanxz
 * @since 2022-05-04
 */
@RestController
@RequestMapping("/carousel")
@Slf4j
public class CarouselController {

    @Autowired
    private CarouselService carouselService;

    @GetMapping("/list")
    public CommonResult getCarouselList() {
        List<Carousel> carouselList = carouselService.list();
        return CommonResult.success(carouselList);
    }

    @PostMapping("/admin/updateCarousel")
    @PreAuthorize("hasAuthority('system')")
    public CommonResult updateCarousel(@RequestBody CarouselDTO carouselDTO) {
        log.debug(carouselDTO.toString());
        Carousel carousel1 = new Carousel(0L, carouselDTO.getPic1(), carouselDTO.getDoubanId1());
        Carousel carousel2 = new Carousel(1L, carouselDTO.getPic2(), carouselDTO.getDoubanId2());
        Carousel carousel3 = new Carousel(2L, carouselDTO.getPic3(), carouselDTO.getDoubanId3());
        Carousel carousel4 = new Carousel(3L, carouselDTO.getPic4(), carouselDTO.getDoubanId4());
        List<Carousel> carouselList = new ArrayList<>();
        carouselList.add(carousel1);
        carouselList.add(carousel2);
        carouselList.add(carousel3);
        carouselList.add(carousel4);
        carouselService.updateBatchById(carouselList);
        return CommonResult.success(null);
    }

}
