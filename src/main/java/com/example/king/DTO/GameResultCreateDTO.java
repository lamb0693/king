package com.example.king.DTO;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Valid
public class GameResultCreateDTO {

    private String game_kind;

    private String winner_id;

    private String looser_id;

    @Override
    public String toString() {
        return "GameResultCreateDTO{" +
                "game_kind='" + game_kind + '\'' +
                ", winner_id='" + winner_id + '\'' +
                ", looser_id='" + looser_id + '\'' +
                '}';
    }
}
