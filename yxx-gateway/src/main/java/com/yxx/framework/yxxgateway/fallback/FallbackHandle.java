package com.yxx.framework.yxxgateway.fallback;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * desc
 * </p>
 *
 * @author wangpan
 * @date 2019/11/21
 */
@RestController
public class FallbackHandle {

    @RequestMapping("/defaultfallback")
    public String defaultfallback(){
        return "is down!!!";
    }
}
