package com.programming.techie.expensetracker;

import com.programming.techie.expensetracker.config.SwaggerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@Import(SwaggerConfiguration.class)
public class ExpenseTrackerRestApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExpenseTrackerRestApiApplication.class, args);
    }

}
