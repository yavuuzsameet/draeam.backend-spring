package dev.xamet.dreamgamesstudy.repository;

import dev.xamet.dreamgamesstudy.entity.Team;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class TeamRepositoryTest {

    @Autowired
    private TeamRepository teamRepository;

    @Test
    void canFindATeamByName() {
        // given
        Team team = new Team(
                1,
                "Wrexham AFC",
                List.of(1, 2, 3, 4, 5)
        );
        teamRepository.save(team);

        // when
        Team foundTeam = teamRepository.findByName("Wrexham AFC");

        // then
        assertThat(foundTeam.getName()).isEqualTo("Wrexham AFC");
        assertThat(foundTeam.getUsers()).toString().equals(List.of(1, 2, 3, 4, 5).toString());
    }

    @Test
    void canReturnNullWhenGivenNameDoesNotExist() {
        // given
        String teamName = "Wrexham AFC";

        // when
        Team foundTeam = teamRepository.findByName(teamName);

        // then
        assertThat(foundTeam).isNull();
    }
}