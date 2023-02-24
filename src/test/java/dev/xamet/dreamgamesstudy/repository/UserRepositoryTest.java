package dev.xamet.dreamgamesstudy.repository;

import dev.xamet.dreamgamesstudy.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class UserRepositoryTest {

    @Autowired private UserRepository userRepository;

    @Test
    void canFindAUserById() {
        // given
        User user = new User(1, 1, 0);
        userRepository.save(user);

        // when
        User foundUser = userRepository.findById(1).orElse(null);

        // then
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getId()).isEqualTo(1);
        assertThat(foundUser.getLevel()).isEqualTo(1);
        assertThat(foundUser.getCoins()).isEqualTo(0);
    }

}