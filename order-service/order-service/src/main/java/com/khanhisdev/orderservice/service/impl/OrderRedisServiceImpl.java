package com.khanhisdev.orderservice.service.impl;

import com.khanhisdev.orderservice.dto.Request.AddTicketRequest;
import com.khanhisdev.orderservice.dto.Request.DeleteTicketRequest;
import com.khanhisdev.orderservice.dto.Request.GetTicketRequest;
import com.khanhisdev.orderservice.dto.Response.ShowtimeForOrderDto;
import com.khanhisdev.orderservice.exception.ResourceNotFoundException;
import com.khanhisdev.orderservice.service.OrderRedisService;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class OrderRedisServiceImpl extends BaseRedisServiceImpl<String,String,Object> implements OrderRedisService{
    @Value("${movie.host}")
    private String movie_hostname;
    @Autowired
    private WebClient webClient;

    public OrderRedisServiceImpl(RedisTemplate<String, Object> redisTemplate, HashOperations<String, String, Object> hashOperations) {
        super(redisTemplate, hashOperations);
    }

    @Override
    public void addTicketToCart(String userId, AddTicketRequest addTicketRequest) {
        String key= "order:user-"+ userId;
        String fieldKey;
        List<String> seatsNeedOrder= addTicketRequest.getSeats();
        fieldKey= "TicketInfo_"+ "MovieId:"+ addTicketRequest.getMovieId()+","
                +"TheaterId:" + addTicketRequest.getTheaterId()+","
                +"RoomId:"+ addTicketRequest.getProjectionRoomId()+","
                +"StartTime:"+ addTicketRequest.getShowtime();
        if(this.hashExists(key,fieldKey)){
            List<String>  seatsOrdered = (List<String>) this.hashGet(key,fieldKey);
            seatsNeedOrder.addAll(seatsOrdered);

        }
        this.hashSet(key,fieldKey,seatsNeedOrder);
        this.setTimeToLive(key, 30);

    }

    @Override
    public void deleteTicketInCart(String userId, DeleteTicketRequest deleteTicketRequest) {
        String key= "order:user-"+ userId;
        String fieldKey;
        fieldKey= "TicketInfo_"+ "MovieId:"+ deleteTicketRequest.getMovieId()+","
                +"TheaterId:" + deleteTicketRequest.getTheaterId()+","
                +"RoomId:"+ deleteTicketRequest.getProjectionRoomId()+","
                +"StartTime:"+ deleteTicketRequest.getShowtime();
        if(!this.hashExists(key,fieldKey)){
            throw new ResourceNotFoundException("Order","id",deleteTicketRequest.getProjectionRoomId());
        }
        this.delete(key,fieldKey);
    }

    @Override
    public List<ShowtimeForOrderDto> getShowtimeFromCart(String userId) {
        String key= "order:user-"+ userId;
        Map<String, Object> showtime= this.getField(key);
        List<GetTicketRequest> getTicketRequests= new ArrayList<>();
        for (Map.Entry<String, Object> entry : showtime.entrySet()) {
            String info= entry.getKey().split("_")[1];
            String startTime= (info.split(",")[3]).split(":")[1]+":"+(info.split(",")[3]).split(":")[2];
            Long roomId= Long.valueOf((info.split(",")[2]).split(":")[1]);
            List<String>  seatsOrdered = (List<String>) entry.getValue();
            getTicketRequests.add(new GetTicketRequest(startTime,roomId,seatsOrdered));
        }

        List<ShowtimeForOrderDto> response= webClient.post()
                .uri("http://"+movie_hostname+ ":8091/showtime/order")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(getTicketRequests)
                .retrieve()
                .bodyToFlux(ShowtimeForOrderDto.class)
                .collectList()
                .block();
        return response;

    }

    @Override
    public void deleteAllTicket(String userId) {
        String key= "order:user-"+ userId;
        this.delete(key);
    }


}
