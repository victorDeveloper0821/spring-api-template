package idv.victor.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 瀏覽器同源政策
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		// TODO Auto-generated method stub
        registry.addMapping("/**").
        allowedHeaders("*").
        allowedMethods("*").
        maxAge(1800).
        allowedOrigins("*");
	}

}
