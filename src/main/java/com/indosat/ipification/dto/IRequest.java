package com.indosat.ipification.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class IRequest implements Serializable {

    private String redirect_uri;

    private String code;

    private String nohp;
    
    private String state;

    private Boolean verified;

    private String message;
}

