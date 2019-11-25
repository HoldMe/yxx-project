package com.yxx.framework.api;

import com.yxx.framework.dto.ChatCustomer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * desc
 * </p>
 *
 * @author wangpan
 * @date 2019/11/12
 */
@RestController
@RequestMapping("/tourist")
public class RegisteredApi {

    @PostMapping("/welcome")
    public void welcome(@RequestBody ChatCustomer customer){
        System.out.println("====welcome===");
    }
}
