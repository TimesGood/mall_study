package com.example.mall_study;



import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class MallStudyApplicationTests {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Test
    void contextLoads() {
        String encode = passwordEncoder.encode("root");
        System.out.println(encode);
    }

}
