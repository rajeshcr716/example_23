package com.eazybytes.eazyschool.controller;


import com.eazybytes.eazyschool.model.Address;
import com.eazybytes.eazyschool.model.Person;
import com.eazybytes.eazyschool.model.Profile;
import com.eazybytes.eazyschool.repository.PersonRepositary;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.dialect.lock.PessimisticEntityLockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@Controller("profileControllerBean")
public class ProfileController {

    @Autowired
    PersonRepositary personRepositary;

    @RequestMapping("/displayProfile")
    public ModelAndView displayMessages(Model model, HttpSession session)
    {
        Person person = (Person) session.getAttribute("loggedInPerson");
        Profile profile = new Profile();
    //Using person object we need to populate value inside Profile class.

        profile.setName(person.getName());
        profile.setName(person.getName());
        profile.setMobileNumber(person.getMobileNumber());
        profile.setEmail(person.getEmail());
        if(person.getAddress() !=null && person.getAddress().getAddressId()>0){
            profile.setAddress1(person.getAddress().getAddress1());
            profile.setAddress2(person.getAddress().getAddress2());
            profile.setCity(person.getAddress().getCity());
            profile.setState(person.getAddress().getState());
            profile.setZipCode(person.getAddress().getZipCode());
        }
    //below section we pass the existing data into the web page ..using modelandview .
        ModelAndView modelAndView = new ModelAndView("profile.html");
        modelAndView.addObject("profile",profile);
        return modelAndView;
    }
    @PostMapping(value = "/updateProfile")
    //@Valid means validation will happen in the profile model.
    //error will used inform error parameter
    //3rd one seeson . take the data from the session store it into session.
    public String updateProfile(@Valid @ModelAttribute("profile") Profile profile, Errors errors,
                                HttpSession session)
    {
        if(errors.hasErrors()){
            return "profile.html";
        }
       //person object we set in the Dashboard controller
        Person person = (Person) session.getAttribute("loggedInPerson");
       //again we fetch detail from profile page because if in case user updated the exsisting detail.
        person.setName(profile.getName());
        person.setEmail(profile.getEmail());
        person.setMobileNumber(profile.getMobileNumber());
        if(person.getAddress() ==null || !(person.getAddress().getAddressId()>0)){
            person.setAddress(new Address()); // first we set empty address object.
        }
        //below we fetch the detail from profile to person.
        person.getAddress().setAddress1(profile.getAddress1());
        person.getAddress().setAddress2(profile.getAddress2());
        person.getAddress().setCity(profile.getCity());
        person.getAddress().setState(profile.getState());
        person.getAddress().setZipCode(profile.getZipCode());
      //finally we save the data into the person Repositary.
        // in that we updating not inserting
        Person savedPerson = personRepositary.save(person);
        session.setAttribute("loggedInPerson", savedPerson);
        return "redirect:/displayProfile";
    }

}
