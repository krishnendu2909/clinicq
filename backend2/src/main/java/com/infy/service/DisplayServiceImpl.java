package com.infy.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.infy.dto.*;
import com.infy.entity.Token;
import com.infy.models.Department;
import com.infy.models.TokenStatus;
import com.infy.repository.TokenRepository;

@Service
public class DisplayServiceImpl implements DisplayService {

  @Autowired
  private TokenRepository tokenRepository;

  @Override
  public NowServingResponseDTO getNowServing() {

      List<Token> tokens = tokenRepository.findTodayActiveTokens();

      Map<String, Map<Long, DoctorQueueDTO>> departmentMap = new LinkedHashMap<>();

      //  LOOP THROUGH TOKENS
      for (Token token : tokens) {

          Department department = token.getDoctor().getDepartment();
          String dept = department.name();

          Long docId = token.getDoctor().getId();

          long currentPauseMinutes=0;
          if(Boolean.TRUE.equals(token.getDoctor().getPaused())&& token.getDoctor().getPausedAt()!=null)
          {
                      currentPauseMinutes=Duration.between(token.getDoctor().getPausedAt(), LocalDateTime.now()).toMinutes();
          }
          //  Create department if not exists
          departmentMap.putIfAbsent(dept, new LinkedHashMap<>());
          Map<Long, DoctorQueueDTO> doctorMap = departmentMap.get(dept);

          //  Create doctor entry if not exists
          doctorMap.putIfAbsent(docId,
                  new DoctorQueueDTO(
                          docId,
                          token.getDoctor().getName(),
                          null,
                          token.getDoctor().getPaused(),
                          token.getDoctor().getPausedAt(),
                          currentPauseMinutes,
                          new ArrayList<>()                        
                  )
          );

          DoctorQueueDTO dto = doctorMap.get(docId);

           if (token.getStatus() == TokenStatus.IN_CONSULTATION) {

              TokenDisplayDTO current = new TokenDisplayDTO();
              current.setToken(token.getTokenDisplay());
              current.setPatientName(token.getPatient().getName());

              dto.setCurrentToken(current);
          }

          //  NEXT TOKENS (LIMIT 3)
          else if (token.getStatus() == TokenStatus.WAITING && dto.getNextTokens().size() < 3) {

                  TokenDisplayDTO td = new TokenDisplayDTO();
                  td.setToken(token.getTokenDisplay());
                  td.setPatientName(token.getPatient().getName());

                  dto.getNextTokens().add(td);
              }
          
      }

      //  IMPORTANT: BUILD RESPONSE AFTER LOOP
      List<DepartmentQueueDTO> departments = new ArrayList<>();

      for (Map.Entry<String, Map<Long, DoctorQueueDTO>> entry : departmentMap.entrySet()) {

          DepartmentQueueDTO deptDTO = new DepartmentQueueDTO();
          deptDTO.setDepartmentName(entry.getKey());
          deptDTO.setDoctors(new ArrayList<>(entry.getValue().values()));

          departments.add(deptDTO);
      }

      NowServingResponseDTO response = new NowServingResponseDTO();
      response.setDepartments(departments);

      return response;
  }
}