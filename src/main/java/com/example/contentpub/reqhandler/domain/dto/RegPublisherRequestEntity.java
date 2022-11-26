package com.example.contentpub.reqhandler.domain.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegPublisherRequestEntity {

    private Integer userId;

    private String name;

    private String description;

    private Integer countryId;

}
