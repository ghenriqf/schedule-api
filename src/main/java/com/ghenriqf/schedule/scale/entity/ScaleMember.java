package com.ghenriqf.schedule.scale.entity;

import com.ghenriqf.schedule.function.entity.Function;
import com.ghenriqf.schedule.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "scale_members")
public class ScaleMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "scale_id")
    private Scale scale;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToMany
    @JoinTable(
            name = "scale_member_functions",
            joinColumns = @JoinColumn(name = "scale_member_id"),
            inverseJoinColumns = @JoinColumn(name = "function_id")
    )
    private Set<Function> functions;
}
