package com.example.king.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyInfoDTO {
    int pingWinCount;
    int pingLoseCount;
    int pingGrade;

    int ddongWinCount;
    int ddongLoseCount;
    int ddongGrade;

    int quizWinCount;
    int quizLoseCount;
    int quizGrade;

    @Override
    public String toString() {
        return "MyInfoDTO{" +
                "pingWinCount=" + pingWinCount +
                ", pingLoseCount=" + pingLoseCount +
                ", pingGrande=" + pingGrade +
                ", ddongWinCount=" + ddongWinCount +
                ", ddongLoseCount=" + ddongLoseCount +
                ", ddongGrade=" + ddongGrade +
                ", quizWinCount=" + quizWinCount +
                ", quizLoseCount=" + quizLoseCount +
                ", quizGrade=" + quizGrade +
                '}';
    }
}
