package com.ev.ampora_backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class MessageResponseDto {
    private String messageId;
    private String senderName;
    private  String receiverName;
}
