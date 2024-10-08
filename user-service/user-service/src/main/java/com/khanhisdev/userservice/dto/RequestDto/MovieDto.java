package com.khanhisdev.userservice.dto.RequestDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieDto {
    private Long id;
    private String name;
    private String description;
    // by minutes
    private int durationMin;
    private List<String> director;
    private List<String> actors;
    private String urlImage;
}
