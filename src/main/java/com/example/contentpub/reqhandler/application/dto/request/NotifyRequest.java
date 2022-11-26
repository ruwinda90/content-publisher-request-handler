package com.example.contentpub.reqhandler.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minidev.json.JSONObject;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotifyRequest {

    private JSONObject data;

}
