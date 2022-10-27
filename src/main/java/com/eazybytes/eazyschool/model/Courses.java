package com.eazybytes.eazyschool.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
//In db table name is same as entity class so @Table not required
// In side entity/pojo class field need to match with table inside db.
@Entity
public class Courses extends  BaseEntity{

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO,generator="native")
    @GenericGenerator(name = "native",strategy = "native")
    private int courseId;

    private String name;

    private String fees;
/* In that we used EAGER fetch type because whenever we load the course type loading
person type also usefull . course enrolled by the person name we got.

 */
@ManyToMany(mappedBy = "courses", fetch = FetchType.EAGER,cascade = CascadeType.PERSIST)

  // Person is a colletion multiple person/user  enroll same course.
    private Set<Person> persons = new HashSet<>();

}
