package com.ghenriqf.schedule.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "scales")
public class Scale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private LocalDateTime date;

    @ManyToMany
    @JoinTable(
            name = "scale_members",
            joinColumns = @JoinColumn(name = "scale_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id")
    )
    private Set<Member> participants;

    @ManyToMany
    @JoinTable(
            name = "scale_musics",
            joinColumns = @JoinColumn(name = "scale_id"),
            inverseJoinColumns = @JoinColumn(name = "music_id")
    )
    private List<Music> musics;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ministry_id", nullable = false)
    private Ministry ministry;
}
