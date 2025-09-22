package com.dophuong.lms.learning_management_system.specification;

import com.dophuong.lms.learning_management_system.entity.Quiz;
import org.springframework.data.jpa.domain.Specification;

public class QuizSpecification {
    public static Specification<Quiz> hasTitle(String title){
        return (root, query, criteriaBuilder) ->
            title == null ? criteriaBuilder.conjunction() :
                    criteriaBuilder.like(root.get("title"), "%" + title + "%")
        ;
    }

    public static Specification<Quiz> isPublished(Boolean published){
        return (root, query, criteriaBuilder) ->
                published == null ? criteriaBuilder.conjunction() :
                        criteriaBuilder.equal(root.get("isPublished"), published);
    }

    public static Specification<Quiz> belongsToCourse(Long courseId){
        return (root, query, criteriaBuilder) ->
                courseId == null ? criteriaBuilder.conjunction() : 
                        criteriaBuilder.equal(root.join("course").get("id"), courseId);
    }
}
