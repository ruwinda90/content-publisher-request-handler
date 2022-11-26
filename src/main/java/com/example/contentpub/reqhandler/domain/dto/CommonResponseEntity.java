package com.example.contentpub.reqhandler.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minidev.json.JSONObject;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponseEntity {

    private Integer statusCode; // HTTP status code.

    private String description; // Optional description.

    private JSONObject responseBody; // Response from inner service.

}
