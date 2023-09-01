package com.example.king.Repository;

import com.example.king.Entity.QuizEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QuizRepostory extends JpaRepository<QuizEntity, Long> {

    @Query(value= "select * from ( SELECT *, row_number()  over (order by id) as rownum from quizes ) as subquery where rownum = :randomNo", nativeQuery = true)
    QuizEntity getRandomQuiz(int randomNo);
}
