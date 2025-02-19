package com.indosat.ipification.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class IResponse implements Serializable {

    private String access_token;

    private String expires_in;

    private String refresh_expires_in;

    private String refresh_token;

    private String token_type;

    private String id_token;

    private String not_before_policy;

    private String session_state;

    private String scope;
}
