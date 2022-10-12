package com.eazybytes.eazyschool.repository;

import com.eazybytes.eazyschool.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
//JPA Repo... is interface specific to the spring data jpa itself. It internally extending pagingAndSoring Repositary also
public interface RolesRepository extends JpaRepository<Roles, Integer> {

    Roles getByRoleName(String roleName); // we added roles in data.sql at the starting
}
