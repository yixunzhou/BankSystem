package org.is.bupt.banksystemmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BankSystemManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankSystemManagementApplication.class, args);
        System.out.println("\033[1;32m"+"String Boot Application has started successfully!!!");
    }

}
