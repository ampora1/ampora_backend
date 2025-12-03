package com.ev.ampora_backend.service;

import com.ev.ampora_backend.dto.MessageRequestDto;
import com.ev.ampora_backend.dto.MessageResponseDto;
import com.ev.ampora_backend.repository.MessageRepository;
import com.ev.ampora_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService{
    private  final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Override
    public MessageResponseDto sendMessage(MessageRequestDto requestDto) {
        return null;
    }
}
