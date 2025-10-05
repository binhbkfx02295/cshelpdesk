package com.binhbkfx02295.cshelpdesk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FacebookUserDetailDTO {
    private String facebookId;
    private String facebookName;
    private String facebookProfilePic;
    private String realName;
    private String email;
    private String phone;
    private String zalo;
    private Instant createdAt;
}
