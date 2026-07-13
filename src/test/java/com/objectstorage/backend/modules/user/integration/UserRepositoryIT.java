package com.objectstorage.backend.modules.user.integration;

import com.objectstorage.backend.config.AbstractTest;
import com.objectstorage.backend.modules.user.model.AuthProvider;
import com.objectstorage.backend.modules.user.model.Role;
import com.objectstorage.backend.modules.user.model.User;
import com.objectstorage.backend.modules.user.model.UserStatus;
import com.objectstorage.backend.modules.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;



import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryIT extends AbstractTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldReturnUserWhenEmailExists() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setPassword("encodedPassword");
        user.setRole(Role.USER);
        user.setStatus(UserStatus.ACTIVE);
        user.setAuthProvider(AuthProvider.EMAIL);;
        userRepository.saveAndFlush(user);
        Optional<User> found = userRepository.findByEmail(user.getEmail());
        assertTrue(found.isPresent());
        assertEquals("test@example.com", found.get().getEmail());
    }

    @Test
    public void whenEmailDoesNotExist_thenReturnEmpty() {
        Optional<User> found = userRepository.findByEmail("test.email.com");
        assertTrue(found.isEmpty());

    }
}
