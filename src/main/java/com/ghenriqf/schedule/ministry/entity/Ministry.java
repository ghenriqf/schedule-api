package com.ghenriqf.schedule.ministry.entity;

import com.ghenriqf.schedule.member.entity.Member;
import com.ghenriqf.schedule.entity.Music;
import com.ghenriqf.schedule.entity.Scale;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "ministries")
public class Ministry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @OneToMany(mappedBy = "ministry")
    private Set<Scale> scales;

    @OneToMany(mappedBy = "ministry")
    private List<Music> repertory;

    @OneToMany(mappedBy = "ministry")
    private Set<Member> members;
}
