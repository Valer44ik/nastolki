package com.example.Campaign.Calculator.repo;

import com.example.Campaign.Calculator.models.MainTask;
import com.example.Campaign.Calculator.models.Match1;
import com.example.Campaign.Calculator.models.SecondaryTask;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SecondaryTaskRepository extends CrudRepository<SecondaryTask, Long> {
    List<SecondaryTask> findByMatch1(Match1 match1);
}
