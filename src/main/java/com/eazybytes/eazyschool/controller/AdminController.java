package com.eazybytes.eazyschool.controller;


import com.eazybytes.eazyschool.model.EazyClass;
import lombok.extern.slf4j.Slf4j;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("admin")
public class AdminController {


    @RequestMapping("/displayClasses")
    public ModelAndView displayClasses(Model model) {

        ModelAndView modelAndView = new ModelAndView("classes.html");

        modelAndView.addObject("eazyClass", new EazyClass());
        return modelAndView;
    }
}