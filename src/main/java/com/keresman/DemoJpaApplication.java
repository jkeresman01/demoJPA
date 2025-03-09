package com.keresman;

import com.github.javafaker.Faker;
import com.keresman.model.Student;
import com.keresman.repository.StudentIdCardRepository;
import com.keresman.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Slf4j
@SpringBootApplication
public class DemoJpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoJpaApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(
            StudentRepository studentRepository,
            StudentIdCardRepository studentIdCardRepository) {
        return args -> { };
    }

    private static void fillDBWithRandomStudents(StudentRepository studentRepository) {
        var faker = new Faker();

        for (int i = 0; i < 50; ++i) {
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            String email = String.format("%s.%s@gmail.com", firstName, lastName);

            var student = new Student(
                    firstName,
                    lastName,
                    email,
                    faker.number().numberBetween(1, 100)
            );

            studentRepository.save(student);
        }

        //pagingDemo(studentRepository);
        //sortingDemo(studentRepository);
    }

    private static void pagingDemo(StudentRepository studentRepository) {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("age"));
        Page<Student> pageResult = studentRepository.findAll(pageRequest);
        pageResult.nextPageable().next();
        pageResult.getContent().forEach(System.out::println);
        System.out.println(pageResult);
    }

    private static void sortingDemo(StudentRepository studentRepository) {
        studentRepository.findAll(Sort.by(Sort.Direction.DESC, "firstName")).stream()
                .map(Student::getFirstName)
                .forEach(System.out::println);

        Sort sort = Sort.by(Sort.Direction.DESC, "age")
                .and(Sort.by(Sort.Direction.ASC, "firstName"));

        studentRepository.findAll(sort).forEach(System.out::println);
    }
}
