package com.example.contentpub.reqhandler.domain.service.view;

import com.example.contentpub.reqhandler.domain.dto.CommonResponseEntity2;
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

    @Value("${view.url}")
    private String viewUrlTemplate;

    @Autowired
    private RestTemplateUtil restTemplateUtil;

    /**
     * Prepare the REST API to call the internal microservice and fetch list of content.
     * @param requestEntity the domain request entity.
     * @return the domain response.
     */
    public CommonResponseEntity2<JSONObject> getContentList(ViewRequestEntity requestEntity) throws DomainException {

        Integer categoryId = requestEntity.getCategoryId();

        /* Set default values for the properties not specified by the user. */
        Integer page = (requestEntity.getPage() != null) ? requestEntity.getPage() : defaultPage;
        Integer pageSize = (requestEntity.getPageSize() != null) ? requestEntity.getPageSize() : defaultPageSize;

        UriComponentsBuilder queryContentListUrl = UriComponentsBuilder.fromUriString(viewUrlTemplate);
        queryContentListUrl.queryParam("categoryId", categoryId);
        queryContentListUrl.queryParam("page", page);
        queryContentListUrl.queryParam("pageSize", pageSize);

        return restTemplateUtil.getResponse2(queryContentListUrl.build().toString(), HttpMethod.GET, null);

    }

    /**
     * Prepare the REST API to call the internal microservice and fetch single item.
     * @param requestEntity the domain request entity.
     * @return the domain response.
     */
    public CommonResponseEntity2<JSONObject> getSingleContentItem(ViewRequestEntity requestEntity) throws DomainException {

        Integer contentId = requestEntity.getContentId();

        UriComponentsBuilder querySingleItemUrl = UriComponentsBuilder.fromUriString(viewUrlTemplate);
        querySingleItemUrl.path("/" + contentId);

        return restTemplateUtil.getResponse2(querySingleItemUrl.build().toString(), HttpMethod.GET, null);
    }
}
