package com.greengram.greengram4.openapi;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.greengram.greengram4.common.OpenApiProperties;
import com.greengram.greengram4.openapi.model.ApartmentTransactionDetailDto;
import com.greengram.greengram4.openapi.model.ApartmentTransactionDetailVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenApiService {
    private final OpenApiProperties openApiProperties;

    public List<ApartmentTransactionDetailVo> getApartmentTransactionList
            (ApartmentTransactionDetailDto dto) throws Exception {
        DefaultUriBuilderFactory factory =
                new DefaultUriBuilderFactory((openApiProperties.getApartment().getBaseUrl()));
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);

        WebClient webClient = WebClient.builder()
                .filters(exchangeFilterFunctions -> exchangeFilterFunctions.add(logRequest()))
                //.clientConnector(new JettyClientHttpConnector(httpClient))
                .uriBuilderFactory(factory)
                .baseUrl(openApiProperties.getApartment().getBaseUrl())
                .build();

        String data = webClient.get().uri(uriBuilder -> {
                            UriBuilder ub = uriBuilder
                                    .path(openApiProperties.getApartment().getDataUrl())
                                    .queryParam("DEAL_YMD", dto.getDealYm())
                                    .queryParam("LAWD_CD", dto.getLawdCd())
                                    .queryParam("serviceKey", openApiProperties.getApartment().getServiceKey());
                            if (dto.getPageNo() > 0) {
                                ub.queryParam("pageNo", dto.getPageNo());
                            }
                            if (dto.getNumOfRows() > 0) {
                                ub.queryParam("numOfRows", dto.getNumOfRows());
                            }
                            return ub.build();
                        }
                ).retrieve()//통신시도
                .bodyToMono(String.class)//응답 String으로 받겠다 String data 는 xml문장이들어옴
                .block();

        log.info("data: {}", data);

        ObjectMapper om = new XmlMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JsonNode jsonNode = om.readTree(data);
        List<ApartmentTransactionDetailVo> list =
                om.convertValue(jsonNode.path("body").path("items"),
                        new TypeReference<List<ApartmentTransactionDetailVo>>() {});
        return list;
    }


    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            if (log.isDebugEnabled()) {
                StringBuilder sb = new StringBuilder("Request: \n");
                //append clientRequest method and url
                clientRequest
                        .headers()
                        .forEach((name, values) -> values.forEach(value -> log.info(value)));
                log.debug(sb.toString());
            }
            return Mono.just(clientRequest);
        });
    }

}








