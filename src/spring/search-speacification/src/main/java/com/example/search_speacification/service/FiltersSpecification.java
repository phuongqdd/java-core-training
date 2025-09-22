package com.example.search_speacification.service;

import com.example.search_speacification.dto.SearchRequest;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FiltersSpecification<T> {

    public Specification<T> getSearchSpecification(SearchRequest request){
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get(request.getColumn()), request.getName());
            }
        };
    }

    public Specification<T> getSearchSpecification(List<SearchRequest> searchRequests){
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            for(SearchRequest request : searchRequests){
                Predicate equal = criteriaBuilder.equal(root.get(request.getColumn()), request.getName());
                predicates.add(equal);
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }

}
