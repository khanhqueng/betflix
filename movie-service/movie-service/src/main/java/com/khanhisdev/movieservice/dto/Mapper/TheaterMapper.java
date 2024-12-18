package com.khanhisdev.movieservice.dto.Mapper;

import com.khanhisdev.movieservice.dto.RequestDto.TheaterRequestDto;
import com.khanhisdev.movieservice.dto.ResponseDto.TheaterResponseDto;
import com.khanhisdev.movieservice.dto.ResponseDto.TheatersAndShowTimesResponseDto;
import com.khanhisdev.movieservice.entity.Theater;
import lombok.Getter;
import lombok.Setter;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Mapper(uses = {ProjectionRoomMapper.class})
public interface TheaterMapper extends GenericMapper<Theater, TheaterRequestDto, TheaterResponseDto>{
    TheatersAndShowTimesResponseDto mapToTheaterWithShowtime(Theater theater);
}
