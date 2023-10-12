package com.example.contentpub.reqhandler.application.controller;

import com.example.contentpub.reqhandler.application.dto.request.AuthRequest;
import com.example.contentpub.reqhandler.application.dto.request.UserRegRequest;
import com.example.contentpub.reqhandler.application.dto.response.AuthResponse;
import com.example.contentpub.reqhandler.application.dto.response.CommonResponse;
import com.example.contentpub.reqhandler.domain.dto.AuthRequestEntity;
import com.example.contentpub.reqhandler.domain.dto.AuthResponseEntity;
import com.example.contentpub.reqhandler.domain.dto.CommonResponseEntity2;
import com.example.contentpub.reqhandler.domain.exception.DomainException;
import com.example.contentpub.reqhandler.domain.service.auth.UserAuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * The RestAuthController contains authentication and authorization endpoints.
 */
@RestController
@RequestMapping("/auth")
public class RestAuthController extends BaseController {

    private final UserAuthService userAuthService;
    private final long cookieLifeSpan;

    public RestAuthController(UserAuthService userAuthService,
                              @Value("${cookie.refresh.life-span}") long cookieLifeSpan) {
        this.userAuthService = userAuthService;
        this.cookieLifeSpan = cookieLifeSpan;
    }

    /**
     * Authenticates an already registered user. If the user credentials are valid, generates a valid JWT.
     *
     * @param authRequest the request body that contains username and the password.
     * @return A valid JWT if the credentials are valid, otherwise empty response with 401 HTTP status code.
     */
    @PostMapping("/authenticate")
    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    public ResponseEntity<CommonResponse<AuthResponse>> authenticate(@RequestBody @Valid AuthRequest authRequest) throws DomainException {

        AuthRequestEntity requestEntity = AuthRequestEntity.builder().email(authRequest.getEmail())
                .password(authRequest.getPassword()).build();

        CommonResponseEntity2<AuthResponseEntity> domainResponse = userAuthService.loginUser(requestEntity);

        ResponseCookie cookie = ResponseCookie.from("refreshJwt",
                        domainResponse.getData().getRefreshToken()).path("/api/v1.0/auth/refresh")
                .maxAge(cookieLifeSpan).httpOnly(true).build();

        return ResponseEntity
                .status(domainResponse.getHttpStatusCode())
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(CommonResponse.<AuthResponse>builder()
                        .code(domainResponse.getCode())
                        .description(domainResponse.getDescription())
                        .data(new AuthResponse(domainResponse.getData().getAccessToken())).build());
    }

    /**
     * The user registration endpoint. Creates a new user.
     *
     * @param userRegRequest the request body.
     * @return a JSON object. The response parameter indicates whether the operation is success or failure.
     */
    @PostMapping("/register")
    public ResponseEntity<CommonResponse<String>> createUser(@RequestBody @Valid UserRegRequest userRegRequest) {

        AuthRequestEntity requestEntity = AuthRequestEntity.builder().email(userRegRequest.getEmail())
                .password(userRegRequest.getPassword()).build();

        CommonResponseEntity2<String> domainResponse = userAuthService.createUser(requestEntity);

        return ResponseEntity.status(domainResponse.getHttpStatusCode())
                .body(CommonResponse.<String>builder()
                        .code(domainResponse.getCode())
                        .description(domainResponse.getDescription()).build());
    }

    /**
     * The access token refresh endpoint. Generates a new access token.
     *
     * @param refreshTokenCookie the refresh token cookie.
     * @return a JSON object. The response parameter indicates whether the operation is success or failure.
     */
    @GetMapping("/refresh")
    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    public ResponseEntity<CommonResponse<AuthResponse>> refreshAccessToken(
            @CookieValue(name = "refreshJwt", required = false) String refreshTokenCookie) throws DomainException {

        CommonResponseEntity2<AuthResponseEntity> domainResponse = userAuthService.refresh(refreshTokenCookie);

        return ResponseEntity.status(domainResponse.getHttpStatusCode())
                .body(CommonResponse.<AuthResponse>builder()
                        .code(domainResponse.getCode())
                        .description(domainResponse.getDescription())
                        .data(new AuthResponse(domainResponse.getData().getAccessToken())).build());
    }
}
