package com.ssafy.trip.model.service.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.trip.model.entity.Sido;
import com.ssafy.trip.model.repository.SidoRepository;
import com.ssafy.trip.util.data.ApiData;
import com.ssafy.trip.util.exception.MyException;
import com.ssafy.trip.util.exception.common.CityInvalidException;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
@Slf4j
public class TripApiServiceImpl implements TripApiService {

    private final String API_KEY;
    private final SidoRepository sidoRepository;

    public TripApiServiceImpl(@Value("${api.key}") String apiKey, SidoRepository sidoRepository) {
        API_KEY = apiKey;
        this.sidoRepository = sidoRepository;
    }

    public void getCityCode() throws MyException {
        HttpClient client = HttpClient.newBuilder().build();

        System.out.println(URI.create(ApiData.apiUrl.get("sido")  + "?serviceKey=" + URLEncoder.encode(API_KEY) + "&MobileOS=ETC&MobileApp=App&_type=json"));
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(ApiData.apiUrl.get("sido") + "?serviceKey=" + URLEncoder.encode(API_KEY) + "&MobileOS=ETC&MobileApp=App&_type=json"))
                .GET().build();

        HttpResponse<String> response;  // Response 받을 변수
        try {
            response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            log.error(e.getMessage());
            throw new CityInvalidException();
        }

        // 응답 코드 확인
        int statusCode = response.statusCode();
        if (statusCode != HttpStatus.OK.value()) throw new CityInvalidException();  // API 호출 실패

        ObjectMapper objectmapper = new ObjectMapper();
        try {
            //  불러오기
            JsonNode jsonNode = objectmapper.readTree(response.body()).get("response").get("body").get("items").get("item");
            System.out.println(jsonNode);
            JsonNode curJsonNode;
            for (int i = 0; i < jsonNode.size(); i++) {
                curJsonNode = jsonNode.get(i);
                sidoRepository.save(Sido.builder().sidoCode(curJsonNode.get("code").asInt()).sidoName(curJsonNode.get("name").asText()).build());
            }
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            throw new CityInvalidException();
        }
    }
}