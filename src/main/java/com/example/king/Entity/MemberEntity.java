package com.example.king.Entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import com.example.king.constant.Role;

import java.time.LocalDateTime;

@Table(name="members")
@Entity
@Data
public class MemberEntity {
    @Id
    @Column(length = 30)
    private String id;

    @Column(length=20, unique = true, nullable = false)
    private String nickname;

    @Column(length=256, nullable = false)
    private String password;

    @Column(length=10, nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(length = 6, nullable=false )
    private boolean locked=false;  //lock는 id로 사용불가 mysql 예약어인듯

    @Column(updatable = false)
    @CreationTimestamp()
    private LocalDateTime joindate = LocalDateTime.now();
}
