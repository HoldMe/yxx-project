package com.yxx.framework.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * desc
 * </p>
 *
 * @author wangpan
 * @date 2020/9/28
 */
@RestController
@RequestMapping("/stat")
public class StatisticApi {

    @GetMapping("/test/{context}")
    public String printSend(@PathVariable("context") String context) {
        return context;
    }
}
