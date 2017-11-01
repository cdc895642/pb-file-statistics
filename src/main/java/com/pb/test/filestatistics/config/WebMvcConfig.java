package com.pb.test.filestatistics.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Use for define messages source
 */
@Configuration
@PropertySource(value = "classpath:messages.properties", encoding = "UTF-8")
public class WebMvcConfig extends WebMvcConfigurerAdapter{

}
