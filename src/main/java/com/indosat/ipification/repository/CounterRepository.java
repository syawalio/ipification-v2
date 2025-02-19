package com.indosat.ipification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.indosat.ipification.entity.CounterEntity;

@Repository
public interface CounterRepository extends JpaRepository<CounterEntity, String> {

}

