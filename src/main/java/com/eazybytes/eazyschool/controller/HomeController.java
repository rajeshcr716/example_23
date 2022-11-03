package com.eazybytes.eazyschool.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.annotation.RequestScope;


@Controller
public class HomeController {

    /*
    Below line means  after "localhost:8080"
    we can add "/","/home" and same as link also it went to home page..
    {"", "/", "home"}
    */
    @RequestMapping(value={"", "/", "home"})
   // @RequestMapping(value={"home"}) // it is used for hal path. now field add application.propertes so not required.
    public String displayHomePage() {
        return "home.html";
    }

}
