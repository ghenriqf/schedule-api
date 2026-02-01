package com.ghenriqf.schedule.music.entity;

import com.ghenriqf.schedule.ministry.entity.Ministry;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "musics")
public class Music {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(length = 100)
    private String artist;

    @Column(length = 10)
    private String tone;

    private String videoLink;

    private String chordSheetLink;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ministry_id", nullable = false)
    private Ministry ministry;
}
