package com.riskmonster.development.service.impl;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
public class LocalBeanUtil implements ApplicationContextAware {
	
	private static ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// TODO Auto-generated method stub
		context = applicationContext;
	}
	
	public static <T> T getBeanOrThrow(Class<T> beanClass) {
		return context.getBean(beanClass);
	}

}
