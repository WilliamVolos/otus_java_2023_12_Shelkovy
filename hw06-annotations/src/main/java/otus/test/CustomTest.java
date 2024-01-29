package otus.test;

import otus.annotations.After;
import otus.annotations.Before;
import otus.annotations.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CustomTest {
    private String valueTest;
    private int intTest;
    private final String FIRST_RESULT = "String Before first test!";

    public CustomTest(){
    }

    @Test
    void firstStringTest(){
        valueTest = valueTest + "first test!";
        assertThat(valueTest).isEqualTo(FIRST_RESULT);
    }

    @Test
    void secondStringTest(){
        valueTest = valueTest + "second test!";
        assertThat(valueTest).isEqualTo(FIRST_RESULT);
    }

    @Test
    void intTest(){
        assertThat(intTest).isEqualTo(10);
    }

    @Before
    void methodBefore1(){
        valueTest = "String Before ";
    }

    @Before
    void methodBefore2(){
        intTest += 10;
    }

    @After
    void methodAfter(){
        intTest = 25;
    }
}
