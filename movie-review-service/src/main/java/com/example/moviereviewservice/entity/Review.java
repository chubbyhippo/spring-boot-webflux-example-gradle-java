package com.example.moviereviewservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    @Id
    private String id;
    private String movieInfoId;
    private String comment;
    private Double rating;
}
