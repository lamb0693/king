package com.example.king.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RankingDTO {
    private String nickname;

    private long winCount;

    @Override
    public String toString() {
        return "RankingDTO{" +
                "nickname='" + nickname + '\'' +
                ", winCount=" + winCount +
                '}';
    }
}
