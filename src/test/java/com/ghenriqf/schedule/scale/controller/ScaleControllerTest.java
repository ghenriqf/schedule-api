package com.ghenriqf.schedule.scale.controller;

import com.ghenriqf.schedule.auth.entity.User;
import com.ghenriqf.schedule.auth.security.TokenService;
import com.ghenriqf.schedule.scale.dto.response.ScaleSummaryResponse;
import com.ghenriqf.schedule.scale.service.ScaleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ScaleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenService tokenService;

    @MockitoBean
    private ScaleService scaleService;

    private String token;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@email.com");
        token = tokenService.generateToken(user);
    }

    @Test
    void shouldCreateScaleAndReturn201() throws Exception {
        // given
        given(scaleService.create(any(), eq(1L)))
                .willReturn(new ScaleSummaryResponse(1L, "scale", "description", LocalDateTime.of(2027, 1, 1, 0, 0)));

        // when
        // then
        mockMvc.perform(post("/ministries/1/scales")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "name": "Escala",
                            "date": "2027-01-15 10:00"
                        }
                    """))
                .andExpect(status().isCreated());
    }
}