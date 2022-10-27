package com.eazybytes.eazyschool.controller;

import com.eazybytes.eazyschool.model.Person;
import com.eazybytes.eazyschool.repository.PersonRepositary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
public class DashboardController {


    // fetching purpose wiring with person repositary
    @Autowired
    PersonRepositary personRepositary;

    @RequestMapping("/dashboard")
    public String displayDashboard(Model model,Authentication authentication, HttpSession session){


        Person person = personRepositary.readByEmail(authentication.getName());
        // get the logged in/authenticated person name

        model.addAttribute("username", person.getName());
        // set user name as logged in user name

        model.addAttribute("roles", authentication.getAuthorities().toString());
        // get the roles by ".getAuthorities()"

       /* Below section is used when student login it show which class is assigned / enrolled.
       model.addAttribute("enrolledClass", person.getEazyClass().getName());
       using we pass the data to front end .. so that in dashboard.html line no: 46 we defined "enrolledClass" object using thymleaf.
       <p class="" th:if="${!#strings.isEmpty(enrolledClass)}" th:text="${'Your assigned class is - [' + enrolledClass+']'}"></p>
        */

        if(null != person.getEazyClass() && null != person.getEazyClass().getName()){
            model.addAttribute("enrolledClass", person.getEazyClass().getName());
        }


        session.setAttribute("loggedInPerson", person);



         return "dashboard.html";
    }

}