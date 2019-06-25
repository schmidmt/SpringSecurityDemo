package com.schmidmt.demo.config;

import com.schmidmt.demo.models.MyGrantAuthority;
import com.schmidmt.demo.models.User;
import com.schmidmt.demo.models.dao.MyGrantAuthorityRepository;
import com.schmidmt.demo.models.dao.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.HashSet;


@SpringBootApplication
@ComponentScan("com.schmidmt.demo.models")
@ComponentScan("com.schmidmt.demo.models.dao")
public class SetupAdmin
        implements CommandLineRunner {

    private static Logger LOG = LoggerFactory
            .getLogger(SetupAdmin.class);

    public static void main(String[] args) {
        LOG.info("STARTING THE APPLICATION");
        SpringApplication.run(SetupAdmin.class, args);
        LOG.info("APPLICATION FINISHED");
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MyGrantAuthorityRepository myGrantAuthorityRepository;

    @Override
    public void run(String... args) {
        LOG.info("EXECUTING : Setting up initial admin user");

        MyGrantAuthority userRole = myGrantAuthorityRepository.getByRole("USER");
        if (userRole != null) {
            LOG.info("User role exists!");
        } else {
            userRole = new MyGrantAuthority("USER");
            myGrantAuthorityRepository.save(userRole);
        }


        MyGrantAuthority adminRole = myGrantAuthorityRepository.getByRole("ADMIN");
        if (adminRole != null) {
            LOG.info("Admin role exists!");
        } else {
            adminRole = new MyGrantAuthority("ADMIN");
            myGrantAuthorityRepository.save(adminRole);
            adminRole = myGrantAuthorityRepository.getByRole("ADMIN");
        }


        User admin = userRepository.findByUsername("admin@admin");

        if (admin != null) {
            LOG.info("Admin user exists, doing nothing...");
        } else {
            LOG.info("Creating new admin user");
            HashSet<MyGrantAuthority> grants = new HashSet<>();
            grants.add(adminRole);
            admin = new User("admin@admin", grants, "abc123");
            userRepository.save(admin);
        }
    }
}