package com.indosat.ipification.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Data
@Table(name = "counter")
public class CounterEntity {
    @Id
    @Column(name = "phone")
    private String phone;

    @Column(name = "date")
    private Date date;
    
    @Column(name = "count")
    private int count;
}
