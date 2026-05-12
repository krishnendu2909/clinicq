package com.infy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.infy.dto.QueuePositionDTO;

@Service
public class QueueNotificationService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void broadcastQueueUpdate(QueuePositionDTO queuePosition) {
        messagingTemplate.convertAndSend("/topic/queue-updates", queuePosition);
    }

    public void broadcastTokenUpdate(Long doctorId, String message) {
        messagingTemplate.convertAndSend("/topic/doctor-" + doctorId + "-updates", message);
    }

    public void broadcastQueueStatus(Long doctorId, Integer waitingCount) {
        messagingTemplate.convertAndSend("/topic/doctor-" + doctorId + "-queue", waitingCount);
    }
}
