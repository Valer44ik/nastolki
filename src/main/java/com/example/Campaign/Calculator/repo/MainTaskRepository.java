package com.example.Campaign.Calculator.repo;

import com.example.Campaign.Calculator.models.Campaign;
import com.example.Campaign.Calculator.models.MainTask;
import com.example.Campaign.Calculator.models.Match1;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MainTaskRepository extends CrudRepository<MainTask, Long> {
    List<MainTask> findByMatch1(Match1 match);

    @Query(value ="SELECT m.main_task_id FROM `main_task` m " +
            "WHERE m.match1 = :match_id " +
            "AND m.player = :player_id ", nativeQuery = true)
    List<Long> findMainTasksByPlayerAndMatch(@Param("match_id") Long match_id, @Param("player_id") Long player_id);
}
