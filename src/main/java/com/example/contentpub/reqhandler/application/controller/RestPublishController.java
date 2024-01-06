package com.example.contentpub.reqhandler.application.controller;

import com.example.contentpub.reqhandler.application.dto.request.ContentCreateRequest;
import com.example.contentpub.reqhandler.application.dto.request.ContentEditRequest;
import com.example.contentpub.reqhandler.application.dto.request.CreatePublisherRequest;
import com.example.contentpub.reqhandler.application.dto.response.CommonResponse;
import com.example.contentpub.reqhandler.domain.dto.CommonResponseEntity;
import com.example.contentpub.reqhandler.domain.dto.PublishRequestEntity;
import com.example.contentpub.reqhandler.domain.dto.RegPublisherRequestEntity;
import com.example.contentpub.reqhandler.domain.exception.DomainException;
import com.example.contentpub.reqhandler.domain.service.auth.impl.TokenUtilService;
import com.example.contentpub.reqhandler.domain.service.publish.PublishService;
import net.minidev.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.example.contentpub.reqhandler.domain.constants.AuthConstants.AUTHORIZATION;

/**
 * The RestPublishController class contains create, update and delete methods related endpoints.
 */
@RestController
@RequestMapping("/publisher")
@CrossOrigin(origins = "http://localhost:3000")
public class RestPublishController extends BaseController {

    private final PublishService publishService;
    private final TokenUtilService tokenUtilService;

    public RestPublishController(PublishService publishService, TokenUtilService tokenUtilService) {
        this.publishService = publishService;
        this.tokenUtilService = tokenUtilService;
    }


    /**
     * Register a READER user as a WRITER user. This endpoint is only allowed for READER users.
     * @param createPublisherRequest the request body that contain details required to be registered as a writer.
     * @param authHeader the Authorization header which is used to extract user-name.
     * @return the response indicating the overall status.
     */
    @PreAuthorize("hasAnyAuthority({'USER_READER'})")
    @PostMapping("/register")
    public ResponseEntity<CommonResponse<JSONObject>> createPublisher(@RequestBody @Valid CreatePublisherRequest createPublisherRequest,
                                                                      @RequestHeader(name = AUTHORIZATION) String authHeader) throws DomainException {

        Integer userId = tokenUtilService.getUserIdFromAccessToken(authHeader.substring(7));

        RegPublisherRequestEntity requestEntity = RegPublisherRequestEntity.builder()
                .userId(userId)
                .name(createPublisherRequest.getName())
                .description(createPublisherRequest.getDescription())
                .countryId(createPublisherRequest.getCountryId())
                .build();

        CommonResponseEntity<JSONObject> domainResponse = publishService.createPublisher(requestEntity);

        return ResponseEntity.status(domainResponse.getHttpStatusCode())
                .body(CommonResponse.<JSONObject>builder()
                        .code(domainResponse.getCode())
                        .description(domainResponse.getDescription())
                        .data(domainResponse.getData())
                        .build());

    }

    /**
     * Create new content. Only allowed for WRITER users.
     * @param contentCreateRequest the request body containing details of the new article.
     * @param authHeader the Authorization header which is used to extract user-name.
     * @return the response indicating overall status.
     */
    @PreAuthorize("hasAnyAuthority({'USER_WRITER'})")
    @PostMapping("/content")
    public ResponseEntity<CommonResponse<JSONObject>> publishContent(@RequestBody @Valid ContentCreateRequest contentCreateRequest,
                                                     @RequestHeader(name = AUTHORIZATION) String authHeader) throws DomainException {

        Integer userId = tokenUtilService.getUserIdFromAccessToken(authHeader.substring(7));

        PublishRequestEntity requestEntity = PublishRequestEntity.builder()
                .title(contentCreateRequest.getTitle())
                .summary(contentCreateRequest.getSummary())
                .details(contentCreateRequest.getDetails())
                .userId(userId)
                .categoryId(contentCreateRequest.getCategoryId())
                .build();

        CommonResponseEntity<JSONObject> domainResponse = publishService.publishContent(requestEntity);

        return ResponseEntity.status(domainResponse.getHttpStatusCode())
                .body(CommonResponse.<JSONObject>builder()
                        .code(domainResponse.getCode())
                        .description(domainResponse.getDescription())
                        .data(domainResponse.getData())
                        .build());

    }

    /**
     * Update an existing content. Only allowed for WRITER users.
     * @param contentId the ID of the content to be edited as a URL path variable.
     * @param contentEditRequest the request body containing the fields to be updated.
     * @param authHeader the Authorization header which is used to extract user-name.
     * @return the response indicating overall status.
     */
    @PreAuthorize("hasAnyAuthority({'USER_WRITER'})")
    @PutMapping("/content/{id}")
    public ResponseEntity<CommonResponse<JSONObject>> updateContent(@PathVariable("id") Integer contentId,
                                                    @RequestBody @Valid ContentEditRequest contentEditRequest,
                                                    @RequestHeader(name = AUTHORIZATION) String authHeader) throws DomainException {

        Integer userId = tokenUtilService.getUserIdFromAccessToken(authHeader.substring(7)); // Fetch user ID.

        PublishRequestEntity requestEntity = PublishRequestEntity.builder()
                .contentId(contentId)
                .title(contentEditRequest.getTitle())
                .summary(contentEditRequest.getSummary())
                .details(contentEditRequest.getDetails())
                .userId(userId)
                .build();

        CommonResponseEntity<JSONObject> domainResponse = publishService.updateContent(requestEntity);

        return ResponseEntity.status(domainResponse.getHttpStatusCode())
                .body(CommonResponse.<JSONObject>builder()
                        .code(domainResponse.getCode())
                        .description(domainResponse.getDescription())
                        .data(domainResponse.getData())
                        .build());
    }

    /**
     * Delete an existing content. Only allowed for WRITER users.
     * @param contentId the ID of the content to be edited as a URL path variable.
     * @param authHeader the Authorization header which is used to extract user-name.
     * @return the response indicating overall status.
     */
    @PreAuthorize("hasAnyAuthority({'USER_WRITER'})")
    @DeleteMapping("/content/{id}")
    public ResponseEntity<CommonResponse<JSONObject>> deleteContent(@PathVariable("id") Integer contentId,
                                                    @RequestHeader(name = AUTHORIZATION) String authHeader) throws DomainException {

        Integer userId = tokenUtilService.getUserIdFromAccessToken(authHeader.substring(7));

        PublishRequestEntity requestEntity = PublishRequestEntity.builder()
                .contentId(contentId)
                .userId(userId)
                .build();

        CommonResponseEntity<JSONObject> domainResponse = publishService.deleteContent(requestEntity);

        return ResponseEntity.status(domainResponse.getHttpStatusCode())
                .body(CommonResponse.<JSONObject>builder()
                        .code(domainResponse.getCode())
                        .description(domainResponse.getDescription())
                        .data(domainResponse.getData())
                        .build());
    }

}
