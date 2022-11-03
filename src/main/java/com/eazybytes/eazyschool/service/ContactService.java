package com.eazybytes.eazyschool.service;


import com.eazybytes.eazyschool.config.EazySchoolProps;
import com.eazybytes.eazyschool.constants.EazySchoolConstants;
import com.eazybytes.eazyschool.model.Contact;

import com.eazybytes.eazyschool.repository.ContactRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
//@RequestScope
//@SessionScope
//@ApplicationScope
//@Data
public class ContactService {

    private int counter = 0;

    public ContactService() {
        System.out.println("Contact service Bean initialized");
    }


    //Now logger object needed no more because above @Slf4j annotaion generate log object by default  based on class names
    //another great advantage is by mistake if we define another class name .The log came from that  class..(consider below line)
    //...(ContactController.class) So this is wrong..manuall mistake . using annotaion by default it populate current class object.

    /*private static Logger log = LoggerFactory.getLogger(ContactService.class);*/

    /**
     * Save Contact Details into DB
     *
     * @param contact
     * @return boolean
     */

    //STEP 3
    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    EazySchoolProps eazySchoolProps;

    /**
     * Save Contact Details into DB
     *
     * @param contact
     * @return boolean
     */
    public boolean saveMessageDetails(Contact contact) {
        boolean isSaved = false;
        contact.setStatus(EazySchoolConstants.OPEN);

        Contact savedContact = contactRepository.save(contact);
        if (null != savedContact && savedContact.getContactId() > 0) {
            isSaved = true;
        }
        return isSaved;
    }

/*
    public List<Contact> findMsgsWithOpenStatus() {
        List<Contact> contactMsgs = contactRepository.findByStatus(EazySchoolConstants.OPEN);
        return contactMsgs;
    }*/

   /*   we are building the pagination related configurations along with
    that dynamic sorting requirements that we have.
    pageNum - 1 --> indicating current page . Spring data jpa start from page 0 so we alawasy subustract pageno-1.
    getting the pageno from the UI.
    */
    public Page<Contact> findMsgsWithOpenStatus(int pageNum, String sortField, String sortDir){

       /*  see this one commented because by default valu it taking from eazySchoolProps and application propertes.
        */

        //  int pageSize = 5;



        int pageSize = eazySchoolProps.getPageSize();
        if(null!=eazySchoolProps.getContact() && null!=eazySchoolProps.getContact().get("pageSize")){
            pageSize = Integer.parseInt(eazySchoolProps.getContact().get("pageSize").trim());
        }



        Pageable pageable = PageRequest.of(pageNum - 1, pageSize,
             /*   BELOW   if we see this is a ternary operator that we have inside the Java.
             So what I am doing here is if my sort of direction is equal to ascending. I'm trying to build my sort object
             based upon this code.
               I'm getting as a method input parameter  from "String sortDir" .
           If my sort direction is descending then 2nd condition will be excuted.--> " : Sort.by(sortField).descending()); "
             */
                sortDir.equals("asc") ? Sort.by(sortField).ascending()
                        : Sort.by(sortField).descending());
      /*   ones we have built this pagination configurations and our sorting requirements.
      We need to pass this "pageable" param to the  contact repository.
       "findByStatus" we built in contactRepository

      findByStatus contain two parameter one is status and another one is pageable.
      return type of findByStatus is Page<contact>
      Spring Data jpa pagination code wrapped inside the Page<--> inbuit interface.
      so that above "Page<Contact>" findBy.... return type is mentioned.
      Now go to contact controller..we get this on line no. 66
       */

        Page<Contact> msgPage = contactRepository.findByStatusWithRepository(EazySchoolConstants.OPEN,pageable);
        return msgPage;
    }



    /*public boolean updateMsgStatus(int contactId) {
        boolean isUpdated = false;
        Optional<Contact> contact = contactRepository.findById(contactId);
        contact.ifPresent(contact1 -> {
            contact1.setStatus(EazySchoolConstants.CLOSE);

        });
        Contact updatedContact = contactRepository.save(contact.get());
        if (null != updatedContact && updatedContact.getUpdatedBy() != null) {
            isUpdated = true;
        }
        return isUpdated;
    }*/
/* So previously,   first  we will try to load the current record that we have
   inside the database based upon the contact ID by using the Find By ID.
  ----> Optional<Contact> contact = contactRepository.findById(contactId);

   top of the record we are going to update the status. and save the message to the pojo class. (class name is table name)
   but we don't need  all these code right now because we don't have to load the entity from the database.
   We can directly update the status with the help of update query that we have written in contact repository.

* */
    public boolean updateMsgStatus(int contactId){
        boolean isUpdated = false;
        int rows = contactRepository.updateStatusById(EazySchoolConstants.CLOSE,contactId);
        if(rows > 0) {
            isUpdated = true;
        }
        return isUpdated;
    }
    /* the before logic that we used to have. First, we need to fetch. Then we need to update.
   But here in which we are doing directly updating  the record with the help of custom code
   performance improvement is there with the new logic */

}