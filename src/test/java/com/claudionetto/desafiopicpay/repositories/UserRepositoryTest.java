package com.claudionetto.desafiopicpay.repositories;

import com.claudionetto.desafiopicpay.domain.user.User;
import com.claudionetto.desafiopicpay.domain.user.UserType;
import com.claudionetto.desafiopicpay.dto.UserCreateDTO;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    EntityManager entityManager;
    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("Should get User successfully from DB searching by a unique document")
    void findByDocument_ReturnUserOptional_WhenSuccessful() {

        String document = "12312312340";
        UserCreateDTO data = new UserCreateDTO("Claudio", "Teste", "teste@gmail.com", document,
                "123123", UserType.COMMON, BigDecimal.valueOf(50));

        User user = createUser(data);

        Optional<User> userFounded = userRepository.findByDocument(document);

        assertThat(userFounded).isPresent();
        assertThat(userFounded.get()).isNotNull();
        assertThat(userFounded.get().getFirstName()).isEqualTo(user.getFirstName());
    }

    @Test
    @DisplayName("Shouldn't get User from DB searching by a unique document when user not exist")
    void findByDocument_ReturnEmptyUserOptional_WhenUserIsNotFound() {

        String document = "12312312340";
        Optional<User> userFounded = userRepository.findByDocument(document);

        assertThat(userFounded).isEmpty();
    }

    @Test
    @DisplayName("Should get User successfully from DB searching by a unique email")
    void findByEmail_ReturnUserOptional_WhenSuccessful() {

        String email = "teste@gmail.com";
        UserCreateDTO data = new UserCreateDTO("Claudio", "Teste", email, "12312312340",
                "123123", UserType.COMMON, BigDecimal.valueOf(50));

        User user = createUser(data);

        Optional<User> userFounded = userRepository.findByEmail(email);

        assertThat(userFounded).isPresent();
        assertThat(userFounded.get()).isNotNull();
        assertThat(userFounded.get().getFirstName()).isEqualTo(user.getFirstName());
    }

    @Test
    @DisplayName("Shouldn't get User from DB searching by a unique email when user not exist")
    void findByEmail_ReturnEmptyUserOptional_WhenUserIsNotFound() {

        String email = "teste@gmail.com";
        Optional<User> userFounded = userRepository.findByEmail(email);

        assertThat(userFounded).isEmpty();
    }

    private User createUser(UserCreateDTO data) {
        User newUser = new User(data);
        this.entityManager.persist(newUser);
        return newUser;
    }

}