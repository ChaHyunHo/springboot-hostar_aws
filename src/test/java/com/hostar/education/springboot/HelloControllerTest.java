package com.hostar.education.springboot;

import com.hostar.education.springboot.web.HelloController;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith (SpringRunner.class)
@WebMvcTest(controllers = HelloController.class)
public class HelloControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void hello() throws Exception {
        String hello = "hello";
        mvc.perform (get ("/hello"))
                    .andExpect(MockMvcResultMatchers.status ().isOk ())
                    .andExpect(MockMvcResultMatchers.content().string(hello));
    }

    @Test
    public void helloDto_test() throws Exception {
        String name = "cha";
        int amount = 1000;

        mvc.perform (get ("/hello/dto")
                .param ("name", name)
                .param ("amount",  String.valueOf (amount)))
                    .andExpect (MockMvcResultMatchers.status().isOk()) // auto import setting 알아볼것 ...
                    .andExpect (MockMvcResultMatchers.jsonPath ("$.name", Matchers.is (name)))
                    .andExpect (MockMvcResultMatchers.jsonPath ("$.amount", Matchers.is (amount)))
                ;
    }
}
