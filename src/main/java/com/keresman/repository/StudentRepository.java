package com.keresman.repository;

import com.keresman.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    @Query(
            value = "SELECT * FROM student s WHERE s.id = :id",
            nativeQuery = true
    )
    Optional<Student> findById(@Param("id")Long id);

    @Query(
            "SELECT s FROM Student s " +
            "WHERE s.firstName = :firstName AND s.age >= :age"
    )
    Optional<Student> findByFirstnameAndAgeGreaterThan(
            @Param("firstName") String firstName,
            @Param("age") int age);

    @Transactional
    @Modifying
    @Query("DELETE Student s WHERE s.id = :id")
    void deleteById(@Param("id") Long id);
}
