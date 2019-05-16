package com.epam.spring;

import com.epam.spring.view.ConsoleView;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("context.xml");

        ctx.getBean(ConsoleView.class).run();
    }
}
