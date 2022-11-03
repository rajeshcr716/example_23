package com.eazybytes.eazyschool.security;


import com.eazybytes.eazyschool.model.Person;
import com.eazybytes.eazyschool.model.Roles;
import com.eazybytes.eazyschool.repository.PersonRepositary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.management.relation.Role;
import java.util.ArrayList;
import java.util.List;

@Component
public class EazySchoolUsernamePwdAuthenticationProvider
        implements AuthenticationProvider {

    @Autowired
    private PersonRepositary personRepositary;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String email = authentication.getName();
        String pwd = authentication.getName();

        Person person = personRepositary.readByEmail(email);

        if(null != person && person.getPersonId()>0 &&
                passwordEncoder.matches(pwd,person.getPwd())){
            return new UsernamePasswordAuthenticationToken(
                    email, null, getGrantedAuthorites(person.getRoles()));
        }else{
            throw new BadCredentialsException("Invalid credentials!");
        }

    }

    private List<GrantedAuthority> getGrantedAuthorites(Roles roles){
      List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
       grantedAuthorityList.add(new SimpleGrantedAuthority("ROLE_"+roles.getRoleName()));
        return  grantedAuthorityList;
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
