package com.binhbkfx02295.cshelpdesk.facebookgraphapi.service;

import com.binhbkfx02295.cshelpdesk.facebookgraphapi.config.FacebookAPIProperties;
import com.binhbkfx02295.cshelpdesk.facebookgraphapi.dto.FacebookTokenResponseDTO;
import com.binhbkfx02295.cshelpdesk.facebookgraphapi.dto.FacebookUserProfileDTO;
import com.binhbkfx02295.cshelpdesk.facebookgraphapi.entity.FacebookToken;
import com.binhbkfx02295.cshelpdesk.facebookgraphapi.repository.FacebookTokenRepository;
import com.binhbkfx02295.cshelpdesk.facebookuser.dto.FacebookUserFetchDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FacebookGraphAPIServiceImpl implements FacebookGraphAPIService {

    private final FacebookTokenRepository tokenRepository;
    private final FacebookAPIProperties facebookApiProperties;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public FacebookToken saveShortLivedToken(String shortLivedToken) {
        String url = String.format("https://graph.facebook.com/oauth/access_token"
                        + "?grant_type=fb_exchange_token"
                        + "&client_id=%s"
                        + "&client_secret=%s"
                        + "&fb_exchange_token=%s",
                facebookApiProperties.getAppId(),
                facebookApiProperties.getAppSecret(),
                shortLivedToken);

        FacebookTokenResponseDTO response = restTemplate.getForObject(url, FacebookTokenResponseDTO.class);

        if (response == null || response.getAccessToken() == null) {
            log.error("❌ Không thể lấy access token từ Facebook!");
            throw new IllegalStateException("Không thể lấy access token từ Facebook");
        }

        FacebookToken token = new FacebookToken();
        token.setPageId(facebookApiProperties.getPageId());
        token.setLongLivedAccessToken(response.getAccessToken());
        token.setLastUpdated(LocalDateTime.now());
        token.setExpiresAt(LocalDateTime.now().plusSeconds(response.getExpiresIn()));
        return tokenRepository.save(token);
    }

    @Override
    public String getValidAccessToken() {
        FacebookToken token;
        Optional<FacebookToken> optionalToken =
                tokenRepository.findFirstByPageIdOrderByLastUpdatedDesc(facebookApiProperties.getPageId());

        if (optionalToken.isPresent()) {
            token = optionalToken.get();
        } else {
            token = new FacebookToken();
            token.setPageId(facebookApiProperties.getPageId());
            token.setLongLivedAccessToken(facebookApiProperties.getDefaultShortToken()); // dùng token cấu hình
            token.setExpiresAt(LocalDateTime.now().plusDays(60)); // mặc định 60 ngày kể từ giờ
            token.setLastUpdated(LocalDateTime.now());
        }

        // Kiểm tra hạn token
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = token.getExpiresAt();

        if (expiresAt.isBefore(now) || expiresAt.isEqual(now)) {
            return saveShortLivedToken(token.getLongLivedAccessToken()).getLongLivedAccessToken();
        } else if (Duration.between(now, expiresAt).toDays() <= 3) {
            return saveShortLivedToken(token.getLongLivedAccessToken()).getLongLivedAccessToken();
        } else {
            return token.getLongLivedAccessToken();
        }
    }

    @Override
    public FacebookUserProfileDTO getUserProfile(String userId) {

        String token = getValidAccessToken();
        String url = buildProfileUrl(userId, token);

        try {
            return restTemplate.getForObject(url, FacebookUserProfileDTO.class);
        } catch (Exception e) {
            if (e.getMessage().contains("code\":190")) {
                String newToken = saveShortLivedToken(token).getLongLivedAccessToken();
                String retryUrl = buildProfileUrl(userId, newToken);
                return restTemplate.getForObject(retryUrl, FacebookUserProfileDTO.class);
            }
            FacebookUserProfileDTO facebookUserProfileDTO = new FacebookUserProfileDTO();
            facebookUserProfileDTO.setId(userId);
            FacebookUserProfileDTO.Picture picture = new FacebookUserProfileDTO.Picture();
            FacebookUserProfileDTO.Picture.Data data = new FacebookUserProfileDTO.Picture.Data();
            data.setUrl("/img/profile-placeholder.jpg");
            picture.setData(data);
            facebookUserProfileDTO.setPicture(picture);
            return facebookUserProfileDTO;
        }
    }

    @Override
    public void notifyNoAssignee(String senderId) {
        String token = getValidAccessToken();
        String url = String.format("https://graph.facebook.com/v19.0/me/messages?access_token=%s", token);

        // 1. Tạo HashMap cho recipient và message
        Map<String, Object> payload = new HashMap<>();
        Map<String, String> recipient = new HashMap<>();
        recipient.put("id", senderId);
        Map<String, String> message = new HashMap<>();
        message.put("text", "Hi, hiện chưa có nhân viên hỗ trợ ngay lúc này. Chúng tôi sẽ liên hệ bạn sớm nhất có thể. Xin cảm ơn!");
        payload.put("recipient", recipient);
        payload.put("message", message);

        try {
            // 2. Chuyển HashMap thành JSON string
            String jsonPayload = objectMapper.writeValueAsString(payload);

            // 3. Gửi POST request
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);

            String response = restTemplate.postForObject(url, request, String.class);
        } catch (Exception e) {
            log.error("❌ Gửi thông báo cho khách hàng thất bại", e);
        }
    }

    private String buildProfileUrl(String userId, String token) {
        return String.format("https://graph.facebook.com/%s?fields=id,first_name,last_name,picture,email&access_token=%s",
                userId, token);
    }
}
