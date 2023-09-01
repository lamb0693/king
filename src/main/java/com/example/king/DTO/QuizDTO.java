package com.example.king.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizDTO {
    private String problem;
    private String sel1;
    private String sel2;
    private String sel3;
    private String sel4;
    private int answer;

    @Override
    public String toString() {
        return "GetQuizDTO{" +
                "problem='" + problem + '\'' +
                ", sel1='" + sel1 + '\'' +
                ", sel2='" + sel2 + '\'' +
                ", sel3='" + sel3 + '\'' +
                ", sel4='" + sel4 + '\'' +
                ", answer=" + answer +
                '}';
    }
}
