package com.khanhisdev.orderservice.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetTicketRequest {
    private String time;



    private Long roomId;
    List<String > seatsOrdered;
}
