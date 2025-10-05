package com.binhbkfx02295.cshelpdesk.repository;

import com.binhbkfx02295.cshelpdesk.dto.TicketSearchCriteria;
import com.binhbkfx02295.cshelpdesk.entity.Ticket;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;


import java.util.ArrayList;
import java.util.List;

public class TicketSpecification  {

    public static Specification<Ticket> build(TicketSearchCriteria criteria) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (criteria.getAssignee() != null) {
                predicates.add(cb.equal(root.get("assignee").get("username"), criteria.getAssignee()));
            }
            if (criteria.getFacebookId() != null) {
                predicates.add(cb.equal(root.get("facebookUser").get("facebookId"), criteria.getFacebookId()));
            }
            if (criteria.getTitle() != null) {
                predicates.add(cb.like(cb.lower(root.get("title")), "%" + criteria.getTitle().toLowerCase() + "%"));
            }
            if (criteria.getProgressStatus() != 0) {
                predicates.add(cb.equal(root.get("progressStatus").get("id"), criteria.getProgressStatus()));
            }
            if (criteria.getFromTime() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), criteria.getFromTime()));
            }
            if (criteria.getToTime() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), criteria.getToTime()));
            }
            if (criteria.getCategory() != 0) {
                predicates.add(cb.equal(root.get("category").get("id"), criteria.getCategory()));
            }
            if (criteria.getEmotion() != 0) {
                predicates.add(cb.equal(root.get("emotion").get("id"), criteria.getEmotion()));
            }
            if (criteria.getSatisfaction() != 0) {
                predicates.add(cb.equal(root.get("satisfaction").get("id"), criteria.getSatisfaction()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
