package de.unijena.cheminf.myfirstspringmvc;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import de.unijena.cheminf.myfirstspringmvc.storage.StorageProperties;
import de.unijena.cheminf.myfirstspringmvc.storage.StorageService;

import static org.springframework.boot.SpringApplication.*;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class MyFirstSpringMvcApplication {

    public static void main(String[] args) {
        run(MyFirstSpringMvcApplication.class, args);
    }

    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
            storageService.deleteAll();
            storageService.init();
        };
    }
}
