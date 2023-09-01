package com.example.king.serviceImpl;

import com.example.king.DTO.QuizDTO;
import com.example.king.Entity.QuizEntity;
import com.example.king.Repository.QuizRepostory;
import com.example.king.service.QuizService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Log4j2
public class QuizServiceImpl implements QuizService {

    QuizRepostory quizRepostory;

    @Transactional
    public QuizDTO getOneQuiz(){
        long totalNoOfQuiz = quizRepostory.count();
        int randNo = (int) Math.ceil(Math.random() * totalNoOfQuiz);
        QuizEntity quizEntity = quizRepostory.getRandomQuiz(randNo);

        QuizDTO quizDTO = new QuizDTO();
        quizDTO.setProblem(quizEntity.getProblem());
        quizDTO.setSel1(quizEntity.getSel1());
        quizDTO.setSel2(quizEntity.getSel2());
        quizDTO.setSel3(quizEntity.getSel3());
        quizDTO.setSel4(quizEntity.getSel4());
        quizDTO.setAnswer(quizEntity.getAnswer());

        log.info("getQuiz@QuizServiceImpl : quizDTO => " + quizDTO.toString());

        return quizDTO;
    }
}
