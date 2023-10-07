package com.example.contentpub.reqhandler.application.controller;

import com.example.contentpub.reqhandler.application.dto.response.CommonResponse;
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
                .body(new CommonResponse(message, "Request parameters not valid"));
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
                .body(new CommonResponse("Invalid request", "Could not read request body properly"));
    }

    /**
     * The MissingServletRequestParameterException handler. MissingServletRequestParameterException gets thrown when
     * there missing request parameters.
     *
     * @param exception the exception.
     * @return the error response.
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<CommonResponse<String>> handleMissingParameterExp(MissingServletRequestParameterException exception) {

        String message = exception.getMessage();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new CommonResponse(message, "Request parameters not valid"));
    }

}
