package com.quiz.quiz_backend.Repository;

import com.quiz.quiz_backend.Entity.ScoreStoredEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ScoreStoringRepo extends JpaRepository<ScoreStoredEntity,Integer>
{

    @Query(value = "select u.username , SUM(best.best_score) as total_score " +
            "from(select user_id,quiz_id,MAX(score) AS best_score from score_stored_entity group by user_id, quiz_id) as best " +
            "JOIN user_table u on u.id=best.user_id " +
            "GROUP BY best.user_id " +
            "ORDER BY total_score DESC " +
            "LIMIT 5",nativeQuery = true)
    List<Object[]> getTopPerformers();
}
