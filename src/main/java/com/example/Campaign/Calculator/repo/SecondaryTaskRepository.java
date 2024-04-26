package com.example.Campaign.Calculator.repo;

import com.example.Campaign.Calculator.models.MainTask;
import com.example.Campaign.Calculator.models.Match1;
import com.example.Campaign.Calculator.models.SecondaryTask;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SecondaryTaskRepository extends CrudRepository<SecondaryTask, Long> {
    List<SecondaryTask> findByMatch1(Match1 match1);

    @Query(value ="SELECT s.secondary_task_id FROM `secondary_task` s  " +
            "WHERE s.match1 = :match_id " +
            "AND s.player = :player_id ", nativeQuery = true)
    List<Long> findSecondaryTasksByPlayerAndMatch(@Param("match_id") Long match_id, @Param("player_id") Long player_id);
}
