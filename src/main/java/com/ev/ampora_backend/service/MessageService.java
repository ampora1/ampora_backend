package com.ev.ampora_backend.service;

import com.ev.ampora_backend.dto.MessageRequestDto;
import com.ev.ampora_backend.dto.MessageResponseDto;

public interface MessageService {
    MessageResponseDto sendMessage(MessageRequestDto requestDto);
}
