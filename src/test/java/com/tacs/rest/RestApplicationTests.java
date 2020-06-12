package com.tacs.rest;

import com.tacs.rest.apiCovid.Covid19_latestResponse;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class RestApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void endpoint_availability_brief()
            throws IOException {

        // Given
        HttpUriRequest request = new HttpGet("https://wuhan-coronavirus-api.laeyoung.endpoint.ainize.ai/jhu-edu/brief");

        // When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        // Then
        assertThat(httpResponse.getStatusLine().getStatusCode(), equalTo(HttpStatus.SC_OK));
    }

    @Test
    public void endpoint_responseJSON_brief()
            throws IOException {
        // Given
        String jsonMimeType = "application/json";
        HttpUriRequest request = new HttpGet("https://wuhan-coronavirus-api.laeyoung.endpoint.ainize.ai/jhu-edu/brief");

        // When
        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        // Then
        String mimeType = ContentType.getOrDefault(response.getEntity()).getMimeType();
        assertEquals(jsonMimeType, mimeType);
    }

/*    @Test
    public void
    endpoint_JSONisCorrect_brief()
            throws ClientProtocolException, IOException {

        // Given
        HttpUriRequest request = new HttpGet("https://wuhan-coronavirus-api.laeyoung.endpoint.ainize.ai/jhu-edu/brief");

        // When
        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        // Then
        Covid19_briefResponse resource = RetrieveUtil.retrieveResourceFromResponse(
                response, Covid19_briefResponse.class);

        assertThat(2707016, Matchers.is(resource.getConfirmed()));
    }*/

    @Test
    public void
    endpoint_JSONisCorrect_latest_Argentina_code()
            throws IOException {

        // Given
        HttpUriRequest request = new HttpGet("https://wuhan-coronavirus-api.laeyoung.endpoint.ainize.ai/jhu-edu/latest?iso2=AR");

        // When
        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        // Then
        List<Covid19_latestResponse> resource = RetrieveUtil.retrieveResourceFromResponseArray(response, Covid19_latestResponse.class);
        assertThat("AR", Matchers.is(resource.get(0).getCountrycode().getIso2()));
    }

}