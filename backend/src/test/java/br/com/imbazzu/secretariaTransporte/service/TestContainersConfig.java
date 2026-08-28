package br.com.imbazzu.secretariaTransporte.service;


import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.mysql.MySQLContainer;

@TestConfiguration(proxyBeanMethods = false)
public class TestContainersConfig {

    @Bean
    @ServiceConnection
    MySQLContainer mysql(){
        return new MySQLContainer("mysql:8.0")
                .withReuse(true);
    }

}
