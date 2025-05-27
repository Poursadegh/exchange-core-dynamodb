package exchange.core2.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ExchangeCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExchangeCoreApplication.class, args);
    }
} 