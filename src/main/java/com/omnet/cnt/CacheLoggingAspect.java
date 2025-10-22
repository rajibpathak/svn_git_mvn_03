	/**
	 * Document: CacheLoggingAspect.java
	 * Author: Jamal Abraar
	 * Date Created: 30-Aug-2024
	 * Last Updated: 
	 */
package com.omnet.cnt;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CacheLoggingAspect {
	@Around("@annotation(cacheable)")
    public Object logCacheAccess(ProceedingJoinPoint joinPoint, Cacheable cacheable) throws Throwable {
        String cacheName = cacheable.value()[0];  // Get the cache name
        String key = joinPoint.getArgs()[0].toString();  // Get the cache key (status)
        
        System.out.println("Checking cache for key: " + key + " in cache: " + cacheName);

        Object result = joinPoint.proceed();

        System.out.println("Data fetched from the database for key: " + key);
        
        return result;
    }
}
