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

import java.util.ArrayList;
import java.util.List;

@Component

/* AuthenticationProvider   it is just an interface it contain two method
1)Authnticate--we can write our own custom logic
2) support--*/

public class EazySchoolUsernamePwdAuthenticationProvider implements AuthenticationProvider
{
    @Autowired
    private PersonRepositary personRepository;


   @Autowired
   private PasswordEncoder passwordEncoder;

    @Override
   //using authentication object we receiving the authentication parameter
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
     //Now we try to get the username and password and what are the credential

        String email = authentication.getName();
        // which will give me the password entered by my end user.
        String pwd = authentication.getCredentials().toString();

        //using person Repo..we pass the email paramter to db.
        Person person = personRepository.readByEmail(email);

        if(null != person && person.getPersonId()>0 &&
                passwordEncoder.matches(pwd,person.getPwd()))      {

            // pas..matches() --> convert user entered pw into hash text after it start to compare hash aleready present in db.

            /*pwd.equals(person.getPwd())*/
            /*now here, in order to send the roles information to mainspring security "UsernamePasswordAuthenticationToken
            sent in the format of granted authority.  grant authority has interface inside spring in that we mention particular
            role of the person "*/
            return new UsernamePasswordAuthenticationToken(
                    email, pwd, getGrantedAuthorities(person.getRoles()));
           /*  Once my spring security is confident that the authentication operation is completed successfully,
             it  is going to erase the password credentials that are available inside this object.
             security purpose spring will not hold the pwd object       */

        }else{
            throw new BadCredentialsException("Invalid credentials!");
        }
    }

    //In the we receiveing the role information from the database
    private List<GrantedAuthority> getGrantedAuthorities(Roles roles) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_"+roles.getRoleName()));
        // I'm trying to get the role information by calling this  roles.getRoleName()
        return grantedAuthorities;
    }

    @Override
    // In that we need to check authentication type is same as username authentication or not..
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
