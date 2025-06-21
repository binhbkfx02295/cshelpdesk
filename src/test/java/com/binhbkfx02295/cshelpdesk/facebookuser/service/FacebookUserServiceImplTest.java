package com.binhbkfx02295.cshelpdesk.facebookuser.service;

import com.binhbkfx02295.cshelpdesk.facebookgraphapi.dto.FacebookUserProfileDTO;
import com.binhbkfx02295.cshelpdesk.facebookuser.dto.FacebookUserDetailDTO;
import com.binhbkfx02295.cshelpdesk.facebookuser.dto.FacebookUserListDTO;
import com.binhbkfx02295.cshelpdesk.facebookuser.entity.FacebookUser;
import com.binhbkfx02295.cshelpdesk.facebookuser.mapper.FacebookUserMapper;
import com.binhbkfx02295.cshelpdesk.facebookuser.repository.FacebookUserRepository;
import com.binhbkfx02295.cshelpdesk.infrastructure.util.APIResultSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.text.html.Option;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FacebookUserServiceImplTest {
    @Mock FacebookUserRepository facebookUserRepository;
    @Mock FacebookUserMapper mapper;
    @InjectMocks  FacebookUserServiceImpl service;

    @Test
    void save_facebookId_invalid() {
        FacebookUserProfileDTO profileDTO = mockFacebookProfile();
        when(mapper.toEntity(profileDTO)).thenReturn(new FacebookUser());
        when(facebookUserRepository.save(any(FacebookUser.class)))
                .thenThrow(new RuntimeException("Lỗi hệ thống"));
        APIResultSet<FacebookUserDetailDTO> result = service.save(profileDTO);
        assertEquals(500, result.getHttpCode());
        assertEquals("Lỗi hệ thống", result.getMessage());
    }

    @Test
    void save_facebookId_success() {
        FacebookUserProfileDTO profileDTO = mockFacebookProfile();
        when(mapper.toEntity(profileDTO)).thenReturn(new FacebookUser());
        when(facebookUserRepository.save(any(FacebookUser.class))).thenReturn(new FacebookUser());
        APIResultSet<FacebookUserDetailDTO> result = service.save(profileDTO);
        assertEquals(200, result.getHttpCode());
        assertEquals("Khởi tạo người dùng Facebook thành công", result.getMessage());
    }

    @Test
    void testUpdate_internalError_failed() {
        // Arrange
        FacebookUserDetailDTO profileDTO = mockFacebookDetail();
        FacebookUser entity = new FacebookUser();
        when(mapper.toEntity(profileDTO)).thenReturn(entity);
        when(facebookUserRepository.update(any(FacebookUser.class)))
                .thenThrow(new RuntimeException());

        // Act
        APIResultSet<FacebookUserDetailDTO> result = service.update(profileDTO);

        // Assert
        assertEquals(500, result.getHttpCode());
        assertEquals("Lỗi hệ thống", result.getMessage());
    }

    @Test
    void testUpdate_facebookId_not_exists_failed() {
        // Arrange
        FacebookUserDetailDTO profileDTO = mockFacebookDetail();
        profileDTO.setFacebookId("1");
        FacebookUser entity = new FacebookUser();
        when(mapper.toEntity(profileDTO)).thenReturn(entity);
        when(facebookUserRepository.update(any(FacebookUser.class)))
                .thenReturn(null);

        // Act
        APIResultSet<FacebookUserDetailDTO> result = service.update(profileDTO);

        // Assert
        assertEquals(404, result.getHttpCode());
        assertEquals("Khách hàng không tồn tại", result.getMessage());
    }

    @Test
    void testUpdate_success() {
        // Arrange
        FacebookUserDetailDTO profileDTO = mockFacebookDetail();
        FacebookUser entity = new FacebookUser();
        FacebookUser updatedEntity = new FacebookUser(); // có thể mock giá trị cụ thể nếu cần
        FacebookUserDetailDTO detailDTO = new FacebookUserDetailDTO();
        when(mapper.toEntity(profileDTO)).thenReturn(entity);
        when(facebookUserRepository.update(any(FacebookUser.class))).thenReturn(updatedEntity);
        when(mapper.toDetailDTO(updatedEntity)).thenReturn(detailDTO);

        // Act
        APIResultSet<FacebookUserDetailDTO> result = service.update(profileDTO);

        // Assert
        assertEquals(200, result.getHttpCode());
        assertEquals("Cập nhật thông tin khách hàng thành công.", result.getMessage());
        assertEquals(detailDTO, result.getData());
    }

    FacebookUserProfileDTO mockFacebookProfile() {
        FacebookUserProfileDTO profile = new FacebookUserProfileDTO();
        FacebookUserProfileDTO.Picture picture = new FacebookUserProfileDTO.Picture();
        FacebookUserProfileDTO.Picture.Data data = new FacebookUserProfileDTO.Picture.Data("url");
        profile.setId("952732");
        picture.setData(data);
        profile.setPicture(picture);
        profile.setFirstName("john");
        profile.setLastName("john");
        profile.setEmail(null);
        return profile;
    }

    FacebookUserDetailDTO mockFacebookDetail() {
        FacebookUserDetailDTO profile = new FacebookUserDetailDTO();
        profile.setFacebookId("1");
        profile.setFacebookName("john joihn");
        profile.setFacebookProfilePic("url");
        return profile;
    }

    @Test
    void testGet_invalid_facebookId_failed() {
        when(facebookUserRepository.get("1")).thenReturn(null);

        APIResultSet<FacebookUserDetailDTO> result = service.get("1");
        assertEquals(404, result.getHttpCode());
        assertEquals("Khách hàng không tồn tại", result.getMessage());
    }

    @Test
    void testGet_internalError_failed() {
        when(facebookUserRepository.get("1")).thenThrow(new RuntimeException());

        APIResultSet<FacebookUserDetailDTO> result = service.get("1");
        assertEquals(500, result.getHttpCode());
        assertEquals("Lỗi hệ thống", result.getMessage());
    }

    @Test
    void testGet_valid_facebookId_success() {

        when(facebookUserRepository.get("1")).thenReturn(new FacebookUser());
        when(mapper.toDetailDTO(any(FacebookUser.class))).thenReturn(any(FacebookUserDetailDTO.class));

        APIResultSet<FacebookUserDetailDTO> result = service.get("1");
        assertEquals(200, result.getHttpCode());
        assertEquals("Truy vấn khách hàng thành công", result.getMessage());
    }

    @Test
    void testGetAll_internalError_failed() {
        when(facebookUserRepository.getAll()).thenThrow(new RuntimeException());
        APIResultSet<List<FacebookUserListDTO>> result = service.getAll();
        assertEquals(500, result.getHttpCode());
        assertEquals("Lỗi hệ thống", result.getMessage());
    }

    @Test
    void testGetAll_success() {
        when(facebookUserRepository.getAll()).thenReturn(new ArrayList<>());
        APIResultSet<List<FacebookUserListDTO>> result = service.getAll();
        assertEquals(200, result.getHttpCode());
        assertEquals("Truy vấn tất cả khách hàng thành công", result.getMessage());
    }
}