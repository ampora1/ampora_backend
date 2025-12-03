package com.ev.ampora_backend.service;

import com.ev.ampora_backend.dto.MessageRequestDto;
import com.ev.ampora_backend.dto.MessageResponseDto;
import com.ev.ampora_backend.entity.Message;
import com.ev.ampora_backend.entity.Role;
import com.ev.ampora_backend.entity.User;
import com.ev.ampora_backend.repository.MessageRepository;
import com.ev.ampora_backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService{

    private  final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Override
    public MessageResponseDto sendMessage(MessageRequestDto requestDto) {
        User sender = userRepository.findByUserId(requestDto.getSenderId()).orElseThrow(() -> new EntityNotFoundException("Sender not found"));

        User receiver = userRepository.findByUserId(requestDto.getReceiverId()).orElseThrow(() -> new EntityNotFoundException("Receiver not found"));

        if(!sender.getRole().equals(Role.OPERATOR)){
            throw new IllegalArgumentException("Only Operators Can Send Messages");
        }

        if(!receiver.getRole().equals(Role.ADMIN)){
            throw new IllegalArgumentException("Only Admin Can Receive Messages");
        }

        Message newMessage = Message.builder()
                .sender(sender)
                .receiver(receiver)
                .content(requestDto.getContent())
                .subject(requestDto.getSubject())
                .build();

        Message savedMessage = messageRepository.save(newMessage);

        MessageResponseDto response = MessageResponseDto.builder()
                .messageId(savedMessage.getMessageId())
                .senderName(sender.getFullName())
                .receiverName(receiver.getFullName())
                .build();

        return response;
    }
}
