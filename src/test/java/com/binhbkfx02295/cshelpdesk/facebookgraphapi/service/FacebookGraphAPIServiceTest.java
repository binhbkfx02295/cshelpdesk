package com.binhbkfx02295.cshelpdesk.facebookgraphapi.service;

import com.binhbkfx02295.cshelpdesk.facebookgraphapi.config.FacebookAPIProperties;
import com.binhbkfx02295.cshelpdesk.facebookgraphapi.dto.FacebookTokenResponseDTO;
import com.binhbkfx02295.cshelpdesk.facebookgraphapi.dto.FacebookUserProfileDTO;
import com.binhbkfx02295.cshelpdesk.facebookgraphapi.entity.FacebookToken;
import com.binhbkfx02295.cshelpdesk.facebookgraphapi.repository.FacebookTokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FacebookGraphAPIServiceTest {

    private static final String PAGE_ACCESS   = "PAGE_TOKEN_ABC";
    private static final String APP_ID        = "APP_ID";
    private static final String APP_SECRET    = "APP_SECRET";
    private static final String PAGE_ID       = "123456";
    private static final String CUSTOMER_ID   = "1111111111";

    @Mock RestTemplate            restTemplate;
    @Mock FacebookAPIProperties   properties;
    @Mock FacebookTokenRepository tokenRepo;
    @Mock ObjectMapper   objectMapper;
    @Mock MessageSource           msgSource;

    @InjectMocks FacebookGraphAPIServiceImpl service;

    @Test
    void getUserProfile_success() {
        FacebookUserProfileDTO profile = new FacebookUserProfileDTO();
        profile.setId(CUSTOMER_ID);
        profile.setFirstName("john");

        String url = String.format(
                "https://graph.facebook.com/%s?fields=id,first_name,last_name,picture,email&access_token=%s",
                CUSTOMER_ID, PAGE_ACCESS);

        when(properties.getDefaultShortToken()).thenReturn(PAGE_ACCESS);
        when(properties.getPageId()).thenReturn(PAGE_ID);
        when(restTemplate.getForObject(url, FacebookUserProfileDTO.class)).thenReturn(profile);

        FacebookUserProfileDTO dto = service.getUserProfile(CUSTOMER_ID);

        assertNotNull(dto);
        System.out.println(dto);
        assertEquals("john", dto.getFirstName());
    }

    @Test
    void getValidAccessToken_existingStillValid() {
        FacebookToken token = new FacebookToken();
        token.setLongLivedAccessToken("LONG_LIVED");
        ZoneId zone = ZoneId.systemDefault();
        Instant future = Instant.now().plus(60, ChronoUnit.DAYS);
        LocalDateTime dateTime = LocalDateTime.ofInstant(future, zone);
        token.setExpiresAt(dateTime);


        when(tokenRepo.findFirstByPageIdOrderByLastUpdatedDesc(any())).thenReturn(Optional.of(token));

        String result = service.getValidAccessToken();

        assertEquals("LONG_LIVED", result);
        verify(tokenRepo, never()).save(any());
        verify(restTemplate, never()).exchange(anyString(), any(), any(), any(Class.class));
    }

    @Test
    void getValidAccessToken_expired_refreshNew() {
        // old expired token
        FacebookToken old = new FacebookToken();
        old.setLongLivedAccessToken("EXPIRED");
        old.setExpiresAt(LocalDateTime.now().plusDays(2));

        when(properties.getPageId()).thenReturn(PAGE_ID);
        when(properties.getAppSecret()).thenReturn(APP_SECRET);
        when(tokenRepo.findFirstByPageIdOrderByLastUpdatedDesc(any())).thenReturn(Optional.of(old));

        // new token from FB
        FacebookTokenResponseDTO fbRes = new FacebookTokenResponseDTO();
        fbRes.setAccessToken("NEW_LONG_LIVED");
        fbRes.setTokenType("bearer");
        fbRes.setExpiresIn(60 * 60 * 24 * 60);           // 60 days

        when(restTemplate.getForObject(
                anyString(),
                eq(FacebookTokenResponseDTO.class)))
                .thenReturn(fbRes);
        FacebookToken saved = new FacebookToken();
        saved.setLongLivedAccessToken("NEW_LONG_LIVED");
        saved.setExpiresAt(LocalDateTime.now().plusDays(60));
        when(tokenRepo.save(any())).thenReturn(saved);

        // ---- Act
        String result = service.getValidAccessToken();

        // ---- Assert
        assertEquals("NEW_LONG_LIVED", result);
        verify(tokenRepo).save(argThat(t -> t.getLongLivedAccessToken().equals("NEW_LONG_LIVED")));
    }

    @Test
    void saveShortLivedToken_persistCorrectly() {
        FacebookTokenResponseDTO dto = new FacebookTokenResponseDTO();
        dto.setAccessToken("SHORT");
        dto.setTokenType("bearer");
        dto.setExpiresIn(3600); // 1h

        when(restTemplate.getForObject(
                "https://graph.facebook.com/oauth/access_token?grant_type=fb_exchange_token&client_id=null&client_secret=null&fb_exchange_token=SHORT",
                FacebookTokenResponseDTO.class)).thenReturn(dto);
        service.saveShortLivedToken("SHORT");

        verify(tokenRepo).save(argThat(t ->
                t.getLongLivedAccessToken().equals("SHORT")
                        && t.getExpiresAt().isAfter(LocalDateTime.now().plusSeconds(3590))
        ));
    }

    @Test
    void notifyNoAssignee_sendTemplateMessage() {
        when(restTemplate.postForObject(anyString(), any(), eq(String.class)))
                .thenReturn("");

        service.notifyNoAssignee(CUSTOMER_ID);

        verify(restTemplate).postForObject(
                contains("/me/messages?access_token="),
                any(HttpEntity.class),
                eq(String.class));
    }
}
