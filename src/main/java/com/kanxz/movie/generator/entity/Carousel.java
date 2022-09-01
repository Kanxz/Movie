package com.kanxz.movie.generator.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author kanxz
 * @since 2022-05-04
 */
@Getter
@Setter
@TableName("carousel")
public class Carousel {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 图片URL地址
     */
    @TableField("pic_url")
    private String picUrl;

    /**
     * 豆瓣ID
     */
    @TableField("douban_id")
    private String doubanId;

    public Carousel() {
    }

    public Carousel(Long id, String picUrl, String doubanId) {
        this.id = id;
        this.picUrl = picUrl;
        this.doubanId = doubanId;
    }
}
