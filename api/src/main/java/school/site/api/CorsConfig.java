package school.site.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("https://jazzy-pie-6d71dc.netlify.app"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "UPDATE", "PUT"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Cookie"));
        configuration.setAllowCredentials(true);
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return new CorsFilter(source);
    }
}
