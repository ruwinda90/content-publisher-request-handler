package com.example.contentpub.reqhandler.domain.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewRequestEntity {

    private Integer contentId;

    private Integer categoryId;

    private Integer page;

    private Integer pageSize;

}
