package com.tdpro.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author zwx
 * @date 2018-06-14
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {
    private static Logger logger = LoggerFactory.getLogger(SpringContextUtil.class);
    private static ApplicationContext APPLICATION_CONTEXT;
    /**
     * 设置spring上下文
     * @param applicationContext spring上下文
     * @throws BeansException
     * */
    @Override  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        logger.debug("ApplicationContext registed-->{}", applicationContext);
        APPLICATION_CONTEXT = applicationContext;
    }

    /**
     * 获取容器
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return APPLICATION_CONTEXT;
    }

    /**
     * 获取容器对象
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> type) {
        return APPLICATION_CONTEXT.getBean(type);
    }
}