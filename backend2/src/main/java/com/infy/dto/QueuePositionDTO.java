package com.infy.dto;

import java.time.LocalDateTime;

public class QueuePositionDTO {
    private String patientId;
    private String name;
    private Integer queueNumber;
    private Integer position;
    private String status;
    private String doctorName;
    private String department;
    private Integer estimatedWaitTime;
    private LocalDateTime timestamp;

    public QueuePositionDTO() {}

    public QueuePositionDTO(String patientId, String name, Integer queueNumber, Integer position, 
                         String status, String doctorName, String department, 
                         Integer estimatedWaitTime, LocalDateTime timestamp) {
        this.patientId = patientId;
        this.name = name;
        this.queueNumber = queueNumber;
        this.position = position;
        this.status = status;
        this.doctorName = doctorName;
        this.department = department;
        this.estimatedWaitTime = estimatedWaitTime;
        this.timestamp = timestamp;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQueueNumber() {
        return queueNumber;
    }

    public void setQueueNumber(Integer queueNumber) {
        this.queueNumber = queueNumber;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getEstimatedWaitTime() {
        return estimatedWaitTime;
    }

    public void setEstimatedWaitTime(Integer estimatedWaitTime) {
        this.estimatedWaitTime = estimatedWaitTime;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
