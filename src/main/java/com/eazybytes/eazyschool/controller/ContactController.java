package com.eazybytes.eazyschool.controller;

import com.eazybytes.eazyschool.model.Contact;
import com.eazybytes.eazyschool.service.ContactService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
@Slf4j
@Controller
public class ContactController {

   // this log line not needed.
    /* private static Logger log = LoggerFactory.getLogger(ContactController.class);*/


    private final ContactService contactService;

    @Autowired
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }


//--STEP 3
// this is commented because no validation
    /*public String displayContactPage(){
        return "contact.html";
    }
*/
    // In below code we need to validate contact information given from the front end
    //first we need create model abject inside method ,after will call the contact object and make validation
  //--STEP 4
    @RequestMapping("/contact")
    public String displayContactPage(Model model){
        model.addAttribute("contact",new Contact());

        return "contact.html";
    }


    //Request mapping in genric in nature and it is our responsibility to convey what method should accept whether it post (or)get.
    //if we not mention the method support both type of them.


///--STEP 1
    //@PostMapping(value = "/saveMsg")
   /* @RequestMapping(value = "/saveMsg",method = POST)
    public ModelAndView saveMessage(@RequestParam String name,@RequestParam String mobileNum,@RequestParam String email,@RequestParam String subject,
                                    @RequestParam String message)
    {
        log.info("Name : " + name);
        log.info("Mobile Number : " + mobileNum);
        log.info("Email Address : " + email);
        log.info("Subject : " + subject);
        log.info("Message : " + message);
        return new ModelAndView("redirect:/contact");

    }*/
// --STEP 2
    /* @RequestMapping(value = "/saveMsg",method = POST)
    public ModelAndView saveMessage(Contact contact)
    {
        return new ModelAndView("redirect:/contact");
    }*/
// for validation some changes required.
// -- STEP 5
@RequestMapping(value = "/saveMsg",method = POST)
public String saveMessage(@Valid @ModelAttribute("contact") Contact contact, Errors errors) {
    if(errors.hasErrors()){
        log.error("Contact form validation failed due to : " + errors.toString());
        return "contact.html";
    }
    contactService.saveMessageDetails(contact);
    return "redirect:/contact";
}

    /*@RequestMapping("/displayMessages")
    public ModelAndView displayMessages(Model model) {
        List<Contact> contactMsgs = contactService.findMsgsWithOpenStatus();
        ModelAndView modelAndView = new ModelAndView("messages.html");
        modelAndView.addObject("contactMsgs",contactMsgs);
        return modelAndView;
    }
*/
   /*
   In that, since we are sending to query Param one with the "page num, sort  field and sort the direction"
   */
    @RequestMapping("/displayMessages/page/{pageNum}")
    public ModelAndView displayMessages(Model model,
                                        @PathVariable(name = "pageNum") int pageNum, @RequestParam("sortField") String sortField,
                                        @RequestParam("sortDir") String sortDir) {
        //we are passing pageNum,sortField,SortDir to the contact service.
        Page<Contact> msgPage = contactService.findMsgsWithOpenStatus(pageNum,sortField,sortDir);
        List<Contact> contactMsgs = msgPage.getContent();
       /* above  contactMsgs is used in messages.html  pass object in Thymleaf observe in table body.
       Line no: 70
       <tr th:each="msg: ${contactMsgs}">
                <td th:text="${msg.name}"></td>
                <td th:text="${msg.mobileNum}"></td>
                <td th:text="${msg.email}"></td>
                <td th:text="${msg.subject}"></td>
                <td th:text="${msg.message}"></td>
                <td><a th:href="@{/closeMsg(id=${msg.contactId})}" class="btn btn-warning">CLOSE</a></td>
            </tr>
       * */

        ModelAndView modelAndView = new ModelAndView("messages.html");
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", msgPage.getTotalPages());
        model.addAttribute("totalMsgs", msgPage.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);

        /*  if we see here, we are also sending the  reverse sorting of direction available and how I'm sending is
        --->   sortDir.equals("asc") ? "desc" : "asc"
        I'm going to send the descending as reverse sorting direction. if my current sorting  direction is ascending,
        I'm going to send that descending as the reverse  sorting direction. Otherwise, I'm going to send the ascending
        as the reverse sorting direction.
       */
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        modelAndView.addObject("contactMsgs",contactMsgs);
        return modelAndView;
    }


    @RequestMapping(value = "/closeMsg",method = GET)
    public String closeMsg(@RequestParam int id, Authentication authentication) {
        contactService.updateMsgStatus(id);
        /*
        After closing message we need return the webpage link if it's not given we are getting some exception.
        */
        return "redirect:/displayMessages/page/1?sortField=name&sortDir=desc";
    }



}





