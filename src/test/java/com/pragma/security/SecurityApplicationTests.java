package com.pragma.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "app.security.jwt.secret-base64=VGhpcy1pcy1hLXRlc3Qtc2VjcmV0LWtleS0zMi1ieXRlcy1sb25nISE=",
        "app.security.jwt.issuer=test-issuer",
        "app.security.jwt.expiration-seconds=900",
        "app.security.users.user.username=test-user",
        "app.security.users.user.password=TestPassword-User-2026!",
        "app.security.users.admin.username=test-admin",
        "app.security.users.admin.password=TestPassword-Admin-2026!"
})
@AutoConfigureMockMvc
class SecurityApplicationTests {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @Test
    void publicEndpointDoesNotRequireAuthentication() throws Exception {
        mockMvc.perform(get("/api/public/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    void securedEndpointRejectsAnonymousRequest() throws Exception {
        mockMvc.perform(get("/api/secured/hello"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void validUserTokenAllowsSecuredEndpointButNotAdminEndpoint() throws Exception {
        String token = login("test-user", "TestPassword-User-2026!");

        mockMvc.perform(get("/api/secured/hello")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user").value("test-user"));

        mockMvc.perform(get("/api/secured/admin")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminTokenAllowsAdminEndpoint() throws Exception {
        String token = login("test-admin", "TestPassword-Admin-2026!");

        mockMvc.perform(get("/api/secured/admin")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user").value("test-admin"));
    }

    @Test
    void invalidTokenIsRejected() throws Exception {
        mockMvc.perform(get("/api/secured/hello")
                        .header("Authorization", "Bearer invalid.jwt.token"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("invalid_token"));
    }

    @Test
    void invalidCredentialsAreRejected() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"test-user\",\"password\":\"wrong\"}"))
                .andExpect(status().isUnauthorized());
    }

    private String login(String username, String password) throws Exception {
        String body = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";
        String response = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JsonNode json = objectMapper.readTree(response);
        return json.get("accessToken").asText();
    }
}
