package com.example.contentpub.reqhandler.application.dto.request;

import com.example.contentpub.reqhandler.application.util.validation.NullOrNonEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContentEditRequest {

    @NullOrNonEmpty(message = "The new title cannot be empty")
    @Size(max = 255, message = "Title is too long")
    private String title;

    @NullOrNonEmpty(message = "The new summary cannot be empty")
    @Size(max = 255, message = "Summary is too long")
    private String summary;

    @NullOrNonEmpty(message = "The new details property cannot be empty")
    @Size(max = 20000, message = "Details is too long")
    private String details;

}
