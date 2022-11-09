package com.eazybytes.eazyschool.controller;

import com.eazybytes.eazyschool.model.Person;
import com.eazybytes.eazyschool.repository.PersonRepositary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
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

   /* Whenver we using @Value need to mention property value. In that "$" symbol is required.
   because to access a specific variable.
   */
    @Value("${eazyschool.pageSize}")
    private int defaultPageSize;

    @Value("${eazyschool.contact.successMsg}")
    private String message;

    @Autowired
    Environment environment;


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

       logMessages();

         return "dashboard.html";
    }

    private void logMessages() {
        log.error("Error message from the Dashboard page");
        log.warn("Warning message from the Dashboard page");
        log.info("Info message from the Dashboard page");
        log.debug("Debug message from the Dashboard page");
        log.trace("Trace message from the Dashboard page");



        log.error("defaultPageSize value with @Value annotation is : "+defaultPageSize);
        log.error("successMsg value with @Value annotation is : "+message);

        log.error("defaultPageSize value with Environment is : "+environment.getProperty("eazyschool.pageSize"));
        log.error("successMsg value with Environment is : "+environment.getProperty("eazyschool.contact.successMsg"));
        log.error("Java Home environment variable using Environment is : "+environment.getProperty("JAVA_HOME"));





    }



}