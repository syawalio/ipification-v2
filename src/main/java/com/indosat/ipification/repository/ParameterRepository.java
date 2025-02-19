package com.indosat.ipification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.indosat.ipification.entity.ParameterEntity;

@Repository
public interface ParameterRepository extends JpaRepository<ParameterEntity,String>{
    
}

