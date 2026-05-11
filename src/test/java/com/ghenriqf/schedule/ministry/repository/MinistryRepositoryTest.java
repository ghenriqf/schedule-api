package com.ghenriqf.schedule.ministry.repository;

import com.ghenriqf.schedule.auth.entity.User;
import com.ghenriqf.schedule.auth.repository.UserRepository;
import com.ghenriqf.schedule.member.entity.Member;
import com.ghenriqf.schedule.member.repository.MemberRepository;
import com.ghenriqf.schedule.ministry.dto.response.MinistryResponse;
import com.ghenriqf.schedule.ministry.entity.Ministry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
class MinistryRepositoryTest {

    @Autowired
    private MinistryRepository ministryRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldReturnMinistriesWhenUserIsMember() {
        // given
        User user = new User();
        userRepository.save(user);

        Ministry ministry = new Ministry();
        ministry.setName("test");
        ministryRepository.save(ministry);

        Member member = new Member();
        member.setUser(user);
        member.setMinistry(ministry);
        memberRepository.save(member);

        // when
        List<MinistryResponse> result = ministryRepository.findMinistriesByUserId(user.getId());

        // then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().id()).isEqualTo(user.getId());
    }
}