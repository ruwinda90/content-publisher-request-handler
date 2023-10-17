package com.example.contentpub.reqhandler.domain.service.publish;

import com.example.contentpub.reqhandler.domain.dto.*;
import com.example.contentpub.reqhandler.domain.exception.DomainException;
import com.example.contentpub.reqhandler.external.util.rest.RestTemplateUtil;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * The PublishService class calls the internal microservices to obtain the required results.
 */
@Service
public class PublishService {

    @Value("${publish.create-publisher.url}")
    private String createPublisherUrlTemplate;

    @Value("${publish.publish-content.url}")
    private String publishContentUrlTemplate;

    @Autowired
    private RestTemplateUtil restTemplateUtil;

    /**
     * Prepare the REST API to call the internal microservice and create a new writer user.
     *
     * @param requestEntity the domain request entity.
     * @return the domain response.
     */
    public CommonResponseEntity<JSONObject> createPublisher(RegPublisherRequestEntity requestEntity) throws DomainException {

        UriComponentsBuilder createPublisherUrl = UriComponentsBuilder.fromUriString(createPublisherUrlTemplate);

        return restTemplateUtil.getResponse(createPublisherUrl.toUriString(), HttpMethod.POST, requestEntity);

    }

    /**
     * Prepare the REST API to call the internal microservice and create a new content.
     *
     * @param requestEntity the domain request entity.
     * @return the domain response.
     */
    public CommonResponseEntity<JSONObject> publishContent(PublishRequestEntity requestEntity) throws DomainException {

        CommonContentRequestBody requestBody = CommonContentRequestBody.builder()
                .title(requestEntity.getTitle())
                .summary(requestEntity.getSummary())
                .details(requestEntity.getDetails())
                .userId(requestEntity.getUserId())
                .categoryId(requestEntity.getCategoryId()).build();
        UriComponentsBuilder publishContentUrl = UriComponentsBuilder.fromUriString(publishContentUrlTemplate);

        return restTemplateUtil.getResponse(publishContentUrl.toUriString(), HttpMethod.POST, requestBody);
    }

    /**
     * Prepare the REST API to call the internal microservice and update an existing content.
     *
     * @param requestEntity the domain request entity.
     * @return the domain response.
     */
    public CommonResponseEntity<JSONObject> updateContent(PublishRequestEntity requestEntity) throws DomainException {

        CommonContentRequestBody requestBody = CommonContentRequestBody.builder()
                .title(requestEntity.getTitle())
                .summary(requestEntity.getSummary())
                .details(requestEntity.getDetails())
                .userId(requestEntity.getUserId()).build();
        UriComponentsBuilder publishContentUrl = UriComponentsBuilder.fromUriString(publishContentUrlTemplate);
        publishContentUrl.path("/" + requestEntity.getContentId());

        return restTemplateUtil.getResponse(publishContentUrl.build().toString(), HttpMethod.PUT, requestBody);
    }

    /**
     * Prepare the REST API to call the internal microservice and delete an existing content.
     *
     * @param requestEntity the domain request entity.
     * @return the domain response.
     */
    public CommonResponseEntity<JSONObject> deleteContent(PublishRequestEntity requestEntity) throws DomainException {

        UriComponentsBuilder publishContentUrl = UriComponentsBuilder.fromUriString(publishContentUrlTemplate);
        publishContentUrl.path("/" + requestEntity.getContentId());
        publishContentUrl.queryParam("userId", requestEntity.getUserId());

        return restTemplateUtil.getResponse(publishContentUrl.build().toString(), HttpMethod.DELETE, null);
    }

}
