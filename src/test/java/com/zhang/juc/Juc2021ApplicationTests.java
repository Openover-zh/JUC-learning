package com.zhang.juc;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

//@SpringBootTest
class Juc2021ApplicationTests
{

    @Test
    void contextLoads()
    {
        ArrayList<User> objects = new ArrayList<>();
        objects.add(new User("zhang","11"));
        objects.add(new User("li","14"));

        User user = objects.stream().filter(res -> res.getAge().equals("11")).findFirst().get();
        user.setAge("22");

        for (User object : objects) {
            System.out.println(object);
        }

    }

}
