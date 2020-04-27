package com.kk.rediscluster.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zhouzijia
 * @version 1.0
 * @description
 * @date 2020/4/27 11:03
 */
@ApiModel("缓存请求类")
@Data
public class CacheBody {

    private String key;

    private String vlaue;

    private int expire;

}
