package com.binhbkfx02295.cshelpdesk.facebookuser.repository.dao;

import com.binhbkfx02295.cshelpdesk.facebookuser.dto.FacebookUserSearchCriteria;
import com.binhbkfx02295.cshelpdesk.facebookuser.entity.FacebookUser;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface FacebookUserDAO {

    FacebookUser save(FacebookUser user) throws RuntimeException;

    FacebookUser get(String id) throws RuntimeException;

    List<FacebookUser> search(String fullName) throws RuntimeException;

    Map<String, Object> search(FacebookUserSearchCriteria criteria, Pageable page) throws RuntimeException;

    List<FacebookUser> getAll() throws RuntimeException;

    FacebookUser update(FacebookUser updatedUser) throws RuntimeException;

    boolean existsById(String id) throws RuntimeException;

    void deleteById(String facebookUserId) throws RuntimeException;

    void deleteAll(List<String> ids) throws RuntimeException;

    FacebookUser getReferenceById(String facebookId) throws RuntimeException;
}
