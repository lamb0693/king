package com.example.king.Entity;

import com.example.king.constant.GameKind;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Table(name="results")
@Entity
@Data
public class GameResultEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long game_id;

    @Column(length=10, nullable = false)
    @Enumerated(EnumType.STRING)
    private GameKind gameKind;

    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity winner;

    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity loser;

    @Column(updatable = false)
    @CreationTimestamp()
    private LocalDateTime gamedate = LocalDateTime.now();
}
