package com.omnet.cnt;

import com.omnet.cnt.Model.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableCaching
@EnableAspectJAutoProxy
@EnableScheduling
 @EnableConfigurationProperties(AppProperties.class) 

public class OmnetJspApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(OmnetJspApplication.class, args);
	}
}
