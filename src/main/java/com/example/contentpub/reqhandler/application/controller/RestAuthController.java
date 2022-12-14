package com.example.contentpub.reqhandler.application.controller;

import com.example.contentpub.reqhandler.application.dto.request.AuthRequest;
import com.example.contentpub.reqhandler.application.dto.request.UserRegRequest;
import com.example.contentpub.reqhandler.application.dto.response.AuthResponse;
import com.example.contentpub.reqhandler.application.dto.response.CommonResponse;
import com.example.contentpub.reqhandler.domain.dto.AuthRequestEntity;
import com.example.contentpub.reqhandler.domain.dto.AuthResponseEntity;
import com.example.contentpub.reqhandler.domain.service.auth.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * The RestAuthController contains authentication and authorization endpoints.
 */
@RestController
@RequestMapping("/auth")
public class RestAuthController extends BaseController {

    @Autowired
    private UserAuthService userAuthService;

    /**
     * Authenticates an already registered user. If the user credentials are valid, generates a valid JWT.
     * @param authRequest the request body that contains username and the password.
     * @return A valid JWT if the credentials are valid, otherwise empty response with 401 HTTP status code.
     */
    @PostMapping("/authenticate")
    public ResponseEntity<CommonResponse<AuthResponse>> authenticate(@RequestBody @Valid AuthRequest authRequest) {

        AuthRequestEntity requestEntity = AuthRequestEntity.builder().email(authRequest.getEmail())
                                            .password(authRequest.getPassword()).build();

        AuthResponseEntity domainResponse = userAuthService.createToken(requestEntity);

        return ResponseEntity.status(domainResponse.getStatusCode())
                .body(CommonResponse.<AuthResponse>builder()
                    .description(domainResponse.getStatus())
                        .response(new AuthResponse(domainResponse.getDescription())).build());
    }

    /**
     * The user registration endpoint. Creates a new user.
     * @param userRegRequest the request body.
     * @return a JSON object. The response parameter indicates whether the operation is success or failure.
     */
    @PostMapping("/register")
    public ResponseEntity<CommonResponse<String>> createUser(@RequestBody @Valid UserRegRequest userRegRequest){

        AuthRequestEntity requestEntity = AuthRequestEntity.builder().email(userRegRequest.getEmail())
                .password(userRegRequest.getPassword()).build();

        AuthResponseEntity domainResponse = userAuthService.createUser(requestEntity);

        return ResponseEntity.status(domainResponse.getStatusCode())
                .body(CommonResponse.<String>builder().response(domainResponse.getStatus())
                        .description(domainResponse.getDescription()).build());
    }

}
