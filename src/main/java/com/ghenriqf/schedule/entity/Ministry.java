package com.ghenriqf.schedule.entity;

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

    private String name;

    private String description;

    @OneToMany(mappedBy = "ministry")
    private Set<Scale> scales;

    @OneToMany
    private List<Music> repertory;

    @OneToMany(mappedBy = "ministry")
    private Set<Member> members;
}
