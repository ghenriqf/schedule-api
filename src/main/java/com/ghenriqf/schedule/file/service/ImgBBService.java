package com.ghenriqf.schedule.file.service;

import com.ghenriqf.schedule.file.dto.ImgBBResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImgBBService {
    @Value("${imgbb.api.key}")
    private String apiKey;
    @Value("${imgbb.upload.url}")
    private String url;
    private final RestTemplate restTemplate;

    public String uploadToImgBB(MultipartFile file) {
        if (file.isEmpty()) throw new RuntimeException("Arquivo vazio");

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", file.getResource());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        String finalUrl = url + "?key=" + apiKey;

        try {
            ImgBBResponse response = restTemplate.postForObject(finalUrl, requestEntity, ImgBBResponse.class);

            if (response != null && response.data() != null) {
                return response.data().url();
            }
            throw new RuntimeException("Falha ao obter resposta do ImgBB");
        } catch (Exception e) {
            throw new RuntimeException("Erro na integração com ImgBB: " + e.getMessage());
        }
    }
}
