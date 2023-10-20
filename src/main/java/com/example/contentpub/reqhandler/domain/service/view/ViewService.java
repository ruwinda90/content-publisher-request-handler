package com.example.contentpub.reqhandler.domain.service.view;

import com.example.contentpub.reqhandler.domain.dto.CommonResponseEntity;
import com.example.contentpub.reqhandler.domain.dto.ViewRequestEntity;
import com.example.contentpub.reqhandler.domain.exception.DomainException;
import com.example.contentpub.reqhandler.external.util.rest.RestTemplateUtil;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * The ViewService class calls the internal microservices to fetch required data.
 */
@Service
public class ViewService {

    @Value("${view.default-values.page}")
    private Integer defaultPage;

    @Value("${view.default-values.page-size}")
    private Integer defaultPageSize;

    @Value("${view.content.url}")
    private String viewContentUrlTemplate;

    @Value("${view.category.url}")
    private String viewCategoryUrlTemplate;

    @Autowired
    private RestTemplateUtil restTemplateUtil;

    /**
     * Prepare the REST API to call the internal microservice and fetch list of content.
     * @param requestEntity the domain request entity.
     * @return the domain response.
     */
    public CommonResponseEntity<JSONObject> getContentList(ViewRequestEntity requestEntity) throws DomainException {

        Integer categoryId = requestEntity.getCategoryId();

        /* Set default values for the properties not specified by the user. */
        Integer page = (requestEntity.getPage() != null) ? requestEntity.getPage() : defaultPage;
        Integer pageSize = (requestEntity.getPageSize() != null) ? requestEntity.getPageSize() : defaultPageSize;

        UriComponentsBuilder queryContentListUrl = UriComponentsBuilder.fromUriString(viewContentUrlTemplate);
        queryContentListUrl.queryParam("categoryId", categoryId);
        queryContentListUrl.queryParam("page", page);
        queryContentListUrl.queryParam("pageSize", pageSize);

        return restTemplateUtil.getResponse(queryContentListUrl.build().toString(), HttpMethod.GET, null);

    }

    /**
     * Prepare the REST API to call the internal microservice and fetch single item.
     * @param requestEntity the domain request entity.
     * @return the domain response.
     */
    public CommonResponseEntity<JSONObject> getSingleContentItem(ViewRequestEntity requestEntity) throws DomainException {

        Integer contentId = requestEntity.getContentId();

        UriComponentsBuilder querySingleItemUrl = UriComponentsBuilder.fromUriString(viewContentUrlTemplate);
        querySingleItemUrl.path("/" + contentId);

        return restTemplateUtil.getResponse(querySingleItemUrl.build().toString(), HttpMethod.GET, null);
    }

    public CommonResponseEntity<JSONObject> getCategoryList() throws DomainException {

        UriComponentsBuilder queryUrl = UriComponentsBuilder.fromUriString(viewCategoryUrlTemplate);

        return restTemplateUtil.getResponse(queryUrl.build().toString(), HttpMethod.GET, null);
    }
}
