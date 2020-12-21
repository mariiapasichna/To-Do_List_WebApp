package com.mariiapasichna.repository;

import com.mariiapasichna.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.NoSuchElementException;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void findByEmailTest() {
        User expected = new User();
        expected.setFirstName("Firstname");
        expected.setLastName("Lastname");
        expected.setEmail("test@test.com");
        expected.setPassword("Password-1");

        entityManager.persist(expected);
        entityManager.flush();

        User actual = userRepository.findByEmail(expected.getEmail()).get();

        Assertions.assertEquals(expected, actual);
        Assertions.assertThrows(NoSuchElementException.class, () -> userRepository.findByEmail("exception@test.com").get());
    }
}