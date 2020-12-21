package com.mariiapasichna.repository;

import com.mariiapasichna.model.State;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.NoSuchElementException;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class StateRepositoryTests {
    @Autowired
    private StateRepository stateRepository;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void getByNameTest() {
        State expected = new State();
        expected.setName("Test");

        entityManager.persist(expected);
        entityManager.flush();

        State actual = stateRepository.getByName(expected.getName()).get();

        Assertions.assertEquals(expected, actual);
        Assertions.assertThrows(NoSuchElementException.class, () -> stateRepository.getByName("TestException").get());
    }
}