package com.example.Campaign.Calculator.repo;

import com.example.Campaign.Calculator.models.Campaign;
import com.example.Campaign.Calculator.models.MainTask;
import com.example.Campaign.Calculator.models.Match1;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MainTaskRepository extends CrudRepository<MainTask, Long> {
    List<MainTask> findByMatch(Match1 match);
}
