package com.ghenriqf.schedule.scale.repository;

import com.ghenriqf.schedule.ministry.entity.Ministry;
import com.ghenriqf.schedule.ministry.repository.MinistryRepository;
import com.ghenriqf.schedule.scale.entity.Scale;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
class ScaleRepositoryTest {

    @Autowired
    private MinistryRepository ministryRepository;

    @Autowired
    private ScaleRepository scaleRepository;

    @Test
    void shouldReturnUpcomingScalesOrderedByDate() {
        // given
        Ministry ministry = new Ministry();
        ministry.setName("test");
        ministryRepository.save(ministry);

        Scale pastScale = new Scale();

        pastScale.setMinistry(ministry);
        pastScale.setName("pastScale");
        pastScale.setDate(LocalDateTime.now().minusDays(1));
        scaleRepository.save(pastScale);

        Scale futureScale = new Scale();
        futureScale.setName("futureScale");
        futureScale.setMinistry(ministry);
        futureScale.setDate(LocalDateTime.now().plusDays(1));
        scaleRepository.save(futureScale);

        // when
        List<Scale> result = scaleRepository.findUpcomingScales(
                ministry.getId(), LocalDateTime.now()
        );

        // then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst()).isEqualTo(futureScale);
    }

}