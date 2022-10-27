package com.eazybytes.eazyschool.controller;


import com.eazybytes.eazyschool.model.Courses;
import com.eazybytes.eazyschool.model.EazyClass;
import com.eazybytes.eazyschool.model.Person;
import com.eazybytes.eazyschool.repository.CoursesRepository;
import com.eazybytes.eazyschool.repository.EazyClassRepository;
import com.eazybytes.eazyschool.repository.PersonRepositary;
import lombok.extern.slf4j.Slf4j;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.OrderBy;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("admin")
public class AdminController {


    @Autowired
    EazyClassRepository eazyClassRepository;

    @Autowired
    PersonRepositary personRepositary;

    @Autowired
    CoursesRepository coursesRepository;

    @RequestMapping("/displayClasses")
    public ModelAndView displayClasses(Model model) {
        //It will fetch all the information inside eazyschool application
        List<EazyClass> eazyClasses = eazyClassRepository.findAll();
        ModelAndView modelAndView = new ModelAndView("classes.html");
        //In that we are trying send the eazy class information to the UI. object name as eazy class
        modelAndView.addObject("eazyClasses", eazyClasses);

        modelAndView.addObject("eazyClass", new EazyClass());
        return modelAndView;
    }

    @PostMapping("/addNewClass")
    public ModelAndView addNewClass(Model model, @ModelAttribute("eazyClass") EazyClass eazyClass) {
        eazyClassRepository.save(eazyClass);
        ModelAndView modelAndView = new ModelAndView("redirect:/admin/displayClasses");
        return modelAndView;
    }

    @RequestMapping("/deleteClass")
    public ModelAndView deleteClass(Model model, @RequestParam int id) {
        Optional<EazyClass> eazyClass = eazyClassRepository.findById(id);
        for (Person person : eazyClass.get().getPersons()) {
            person.setEazyClass(null);
            personRepositary.save(person);
        }
        eazyClassRepository.deleteById(id);
        ModelAndView modelAndView = new ModelAndView("redirect:/admin/displayClasses");
        return modelAndView;
    }

    @GetMapping("/displayStudents")
    public ModelAndView displayStudents(Model model, @RequestParam int classId, HttpSession session,
                                        @RequestParam(value = "error", required = false) String error) {
        String errorMessage = null;
        ModelAndView modelAndView = new ModelAndView("students.html");
        Optional<EazyClass> eazyClass = eazyClassRepository.findById(classId);
        modelAndView.addObject("eazyClass",eazyClass.get());
        modelAndView.addObject("person",new Person());
        session.setAttribute("eazyClass",eazyClass.get());
        if(error != null) {
            errorMessage = "Invalid Email entered!!";
            modelAndView.addObject("errorMessage", errorMessage);
        }
        return modelAndView;
    }








    @PostMapping("/addStudent")
    public ModelAndView addStudent(Model model, @ModelAttribute("person") Person person, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        EazyClass eazyClass = (EazyClass) session.getAttribute("eazyClass");
        Person personEntity =  personRepositary.readByEmail(person.getEmail());
        if(personEntity==null || !(personEntity.getPersonId()>0)){
            modelAndView.setViewName("redirect:/admin/displayStudents?classId="+eazyClass.getClassId()
                    +"&error=true");
            return modelAndView;
        }
        personEntity.setEazyClass(eazyClass);
         personRepositary.save(personEntity);
        eazyClass.getPersons().add(personEntity);
        eazyClassRepository.save(eazyClass);
        modelAndView.setViewName("redirect:/admin/displayStudents?classId="+eazyClass.getClassId());
        return modelAndView;
    }

    @GetMapping("/deleteStudent")
    public ModelAndView deleteStudent(Model model, @RequestParam int personId, HttpSession session) {
        EazyClass eazyClass = (EazyClass) session.getAttribute("eazyClass");
        Optional<Person> person = personRepositary.findById(personId);
        person.get().setEazyClass(null);
        eazyClass.getPersons().remove(person.get());
        EazyClass eazyClassSaved = eazyClassRepository.save(eazyClass);
        session.setAttribute("eazyClass",eazyClassSaved);
        ModelAndView modelAndView = new ModelAndView("redirect:/admin/displayStudents?classId="+eazyClass.getClassId());
        return modelAndView;
    }


    @GetMapping("/displayCourses")
    public ModelAndView displayCourses(Model model) {
      //  List<Courses> courses = coursesRepository.findByOrderByNameDesc();
       // List<Courses> courses = coursesRepository.findByOrderByNameDesc();

      //Below line specifies the Dynamic sorting jpa
      //  List<Courses> courses = coursesRepository.findAll(Sort.by("name").ascending().and(Sort.by("courseId")));
        List<Courses> courses = coursesRepository.findAll(Sort.by("name").ascending());

        ModelAndView modelAndView = new ModelAndView("courses_secure.html");
        modelAndView.addObject("courses",courses);
        modelAndView.addObject("course", new Courses());
        return modelAndView;
    }

    @PostMapping("/addNewCourse")
    public ModelAndView addNewCourse(Model model, @ModelAttribute("course") Courses course) {
        ModelAndView modelAndView = new ModelAndView();
        coursesRepository.save(course);
        modelAndView.setViewName("redirect:/admin/displayCourses");
        return modelAndView;
    }
/* @RequestParam we have receiving from web page .
* */
@GetMapping("/viewStudents")
/*  @Req Param.. req=false means adding email section. value = error just alias . String error is an param. just sending
error message to UI. intailly error = null.
* */
public ModelAndView viewStudents(Model model, @RequestParam int id
        ,HttpSession session,@RequestParam(required = false) String error) {
    String errorMessage = null;
    ModelAndView modelAndView = new ModelAndView("course_students.html");
    Optional<Courses> courses = coursesRepository.findById(id);
    modelAndView.addObject("courses",courses.get());
    modelAndView.addObject("person",new Person());
    session.setAttribute("courses",courses.get());
    if(error != null) {
        errorMessage = "Invalid Email entered!!";
        modelAndView.addObject("errorMessage", errorMessage);
    }
    return modelAndView;
}

    @PostMapping("/addStudentToCourse")
    public ModelAndView addStudentToCourse(Model model, @ModelAttribute("person") Person person,
                                           HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        Courses courses = (Courses) session.getAttribute("courses");
        Person personEntity = personRepositary.readByEmail(person.getEmail());
        if(personEntity==null || !(personEntity.getPersonId()>0)){
            modelAndView.setViewName("redirect:/admin/viewStudents?id="+courses.getCourseId()
                    +"&error=true");
            return modelAndView;
        }
        personEntity.getCourses().add(courses);
        courses.getPersons().add(personEntity);
         personRepositary.save(personEntity);
        session.setAttribute("courses",courses);
        modelAndView.setViewName("redirect:/admin/viewStudents?id="+courses.getCourseId());
        return modelAndView;
    }

   // In that we not used @DeleteMapping because hard delete / direct delete from db is not needed.. need to learn in depth
   // no issue we can use @GetMapping or @DeleteMapping no issue
    @GetMapping("/deleteStudentFromCourse")
    public ModelAndView deleteStudentFromCourse(Model model, @RequestParam int personId,
                                                HttpSession session) {
      /* from which course admin need to delete the person so coursed repository taken.
     we have to take course from the current session
     * */
        Courses courses = (Courses) session.getAttribute("courses");
        Optional<Person> person = personRepositary.findById(personId);
       // in that we removing to break the line b/w person and particular course.
        person.get().getCourses().remove(courses);
        courses.getPersons().remove(person);
         personRepositary.save(person.get());
        session.setAttribute("courses",courses);
        ModelAndView modelAndView = new
                ModelAndView("redirect:/admin/viewStudents?id="+courses.getCourseId());
        return modelAndView;
    }

    /*
 Adding this two to three lines of the code inside my Java methods and everything else is being taken
care of by my spring  frameworks like creating a connection, creating a statement, execute the statement.
And once that statement is executed, closing the connection, closing the transaction.
All magic will happen

--Why are we using Session to save the courses
 Courses courses = (Courses) session.getAttribute("courses")

  When the admin lands on the View Students page, he can add/delete the students from the course.
  But the admin simply click the Add/Delete button with out providing any course details like from which course
  he want to delete the student.

 So that's why we store the current course details inside the session object
 when admin is trying to land onto the students page from the course page.
 This way we can use the same while deleting/adding the student.


     */




}
