package com.eazybytes.eazyschool.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
//genrate constructor without any parameter
@NoArgsConstructor
public class Response {

    private String statusCode;
    private String statusMsg;

}
