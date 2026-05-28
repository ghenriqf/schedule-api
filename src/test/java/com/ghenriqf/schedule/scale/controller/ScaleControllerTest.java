package com.ghenriqf.schedule.scale.controller;

import com.ghenriqf.schedule.auth.entity.User;
import com.ghenriqf.schedule.auth.security.TokenService;
import com.ghenriqf.schedule.scale.dto.response.ScaleResponse;
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
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
        LocalDateTime date = LocalDateTime.of(2027, 1, 15, 10, 0);
        given(scaleService.create(any(), eq(1L)))
                .willReturn(new ScaleSummaryResponse(1L, "scale", "description", date));

        // when
        // then
        mockMvc.perform(post("/ministries/1/scales")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "name": "scale",
                            "date": "2027-01-15 10:00"
                        }
                    """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("scale"))
                .andExpect(jsonPath("$.description").value("description"));
    }

    @Test
    void shouldUpdateScaleAndReturn200 () throws Exception {
        // given
        LocalDateTime date = LocalDateTime.of(2027, 1, 15, 10, 0);
        ScaleResponse response = new ScaleResponse(1L, 1L, 1L, "scale", "description", date, Set.of(), List.of());

        given(scaleService.update(eq(1L), eq(1L), any())).willReturn(response);

        // when
        // then
        mockMvc.perform(patch("/ministries/1/scales/1")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "name": "scale",
                            "description": "description",
                            "date": "2027-01-15T10:00:00",
                            "ministerId": 1
                        }
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.ministryId").value(1))
                .andExpect(jsonPath("$.name").value("scale"))
                .andExpect(jsonPath("$.description").value("description"))
                .andExpect(jsonPath("$.members").isArray())
                .andExpect(jsonPath("$.musics").isArray());
    }
}