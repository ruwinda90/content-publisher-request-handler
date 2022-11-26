package com.example.contentpub.reqhandler.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContentCreateRequest {

    @NotNull(message = "Title cannot be null")
    @NotBlank(message = "Title cannot be empty")
    @Size(max = 255, message = "Title is too long")
    private String title;

    @NotNull(message = "Summary cannot be null")
    @NotBlank(message = "Summary cannot be empty")
    @Size(max = 255, message = "Summary is too long")
    private String summary;

    @NotNull(message = "Details cannot be null")
    @NotBlank(message = "Details cannot be empty")
    @Size(max = 20000, message = "Details is too long")
    private String details;

    @NotNull(message = "CategoryId cannot be null")
    private Integer categoryId;

}
