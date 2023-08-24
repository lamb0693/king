package com.example.king.Entity;

import com.example.king.constant.GameKind;
import com.example.king.constant.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NonNull;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Table(name="results")
@Entity
@Data
public class ResultEntity {
    @Id
    @Column(length = 30)
    private String game_id;

    @Column(length=10, nullable = false)
    @Enumerated(EnumType.STRING)
    private GameKind gameKind;

    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity player0;

    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity player1;

    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity winner;

    @Column(updatable = false)
    @CreationTimestamp()
    private LocalDateTime gamedate = LocalDateTime.now();
}
