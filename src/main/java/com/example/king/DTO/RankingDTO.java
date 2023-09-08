package com.example.king.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RankingDTO {
    private String nickname;

    private long winCount;

    private String gameName;

    private int rank;

    @Override
    public String toString() {
        return "RankingDTO{" +
                "nickname='" + nickname + '\'' +
                ", winCount=" + winCount +
                ", gameName='" + gameName + '\'' +
                ", rank=" + rank +
                '}';
    }
}
