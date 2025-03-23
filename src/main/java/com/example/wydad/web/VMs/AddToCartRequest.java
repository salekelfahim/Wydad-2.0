package com.example.wydad.web.VMs;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddToCartRequest {
    private Integer productId;
    private Integer ticketId;
    private Double quantity;
}
