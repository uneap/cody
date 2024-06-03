package com.cody.backend.storage.response;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
@Getter
@SuperBuilder
public class Response {
    private int statusCode;
    private String reason;
}
