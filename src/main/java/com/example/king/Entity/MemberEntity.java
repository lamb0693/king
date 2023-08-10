package com.example.king.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Table(name="members")
@Entity
@Data
public class MemberEntity {
    @Id
    private String id;
    private String nickname;
    private String password;
    private Date joindate;
}
