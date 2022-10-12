package com.eazybytes.eazyschool.repository;

import com.eazybytes.eazyschool.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepositary extends JpaRepository<Person, Integer> {
    Person readByEmail(String email);//It will inform the spring boot to the run select query to chceck email.. like getName(..)
    //these are we manually adding
}
