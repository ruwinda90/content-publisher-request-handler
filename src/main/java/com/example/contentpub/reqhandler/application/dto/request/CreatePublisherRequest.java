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
public class CreatePublisherRequest {

    @NotNull(message = "Name cannot be null")
    @NotBlank(message = "Name cannot be empty")
    @Size(max = 255, message = "Name is too long")
    private String name;

    @NotNull(message = "Description cannot be null")
    @NotBlank(message = "Description cannot be empty")
    @Size(max = 255, message = "Description is too long")
    private String description;

    @NotNull(message = "CountryId is mandatory")
    private Integer countryId;

}
