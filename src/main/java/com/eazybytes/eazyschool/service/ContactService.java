package com.eazybytes.eazyschool.service;


import com.eazybytes.eazyschool.constants.EazySchoolConstants;
import com.eazybytes.eazyschool.model.Contact;

import com.eazybytes.eazyschool.repository.ContactRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<Contact> findMsgsWithOpenStatus() {
        List<Contact> contactMsgs = contactRepository.findByStatus(EazySchoolConstants.OPEN);
        return contactMsgs;
    }

    public boolean updateMsgStatus(int contactId) {
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
    }
}