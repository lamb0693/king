package com.example.king.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jdk.jfr.Timestamp;
import lombok.Data;

import java.util.Date;

@Table(name="members")
@Entity
@Data
public class MemberEntity {
    @Id
    @Column(length = 20)
    private String id;

    @Column(length=20, unique = true, nullable = false)
    private String nickname;

    @Column(length=256, nullable = false)
    private String password;

    @Column
    @Timestamp
    private Date joindate;
}
