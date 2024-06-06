package com.cody.backend.storage.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Response {
    protected int statusCode;
    protected String reason;
    public Response(int statusCode, String reason) {
        this.statusCode = statusCode;
        this.reason = reason;
    }
}
