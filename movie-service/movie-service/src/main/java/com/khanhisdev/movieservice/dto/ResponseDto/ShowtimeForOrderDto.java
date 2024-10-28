package com.khanhisdev.movieservice.dto.ResponseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShowtimeForOrderDto {
    private Long id;
    private LocalDateTime startTime;
    private String movieName;
    private String theaterName;
    private Integer projectionRoomName;
    private List<String> seats;
}