package com.ghenriqf.schedule.scale.service;

import com.ghenriqf.schedule.auth.context.CurrentUserProvider;
import com.ghenriqf.schedule.auth.entity.User;
import com.ghenriqf.schedule.common.exception.AccessDeniedException;
import com.ghenriqf.schedule.common.exception.ResourceNotFoundException;
import com.ghenriqf.schedule.function.entity.Function;
import com.ghenriqf.schedule.function.repository.FunctionRepository;
import com.ghenriqf.schedule.member.dto.response.MemberResponse;
import com.ghenriqf.schedule.member.entity.Member;
import com.ghenriqf.schedule.member.mapper.MemberMapper;
import com.ghenriqf.schedule.member.repository.MemberRepository;
import com.ghenriqf.schedule.member.service.MemberService;
import com.ghenriqf.schedule.ministry.entity.MinistryRole;
import com.ghenriqf.schedule.ministry.repository.MinistryRepository;
import com.ghenriqf.schedule.music.dto.response.MusicResponse;
import com.ghenriqf.schedule.music.entity.Music;
import com.ghenriqf.schedule.music.mapper.MusicMapper;
import com.ghenriqf.schedule.music.repository.MusicRepository;
import com.ghenriqf.schedule.scale.dto.request.ScaleMemberRequest;
import com.ghenriqf.schedule.scale.dto.request.ScaleRequest;
import com.ghenriqf.schedule.scale.dto.response.ScaleMemberResponse;
import com.ghenriqf.schedule.scale.dto.response.ScaleResponse;
import com.ghenriqf.schedule.scale.entity.Scale;
import com.ghenriqf.schedule.scale.entity.ScaleMember;
import com.ghenriqf.schedule.scale.mapper.ScaleMapper;
import com.ghenriqf.schedule.scale.repository.ScaleMemberRepository;
import com.ghenriqf.schedule.scale.repository.ScaleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScaleService {

    private final ScaleRepository scaleRepository;
    private final CurrentUserProvider currentUserProvider;
    private final MemberService memberService;
    private final MinistryRepository ministryRepository;
    private final MusicRepository musicRepository;
    private final FunctionRepository functionRepository;
    private final MemberRepository memberRepository;
    private final ScaleMemberRepository scaleMemberRepository;


    public ScaleResponse create (ScaleRequest scaleRequest, Long ministryId) {
        User currentUser = currentUserProvider.getCurrentUser();
        Member member = memberService.findByUserIdAndMinistryId(currentUser.getId(), ministryId);

        if (!(member.getRole().equals(MinistryRole.ADMIN))) {
            throw new AccessDeniedException("Only administrators can create a scale");
        }

        Scale scale = ScaleMapper.toEntity(scaleRequest);

        scale.setMinistry(ministryRepository.findById(ministryId)
                .orElseThrow(() -> new ResourceNotFoundException("Ministry not found with id: " + ministryId)));

        Scale save = scaleRepository.save(scale);

        return ScaleMapper.toResponse(save);
    }

    public MusicResponse addMusic (Long musicId, Long scaleId) {
        Scale scale = scaleRepository.findById(scaleId)
                .orElseThrow(() -> new ResourceNotFoundException("Scale not found"));

        User currentUser = currentUserProvider.getCurrentUser();
        Member member = memberService.findByUserIdAndMinistryId(currentUser.getId(), scale.getMinistry().getId());

        if (!(scale.getLeader().getId().equals(member.getId()) || member.getRole().equals(MinistryRole.ADMIN))) {
            throw new AccessDeniedException("Only the leader can add song");
        }

        Music music = musicRepository.findById(musicId)
                .orElseThrow(() -> new ResourceNotFoundException("Song not found"));

        scale.getMusics().add(music);
        scaleRepository.save(scale);

        return MusicMapper.toResponse(music);
    }

    public MusicResponse removeMusic (Long musicId, Long scaleId) {
        Scale scale = scaleRepository.findById(scaleId)
                .orElseThrow(() -> new ResourceNotFoundException("Scale not found"));

        User currentUser = currentUserProvider.getCurrentUser();
        Member member = memberService.findByUserIdAndMinistryId(currentUser.getId(), scale.getMinistry().getId());

        if (!(scale.getLeader().getId().equals(member.getId()) || member.getRole().equals(MinistryRole.ADMIN))) {
            throw new AccessDeniedException("Only the leader can remove song");
        }

        Music music = musicRepository.findById(musicId)
                .orElseThrow(() -> new ResourceNotFoundException("Song not found"));

        scale.getMusics().remove(music);
        scaleRepository.save(scale);

        return MusicMapper.toResponse(music);
    }

    @Transactional
    public ScaleMemberResponse addMember (Long scaleId, Long memberId, ScaleMemberRequest request) {
        Scale scale = scaleRepository.findById(scaleId)
                .orElseThrow(() -> new ResourceNotFoundException("Scale not found with id: " + scaleId));

        User currentUser = currentUserProvider.getCurrentUser();
        Member admin = memberService.findByUserIdAndMinistryId(currentUser.getId(), scale.getMinistry().getId());

        if (!(admin.getRole().equals(MinistryRole.ADMIN))) {
            throw new AccessDeniedException("Only the leader can add member");
        }

        List<Function> functions = functionRepository.findAllById(request.functionIds());
        if (functions.size() != request.functionIds().size()) {
            throw new ResourceNotFoundException("One or more functions not found");
        }
        Member memberToBeScaled = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + memberId));

        ScaleMember scaleMember = ScaleMember
                .builder()
                .scale(scale)
                .member(memberToBeScaled)
                .functions(new HashSet<>(functions))
                .build();

        ScaleMember saved = scaleMemberRepository.save(scaleMember);

        return ScaleMemberResponse
                .builder()
                .id(saved.getId())
                .memberId(saved.getMember().getId())
                .memberName(saved.getMember().getUser().getName())
                .functions(
                        saved.getFunctions()
                        .stream()
                        .map(Function::getName)
                        .collect(Collectors.toSet()))
                .build();
    }

    public Long countByMinistryIdAndDateAfter (Long id, LocalDateTime date) {
        return scaleRepository.countByMinistryIdAndDateAfter(id, date);
    }
}

