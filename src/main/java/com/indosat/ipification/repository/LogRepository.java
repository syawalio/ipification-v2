package com.indosat.ipification.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.indosat.ipification.entity.LogEntity;

@Repository
public interface LogRepository extends JpaRepository<LogEntity, UUID> {

}