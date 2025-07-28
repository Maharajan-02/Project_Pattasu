package com.pattasu.config;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.github.benmanes.caffeine.cache.Caffeine;

@Configuration
@EnableCaching
public class AppConfig {
	
	@Value("${cloudVar}")
	private String cloudVar;
    
	@Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public CacheManager cacheManager() {
    	CaffeineCacheManager cacheManager = new CaffeineCacheManager();
    	cacheManager.setCaffeine(Caffeine.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES).maximumSize(1000));
    	return cacheManager;
    }
    
    @Bean
    public Cloudinary cloudinary() {
    	String[] cloudVars = cloudVar.split("\\.");

        if (cloudVars.length < 3) {
            throw new IllegalArgumentException("cloudVar must contain cloud_name.api_key.api_secret");
        }
        return new Cloudinary(ObjectUtils.asMap(
	    		"cloud_name", cloudVars[0],
	            "api_key", cloudVars[1],
	            "api_secret", cloudVars[2],
                "secure", true
        ));
    }
}
