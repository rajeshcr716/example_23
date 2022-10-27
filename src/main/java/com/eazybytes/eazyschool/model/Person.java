package com.eazybytes.eazyschool.model;

import com.eazybytes.eazyschool.annotation.FieldsValueMatch;
import com.eazybytes.eazyschool.annotation.PasswordValidator;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

//@Data
@Getter
@Setter

@Entity
@FieldsValueMatch.List({   //click on the ".List"
        // see "field" and "fieldMatch" ..need to compare . validation happend in "@FieldValueMatch..inside implemented class
      // "FieldValueMatchValidator"  field and fieldMatch comparison code is there
        @FieldsValueMatch(
                field = "pwd",
                fieldMatch = "confirmPwd",
                message = "Passwords do not match!"
        ),
        @FieldsValueMatch(
                field = "email",
                fieldMatch = "confirmEmail",
                message = "Email addresses do not match!"
        )
})
public class Person extends BaseEntity{

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO,generator="native")
    @GenericGenerator(name = "native",strategy = "native")
    private int personId;

    @NotBlank(message="Name must not be blank")
    @Size(min=3, message="Name must be at least 3 characters long")
    private String name;

    @NotBlank(message="Mobile number must not be blank")
    @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
    private String mobileNumber;

    @NotBlank(message="Email must not be blank")
    @Email(message = "Please provide a valid email address" )

    private String email;

    @NotBlank(message="Confirm Email must not be blank")
    @Email(message = "Please provide a valid confirm email address" )
    @Transient // it is used only for verification/user password check.. no need include in table creation.
    private String confirmEmail;

    @NotBlank(message="Password must not be blank")
    @Size(min=5, message="Password must be at least 5 characters long")
    @PasswordValidator
    private String pwd;

    @NotBlank(message="Confirm Password must not be blank")
    @Size(min=5, message="Confirm Password must be at least 5 characters long")
    @Transient // it inform string jpa to doesn't include this attribute as table/sql operation while creation table,update,delete ..etc.
    private String confirmPwd;

    //fetch means do you want to fetch the child entiry also egalry or Lazily.
    //Whenver I trying to fetch the Person record table does my spring data jpa automatically fetch the child table record or not
    //that can by control by fetch type (Eager or Lazy)
    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.PERSIST, targetEntity = Roles.class)
    @JoinColumn(name = "role_id", referencedColumnName = "roleId",nullable = false)
    private Roles roles;

    //cascade type we have parent and child relation any operation doing on parent does the same needs to be cascade on child also
    // if I delete the parent object then spring jpa will the delete child object also. That type of cascade configuration can be
    //done by CascadeType. if I mention ALL then operation done parent will also affect the child ex: If delete parent table address also
    //deleted.

   //TargetEntity-- that is an optional class just for readability purpose we maintain that
  //@JoinColumn-- Address has one to one relation with which entity or column that configuration can be done with help of @JoinColumn
    // first need to mention column name 2nd refColName means using this spring jpa will know the which column in the Address has one to one
    //relation and 3rd one has nullable it means relation with child and parent at staring point is null I think check once.


    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL, targetEntity = Address.class)
    @JoinColumn(name = "address_id", referencedColumnName = "addressId",nullable = true)
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "class_id", referencedColumnName = "classId", nullable = true)
    private EazyClass eazyClass;
 /*
     FETCH-->LAZY
    when loading person entiry it will not read and load the child entiry ex: lazy fecting of person it does't fetch the Address table..
    @JoinColumn annotation is used to specify the foreign key details.
*/


/* @JoinTable is used to join the column from different table
name = "person_courses" means this is the table name contain PERSON_ID and COURSE_ID
referencedColumnName = "personId" column name in the person table is ref column.
 inverseJoinColumns  means need join from other table column then ...it will be used.
 */
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name = "person_courses",
            joinColumns = {
                    @JoinColumn(name = "person_id", referencedColumnName = "personId")},
            inverseJoinColumns = {
                    @JoinColumn(name = "course_id", referencedColumnName = "courseId")})
    private Set<Courses> courses = new HashSet<>();





}
