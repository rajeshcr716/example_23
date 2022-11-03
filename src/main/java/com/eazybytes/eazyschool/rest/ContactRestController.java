package com.eazybytes.eazyschool.rest;

import com.eazybytes.eazyschool.constants.EazySchoolConstants;
import com.eazybytes.eazyschool.model.Contact;
import com.eazybytes.eazyschool.model.Response;
import com.eazybytes.eazyschool.repository.ContactRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
//@Controller
@RestController
@RequestMapping(path = "/api/contact",produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
@CrossOrigin(origins = "*")
 /*it means more than one coming from rest service tool like postman then error came so
 we allow request any no of domain like ex: (http://localhost:8080)
*/
public class ContactRestController {


    @Autowired
    ContactRepository contactRepository;
/* 1st method accept the http page request and with the path /api/contact/getAllMsgsByStatus
 @2nd method @ResponseBody
So this annotation gives an indication to my spring MVC framework.
Please do not expect any new information as a Return statement from this method.
So that model and view attribute code not written.
 Below Java code we have to fetch the ststus information from the contact Repository.
    */
    @GetMapping("/getMessagesByStatus")
    //@ResponseBody
    public List<Contact> getMessagesByStatus(@RequestParam(name = "status")  String status){
        return contactRepository.findByStatus(status);
    }


   /*@RequestBody Will convert JSON format request comming from the end user into the Java object.
   in that @RequestBody will send the inforamtion to the "contact" variable.

  @ResponseBody convert JSON format because other application like built on different technologies like python,rudby,,etc
  those not understooed java object so @ResponseBody convert to JSON format

  @RestController = @Controller + @ResponseBody
  So that we commented @ResponseBody and used @RestController at the top of the java class.
 */
    @GetMapping("/getAllMsgsByStatus")
   //@ResponseBody

   public List<Contact> getAllMsgsByStatus(@RequestBody Contact contact){
      /*
      in that contact!=null ...means we sending open and close status through postman through above link
      --> http://localhost:8080/api/contact/getAllMsgsByStatus
      Request sent in the form json .. after receving that json format @Request body sent that message to contact vairable..
      {
    "status":"Close"
      }
      */
        if(null != contact && null != contact.getStatus()){
            return contactRepository.findByStatus(contact.getStatus());
        }else{
            return List.of();
        }
    }

   /*
   some we need to accept and fecth header information also so that ..we @RequestHeader annoataion useed. 1st parameter
   is the invocationFrom  , and 2nd one @RequestBody
   below line @Valid used because validation needed. rest service
   So whenever you're using ResponseEntity , we should also tell to this response entity class.--><Response>
   so I added Response class inside ResponseEntity<Response>
   important --> body(response); whatever we mentioned inside body is same class present inside the ResponseEntity

   @PostMapping this method going to save the information into the database
   */
    @PostMapping("/saveMsg")
   // @ResponseBody

    public ResponseEntity<Response> saveMsg(@RequestHeader("invocationFrom") String invocationFrom,
                                            @Valid @RequestBody Contact contact){
       /* Request header means ..request the header section (key,value) in the postman. see we added
       key = "invocationFrom" value = "mobile" in our console it display after validation sucessfull save message
       */
        log.info(String.format("Header invocationFrom = %s", invocationFrom));
        contactRepository.save(contact);
        Response response = new Response();
        response.setStatusCode("200");
        response.setStatusMsg("Message saved successfully");
        return ResponseEntity
                .status(HttpStatus.CREATED) // status is "CREATED"
                .header("isMsgSaved", "true") //sending header information
                .body(response);
    }

    @DeleteMapping("/deleteMsg")
    public ResponseEntity<Response> deleteMsg(RequestEntity<Contact> requestEntity){
     /*
     @DeleteMapping is used to delete the record from the database.
     this another type like @RequestHeader
     RequestEntity<Contact> requestEntity.  instead of calling Request we call   .getBody() in below section
     */
        HttpHeaders headers = requestEntity.getHeaders();
        headers.forEach((key, value) -> {
            log.info(String.format(
                    "Header '%s' = %s", key, value.stream().collect(Collectors.joining("|"))));
        });
        Contact contact = requestEntity.getBody();
        contactRepository.deleteById(contact.getContactId()); //After calling this line deleting operation will be successfull.
        Response response = new Response();
        response.setStatusCode("200");
        response.setStatusMsg("Message successfully deleted");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PatchMapping("/closeMsg")
    public ResponseEntity<Response> closeMsg(@RequestBody Contact contactReq){
        Response response = new Response();
        Optional<Contact> contact = contactRepository.findById(contactReq.getContactId());

        if(contact.isPresent()){
            contact.get().setStatus(EazySchoolConstants.CLOSE);
            contactRepository.save(contact.get());
        }else{
            response.setStatusCode("400");
            response.setStatusMsg("Invalid Contact ID received");
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }
        response.setStatusCode("200");
        response.setStatusMsg("Message successfully closed");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }






}
