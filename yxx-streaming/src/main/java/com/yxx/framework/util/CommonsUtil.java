package com.yxx.framework.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;

/**
 * <p>
 * desc
 * </p>
 *
 * @author wangpan
 * @date 2019/11/4
 */
public class CommonsUtil {

    private static Boolean stopTag;
    @Autowired
    private ApplicationContext sc;

    /**
     * 启动任务
     * @param taskName
     */
    public void startTask(Class taskName) throws Exception{
        stopTag = true;
        Object bean = sc.getBean(taskName);
        Method method = taskName.getMethod("startTask");
        method.invoke(null);
    }

    /**
     * 停止任务
     */
    public  void stopTask(Class taskName) throws Exception{
        Object bean = sc.getBean(taskName);
        Method method = taskName.getMethod("stopTask");
        method.invoke(null);
    }
}
