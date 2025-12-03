package com.ev.ampora_backend.repository;

import com.ev.ampora_backend.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message , String> {
}
