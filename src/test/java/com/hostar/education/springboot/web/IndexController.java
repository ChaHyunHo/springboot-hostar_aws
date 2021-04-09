package com.hostar.education.springboot.web;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IndexController {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void main_page_test() throws Exception {

        // when
        String body = this.restTemplate.getForObject("/", String.class);

        Assertions.assertThat(body).contains("스프링부트로 시작하는 웹 서비스 Ver.2");
    }
}
