package pl.com.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import pl.com.app.configuration.ConverterConfig;


@SpringBootApplication
@EnableJpaRepositories("pl.com.app")
@EntityScan("pl.com.app")
@ComponentScan("pl.com.app")
@Import({ConverterConfig.class})
@EnableScheduling
public class WebApp {
    public static void main(String[] args) {
        SpringApplication.run(WebApp.class,args);
    }

//    @Bean
//    public RestTemplate restTemplate() {
//        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
//        requestFactory.setReadTimeout(600000);
//        requestFactory.setConnectTimeout(600000);
//        return new RestTemplate(requestFactory);
//    }
}
