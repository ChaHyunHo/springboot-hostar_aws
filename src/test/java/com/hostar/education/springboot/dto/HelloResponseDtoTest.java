package com.hostar.education.springboot.dto;

import com.hostar.education.springboot.web.dto.HelloResponseDto;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class HelloResponseDtoTest {

    @Test
    public void lombok_test() {
        String name = "test";
        int amount = 1000;

        HelloResponseDto dto = new HelloResponseDto ("test", 1000);

        Assertions.assertThat (dto.getName ()).isEqualTo (name);
        Assertions.assertThat (dto.getAmount ()).isEqualTo (amount);
    }

}
