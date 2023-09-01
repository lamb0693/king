package com.example.king.Entity;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Table(name="quizes")
@Entity
@Data
public class QuizEntity {

    @Id
    @Column(length = 10)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length=100, nullable = false)
    private String problem;

    @Column(length=100, nullable = false)
    private String sel1;

    @Column(length=100, nullable = false)
    private String sel2;

    @Column(length=100, nullable = false)
    private String sel3;

    @Column(length=100, nullable = false)
    private String sel4;

    @Column(nullable = false)
    private int answer;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime created_at = LocalDateTime.now();

}
