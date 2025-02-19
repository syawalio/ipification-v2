package com.indosat.ipification.entity;

import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "log_ipification")
public class LogEntity {

    @Id
    @Column(name = "uid")
    private UUID uid;

    @Column(name = "no_hp")
    private String noHp;

    @Column(name = "request")
    private String request;

    @Column(name = "response")
    private String response;

    @Column(name = "request_date")
    private Date requestDate;

    @Column(name = "response_date")
    private Date responseDate;

    @Column(name = "service")
    private String service;

    @Column(name = "rc")
    private String rc;

    @Column(name = "result")
    private Boolean result;

}
