package com.mariiapasichna.repository;

import com.mariiapasichna.model.ToDo;
import com.mariiapasichna.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ToDoRepositoryTests {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ToDoRepository toDoRepository;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void findAllByOwnerIdTest() {
        User user = new User();
        user.setFirstName("Firstname");
        user.setLastName("Lastname");
        user.setEmail("test@test.com");
        user.setPassword("Password-1");
        User testUser = entityManager.persist(user);
        ToDo toDo = new ToDo();
        toDo.setTitle("TestToDo");
        toDo.setCreatedAt(LocalDateTime.now());
        toDo.setOwner(testUser);
        ToDo testToDo = entityManager.persist(toDo);
        testUser.setMyToDo(Arrays.asList(testToDo));
        List<ToDo> expected = testUser.getMyToDo();
        List<ToDo> actual = toDoRepository.findAllByOwnerId(testUser.getId());
        entityManager.flush();

        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(new ArrayList<>(), toDoRepository.findAllByOwnerId(100L));
    }
}