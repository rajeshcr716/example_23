package com.eazybytes.eazyschool.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Map;

/*
 @PropertySource("classPath:some.Properties") just ex: we need get property other than app.property then we need declare.
@Component("eazySchoolProps") //creating a bean with name "eazyScollProps
@Data
  need to mention the property present in some other file.
@ConfigurationProperties(prefix = "eazyschool")


above annotation need to mention prefix. check that prefix present in appl.properties.
filed in bean class must match with field app.properties.

@Validated // It is used perform all validation operation on the properties.

*/

@Component("eazySchoolProps")
@Data
@ConfigurationProperties(prefix = "eazyschool")
@Validated
public class EazySchoolProps {

    @Min(value=5, message="must be between 5 and 25")
    @Max(value=25, message="must be between 5 and 25")
    /*above condition is used for the pagesize and  @Validate will validate the value. */
    private int pageSize;
    private Map<String, String> contact; //explained below section
    private List<String> branches;

    //Check contact service page size code will be set using this code.

/*
    Application.properties.

    Map<String,String> --WILL BE CONSIDERED LIKE THIS  CHECK ONCE
    <key,value> --> <pageSize,10>
    eazyschool.pageSize=10
    eazyschool.contact.pageSize=5


           ---> <successMsg,"Your message is submitted successfully">
    eazyschool.contact.successMsg=Your message is submitted successfully.

    <baranches[0],NewYork>
    eazyschool.branches[0]=NewYork
    eazyschool.branches[1]=Delhi
    eazyschool.branches[2]=Paris
    eazyschool.branches[3]=Singapore
    */


}
