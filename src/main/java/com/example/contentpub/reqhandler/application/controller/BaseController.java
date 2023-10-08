package com.example.contentpub.reqhandler.application.controller;

import com.example.contentpub.reqhandler.application.dto.response.CommonResponse;
import com.example.contentpub.reqhandler.domain.constants.StatusCodes;
import com.example.contentpub.reqhandler.domain.exception.DomainException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

/**
 * The BaseController class is the parent of all the controller classes. Contains methods to handle various exceptions
 * and generate failure messages.
 */
public class BaseController {

    /**
     * The MethodArgumentNotValidException handler. MethodArgumentNotValidException gets thrown when the request params
     * are not valid.
     *
     * @param exception the exception.
     * @return the error response.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse<String>> handleMethodArgumentNotValidExp(MethodArgumentNotValidException exception) {

        String message = exception.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList()).get(0);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new CommonResponse(null, message, null));
    }

    /**
     * The HttpMessageNotReadableException handler.
     *
     * @param exception the exception.
     * @return the error response.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CommonResponse<String>> handleHttpMessageNotReadableExp(HttpMessageNotReadableException exception) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new CommonResponse(null, "Could not read request body properly", null));
    }

    /**
     * The MissingServletRequestParameterException handler. MissingServletRequestParameterException gets thrown when
     * there are missing request parameters.
     *
     * @param exception the exception.
     * @return the error response.
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<CommonResponse<String>> handleMissingParameterExp(MissingServletRequestParameterException exception) {

        String message = exception.getMessage();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new CommonResponse(null, message, null));
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<CommonResponse<String>> handleDomainExp(DomainException exception) {

        return ResponseEntity
                .status(exception.getHttpStatus())
                .body(new CommonResponse(exception.getCode(), exception.getMessage(), null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse<String>> handleCommonExp(Exception exception) {

        return ResponseEntity
                .status(StatusCodes.INTERNAL_ERROR.getHttpStatus())
                .body(new CommonResponse(StatusCodes.INTERNAL_ERROR.getCode(), exception.getMessage(), null));
    }

}
