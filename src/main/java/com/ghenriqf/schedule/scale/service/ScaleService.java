package com.ghenriqf.schedule.scale.service;

import com.ghenriqf.schedule.auth.context.CurrentUserProvider;
import com.ghenriqf.schedule.auth.entity.User;
import com.ghenriqf.schedule.common.exception.AccessDeniedException;
import com.ghenriqf.schedule.common.exception.ConflictException;
import com.ghenriqf.schedule.common.exception.ResourceNotFoundException;
import com.ghenriqf.schedule.function.entity.Function;
import com.ghenriqf.schedule.function.repository.FunctionRepository;
import com.ghenriqf.schedule.member.entity.Member;
import com.ghenriqf.schedule.member.repository.MemberRepository;
import com.ghenriqf.schedule.member.service.MemberService;
import com.ghenriqf.schedule.ministry.entity.MinistryRole;
import com.ghenriqf.schedule.ministry.repository.MinistryRepository;
import com.ghenriqf.schedule.music.entity.Music;
import com.ghenriqf.schedule.music.repository.MusicRepository;
import com.ghenriqf.schedule.scale.dto.request.ScaleMemberRequest;
import com.ghenriqf.schedule.scale.dto.request.ScaleRequest;
import com.ghenriqf.schedule.scale.dto.request.ScaleUpdateRequest;
import com.ghenriqf.schedule.scale.dto.response.ScaleResponse;
import com.ghenriqf.schedule.scale.dto.response.ScaleSummaryResponse;
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


    public ScaleSummaryResponse create (ScaleRequest scaleRequest, Long ministryId) {
        User currentUser = currentUserProvider.getCurrentUser();
        Member member = memberService.findByUserIdAndMinistryId(currentUser.getId(), ministryId);

        if (!(member.getRole().equals(MinistryRole.ADMIN))) {
            throw new AccessDeniedException("Only administrators can create a scale");
        }

        Scale scale = ScaleMapper.toEntity(
                scaleRequest,
                memberRepository.findByIdAndMinistryId(scaleRequest.ministerId(), ministryId)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Minister not found in this ministry with id: " + scaleRequest.ministerId())
                        ));

        scale.setMinistry(ministryRepository.findById(ministryId)
                .orElseThrow(() -> new ResourceNotFoundException("Ministry not found with id: " + ministryId)));

        Scale save = scaleRepository.save(scale);
        return ScaleMapper.toSummaryResponse(save);
    }

    @Transactional
    public ScaleResponse update (Long ministryId, Long scaleId, ScaleUpdateRequest scaleUpdateRequest) {
        User currentUser = currentUserProvider.getCurrentUser();
        Member member = memberService.findByUserIdAndMinistryId(currentUser.getId(), ministryId);

        if (!(member.getRole().equals(MinistryRole.ADMIN))) {
            throw new AccessDeniedException("Only administrators can update a scale");
        }

        Scale scale = scaleRepository.findByIdAndMinistryId(scaleId, ministryId)
                .orElseThrow(() -> new ResourceNotFoundException("Scale not found in this ministry"));

        if (scaleUpdateRequest.name() != null) {
            scale.setName(scaleUpdateRequest.name());
        }
        if (scaleUpdateRequest.description() != null) {
            scale.setDescription(scaleUpdateRequest.description());
        }
        if (scaleUpdateRequest.date() != null) {
            scale.setDate(scaleUpdateRequest.date());
        }
        if (scaleUpdateRequest.ministerId() != null) {
            scale.setMinister(memberRepository.findByIdAndMinistryId(scaleUpdateRequest.ministerId(), ministryId)
                    .orElseThrow(
                            () -> new ResourceNotFoundException(
                                    "Minister not found in this ministry with id: " + scaleUpdateRequest.ministerId()
                            )
                    )
            );
        }

        Scale save = scaleRepository.save(scale);
        return ScaleMapper.toResponse(save);
    }

    @Transactional
    public void delete (Long ministryId, Long scaleId) {
        User currentUser = currentUserProvider.getCurrentUser();
        Member member = memberService.findByUserIdAndMinistryId(currentUser.getId(), ministryId);

        if (!(member.getRole().equals(MinistryRole.ADMIN))) {
            throw new AccessDeniedException("Only administrators can delete a scale");
        }

        Scale scale = scaleRepository.findByIdAndMinistryId(scaleId, ministryId)
                .orElseThrow(() -> new ResourceNotFoundException("Scale not found in this ministry"));

        scaleRepository.delete(scale);
    }

    public ScaleResponse findById (Long ministryId, Long scaleId) {
        User currentUser = currentUserProvider.getCurrentUser();
        memberService.verifyIfUserIsMemberOfMinistry(currentUser.getId(), ministryId);

        return ScaleMapper
                .toResponse(
                        scaleRepository.findByIdAndMinistryId(scaleId, ministryId)
                                .orElseThrow(() -> new ResourceNotFoundException("Scale not found with id: " + scaleId)
                        )
                );
    }

    public List<ScaleSummaryResponse> listAllByAfterDate (Long ministryId, LocalDateTime date) {
        User currentUser = currentUserProvider.getCurrentUser();
        memberService.verifyIfUserIsMemberOfMinistry(currentUser.getId(), ministryId);

        LocalDateTime dateTime = (date != null) ? date : LocalDateTime.now();
        List<Scale> scales = scaleRepository.findUpcomingScales(ministryId, dateTime);

        return scales
                .stream()
                .map(ScaleMapper::toSummaryResponse)
                .toList();
    }

    @Transactional
    public ScaleResponse addMusic (Long musicId, Long scaleId) {
        Scale scale = scaleRepository.findById(scaleId)
                .orElseThrow(() -> new ResourceNotFoundException("Scale not found"));

        User currentUser = currentUserProvider.getCurrentUser();
        Member member = memberService.findByUserIdAndMinistryId(currentUser.getId(), scale.getMinistry().getId());

        if (!(scale.getMinister().getId().equals(member.getId()) || member.getRole().equals(MinistryRole.ADMIN))) {
            throw new AccessDeniedException("Only minister or administrator can add song");
        }

        Music music = musicRepository.findById(musicId)
                .orElseThrow(() -> new ResourceNotFoundException("Song not found with id: " + musicId));

        if (scale.getMusics().contains(music)) {
            throw new ConflictException("This song is already in the scale");
        }

        scale.getMusics().add(music);
        Scale save = scaleRepository.save(scale);

        return ScaleMapper.toResponse(save);
    }

    @Transactional
    public ScaleResponse removeMusic (Long scaleId, Long musicId) {
        Scale scale = scaleRepository.findById(scaleId)
                .orElseThrow(() -> new ResourceNotFoundException("Scale not found with id: " + scaleId));

        User currentUser = currentUserProvider.getCurrentUser();
        Member member = memberService.findByUserIdAndMinistryId(currentUser.getId(), scale.getMinistry().getId());

        if (!(scale.getMinister().getId().equals(member.getId()) || member.getRole().equals(MinistryRole.ADMIN))) {
            throw new AccessDeniedException("Only minister or administrator can remove song");
        }

        Music music = musicRepository.findById(musicId)
                .orElseThrow(() -> new ResourceNotFoundException("Song not found with id: " + musicId));

        scale.getMusics().remove(music);
        Scale save = scaleRepository.save(scale);

        return ScaleMapper.toResponse(save);
    }

    @Transactional
    public ScaleResponse addMember (Long scaleId, Long memberId, ScaleMemberRequest request) {
        Scale scale = scaleRepository.findById(scaleId)
                .orElseThrow(() -> new ResourceNotFoundException("Scale not found with id: " + scaleId));

        User currentUser = currentUserProvider.getCurrentUser();
        Member admin = memberService.findByUserIdAndMinistryId(currentUser.getId(), scale.getMinistry().getId());

        if (!admin.getRole().equals(MinistryRole.ADMIN)) {
            throw new AccessDeniedException("Only administrators can add member");
        }

        if (scaleMemberRepository.existsByScaleIdAndMemberId(scaleId, memberId)) {
            throw new ConflictException("The member is already scheduled for this event");
        }

        Member memberToBeScaled = memberRepository.findByIdAndMinistryId(memberId, scale.getMinistry().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Member not found in this ministry"));


        List<Function> functions = functionRepository.findAllById(request.functionIds());
        if (functions.size() != request.functionIds().size()) {
            throw new ResourceNotFoundException("One or more functions not found");
        }

        ScaleMember scaleMember = ScaleMember
                .builder()
                .scale(scale)
                .member(memberToBeScaled)
                .functions(new HashSet<>(functions))
                .build();

        scaleMemberRepository.save(scaleMember);


        if (scale.getMembers() == null) {
            scale.setMembers(new HashSet<>());
        }
        scale.getMembers().add(scaleMember);

        return ScaleMapper.toResponse(scale);
    }

    @Transactional
    public ScaleResponse removeMember (Long scaleId, Long memberId) {
        Scale scale = scaleRepository.findById(scaleId)
                .orElseThrow(() -> new ResourceNotFoundException("Scale not found with id: " + scaleId));

        User currentUser = currentUserProvider.getCurrentUser();
        Member admin = memberService.findByUserIdAndMinistryId(currentUser.getId(), scale.getMinistry().getId());

        if (!(admin.getRole().equals(MinistryRole.ADMIN))) {
            throw new AccessDeniedException("Only administrators can remove member");
        }

        ScaleMember scaleMember = scaleMemberRepository.findByScaleIdAndMemberId(scaleId, memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member is not scheduled in this scale"));

        scale.getMembers().remove(scaleMember);
        scaleMemberRepository.delete(scaleMember);
        Scale savedScale = scaleRepository.save(scale);

        return ScaleMapper.toResponse(savedScale);
    }

    public Long countByMinistryIdAndDateAfter (Long id, LocalDateTime date) {
        return scaleRepository.countByMinistryIdAndDateAfter(id, date);
    }
}

