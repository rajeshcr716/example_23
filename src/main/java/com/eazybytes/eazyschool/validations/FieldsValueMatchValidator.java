package com.eazybytes.eazyschool.validations;

import com.eazybytes.eazyschool.annotation.FieldsValueMatch;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

// <FieldValuematch, Object> ..declared as object because password field contain string,integer spl char...etc.
// interface present annotation package

public class FieldsValueMatchValidator
        implements ConstraintValidator<FieldsValueMatch, Object> {

    private String field;
    private String fieldMatch;

    @Override //-->This intialization method fields
    //we have to fetch field,fieldMatch name form "FieldValueMatch"
    public void initialize(FieldsValueMatch constraintAnnotation) {
        this.field = constraintAnnotation.field();             // calling field method
        this.fieldMatch = constraintAnnotation.fieldMatch();   //calling fieldMatch method
    }

    @Override //--> this Bussiness logic if the fields are valid or not.
    //we pass the object into the "Const..ValidatorContext"
    public boolean isValid(Object value,ConstraintValidatorContext context) {
        //using beanWrapper we fecth value of the field.
        Object fieldValue = new BeanWrapperImpl(value)         //create object of field .
                .getPropertyValue(field);
        Object fieldMatchValue = new BeanWrapperImpl(value)    //create object of field match.
                .getPropertyValue(fieldMatch);
        //compare field and fieldMatch ... using those object
        /*if (fieldValue != null) {
            return fieldValue.equals(fieldMatchValue);
        }
        if(fieldValue.toString().startsWith("$2a")){
            return true;
        }
        else {
            return fieldMatchValue == null;
        }*/
        //field means value comming from end user and fieldMatch backend code like pattern check oncce..in depth.

        if (fieldValue != null) {
            return fieldValue.equals(fieldMatchValue);
        } else {
            return fieldMatchValue == null;
        }

    }

}
