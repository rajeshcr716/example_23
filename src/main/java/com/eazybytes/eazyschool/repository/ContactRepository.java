package com.eazybytes.eazyschool.repository;

import com.eazybytes.eazyschool.model.Contact;

import com.eazybytes.eazyschool.model.Holiday;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

/*
@Repository stereotype annotation is used to add a bean of this class
type to the Spring context and indicate that given Bean is used to perform
DB related operations and
* */
/*
@Repository
public class ContactRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ContactRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int saveContactMsg(Contact contact){
        String sql = "INSERT INTO CONTACT_MSG (NAME,MOBILE_NUM,EMAIL,SUBJECT,MESSAGE,STATUS," +
                "CREATED_AT,CREATED_BY) VALUES (?,?,?,?,?,?,?,?)";
        return jdbcTemplate.update(sql,contact.getName(),contact.getMobileNum(),
                contact.getEmail(),contact.getSubject(),contact.getMessage(),
                contact.getStatus(),contact.getCreatedAt(),contact.getCreatedBy());
    }

     public List<Contact> findMsgsWithStatus(String status) {
        String sql = "SELECT * FROM CONTACT_MSG WHERE STATUS = ?";
        return jdbcTemplate.query(sql,new PreparedStatementSetter() {
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setString(1, status);
            }
        },new ContactRowMapper());
    }

    public int updateMsgStatus(int contactId, String status,String updatedBy) {
        String sql = "UPDATE CONTACT_MSG SET STATUS = ?, UPDATED_BY = ?,UPDATED_AT =? WHERE CONTACT_ID = ?";
        return jdbcTemplate.update(sql,new PreparedStatementSetter() {
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setString(1, status);
                preparedStatement.setString(2, updatedBy);
                preparedStatement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
                preparedStatement.setInt(4, contactId);
            }
        });
    }

}
*/

@Repository
public interface ContactRepository extends CrudRepository<Contact,Integer> {

List<Contact> findByStatus(String status);

   //@Query("SELECT c FROM Contact c WHERE c.status = :status")

  @Query(value = "SELECT * FROM contact_msg WHERE contact_msg.status = :status", nativeQuery = true)

    // page is an inbuilt interface
  Page<Contact> findByStatusWithRepository(@Param("status") String status, Pageable pageable);
  /*
  "status" The first one is  what is the status that we need to  fetch from the record from db

  "pageable"   the second one is pagination configurations present in contact service.
*/


  //below two Annotation are mandatory while writing jpa queries these changes status of the db. update,delete... etc operation.
  @Transactional
  @Modifying
  @Query("UPDATE Contact c SET c.status = ?1 WHERE c.contactId = ?2")
  int updateStatusById(String status, int id);
  /*  "?1" means it related "status" and "?2" means it related to "id".  need to check / learn in depth...
  int is the return type here*/

  Page<Contact> findOpenMsgs(@Param("status") String status, Pageable pageable);

  @Transactional
  @Modifying
  int updateMsgStatus(String status, int id);

  @Query(nativeQuery = true)
  Page<Contact> findOpenMsgsNative(@Param("status") String status, Pageable pageable);

  @Transactional
  @Modifying
  @Query(nativeQuery = true)
  int updateMsgStatusNative(String status, int id);




}