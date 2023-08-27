package com.example.king.DTO;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Valid
public class GameResultListDTO {
    private Long game_id;

    private String game_kind;

    private LocalDateTime gamedate;

    private String winner_id;

    private String loser_id;
}
