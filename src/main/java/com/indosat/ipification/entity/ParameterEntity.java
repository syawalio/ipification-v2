package com.indosat.ipification.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Data
@Table(name = "parameter")
public class ParameterEntity {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "value")
    private String value;
}
