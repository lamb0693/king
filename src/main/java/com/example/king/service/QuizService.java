package com.example.king.service;

import com.example.king.DTO.QuizDTO;
import com.example.king.Repository.QuizRepostory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

public interface QuizService {
    public QuizDTO getOneQuiz();

}
