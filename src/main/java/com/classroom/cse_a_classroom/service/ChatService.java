package com.classroom.cse_a_classroom.service;

import com.classroom.cse_a_classroom.model.*;
import com.classroom.cse_a_classroom.repository.ChatMessageRepository;
import com.classroom.cse_a_classroom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

@Service
public class ChatService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public ChatMessage sendGroupMessage(User sender, Classroom classroom, String content, String fileUrl, String fileName, String fileType) {
        ChatMessage message = ChatMessage.builder()
                .sender(sender)
                .classroom(classroom)
                .content(content)
                .fileUrl(fileUrl)
                .fileName(fileName)
                .fileType(fileType)
                .build();
        return chatMessageRepository.save(message);
    }

    @Transactional
    public ChatMessage sendPersonalMessage(User sender, User receiver, String content, String fileUrl, String fileName, String fileType) {
        ChatMessage message = ChatMessage.builder()
                .sender(sender)
                .receiver(receiver)
                .content(content)
                .fileUrl(fileUrl)
                .fileName(fileName)
                .fileType(fileType)
                .build();
        return chatMessageRepository.save(message);
    }

    public List<ChatMessage> getGroupMessages(Classroom classroom) {
        return chatMessageRepository.findByClassroomOrderBySentAtAsc(classroom);
    }

    public List<ChatMessage> getPersonalMessages(User user1, User user2) {
        return chatMessageRepository.findPersonalMessages(user1, user2);
    }
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
