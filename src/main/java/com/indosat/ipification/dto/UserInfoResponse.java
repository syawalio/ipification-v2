package com.indosat.ipification.dto;
import lombok.Data;
import java.io.Serializable;

@Data
public class UserInfoResponse implements Serializable {

    private String sub;

    private String login_hint;

    private String phone_number_verified;
}
