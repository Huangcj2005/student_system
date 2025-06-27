package com.example.student_system.service.account;

public interface MailService {
    public void sendSimpleMail(String email, String subject, String content);
}
