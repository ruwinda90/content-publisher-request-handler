package com.example.contentpub.reqhandler.application.controller;

import com.example.contentpub.reqhandler.application.dto.response.CommonResponse;
import com.example.contentpub.reqhandler.domain.dto.CommonResponseEntity;
import com.example.contentpub.reqhandler.domain.dto.ViewRequestEntity;
import com.example.contentpub.reqhandler.domain.exception.DomainException;
import com.example.contentpub.reqhandler.domain.service.view.ViewService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * The RestViewController class contain read related API endpoints.
 */
@RestController
@RequestMapping("/view")
public class RestViewController extends BaseController {

    @Autowired
    private ViewService viewService;

    /**
     * Get a list of content published to the platform. Does not contain the entire article in the response. Allowed for
     * both READERS and WRITERS. Supports pagination.
     * @param categoryId the category ID as a request parameter.
     * @param page the page (optional).
     * @param pageSize the pageSize (optional). Large pageSizes are blocked.
     * @return the list of articles.
     */
    @PreAuthorize("hasAnyAuthority({'USER_READER', 'USER_WRITER'})")
    @GetMapping("/content")
    public ResponseEntity<CommonResponse<JSONObject>> getContentList(@RequestParam(name = "categoryId") Integer categoryId,
                                                                     @RequestParam(name = "page", required = false) Integer page,
                                                                     @RequestParam(name = "pageSize", required = false) Integer pageSize) throws DomainException {

        ViewRequestEntity requestEntity = ViewRequestEntity.builder()
                .categoryId(categoryId)
                .page(page)
                .pageSize(pageSize)
                .build();

        CommonResponseEntity<JSONObject> domainResponse = viewService.getContentList(requestEntity);

        return ResponseEntity.status(domainResponse.getHttpStatusCode())
                .body(CommonResponse.<JSONObject>builder()
                        .code(domainResponse.getCode())
                        .description(domainResponse.getDescription())
                                                          .data(domainResponse.getData())
                                                        .build());
    }

    /**
     * Fetch all the details about single article.
     * @param contentId the ID of the article as a URL path variable.
     * @return the article details.
     */
    @PreAuthorize("hasAnyAuthority({'USER_READER', 'USER_WRITER'})")
    @GetMapping("/content/{id}")
    public ResponseEntity<CommonResponse<JSONObject>> getSingleContentItem(@PathVariable(name = "id") Integer contentId) throws DomainException  {

        ViewRequestEntity requestEntity = ViewRequestEntity.builder().contentId(contentId).build();

        CommonResponseEntity<JSONObject> domainResponse = viewService.getSingleContentItem(requestEntity);

        return ResponseEntity.status(domainResponse.getHttpStatusCode())
                .body(CommonResponse.<JSONObject>builder()
                        .code(domainResponse.getCode())
                        .description(domainResponse.getDescription())
                        .data(domainResponse.getData())
                        .build());
    }

    @PreAuthorize("hasAnyAuthority({'USER_READER', 'USER_WRITER'})")
    @GetMapping("/category")
    public ResponseEntity<CommonResponse<JSONObject>> getCategoryList() throws DomainException {

        CommonResponseEntity<JSONObject> domainResponse = viewService.getCategoryList();

        return ResponseEntity.status(domainResponse.getHttpStatusCode())
                .body(CommonResponse.<JSONObject>builder()
                        .code(domainResponse.getCode())
                        .description(domainResponse.getDescription())
                        .data(domainResponse.getData())
                        .build());
    }

}
