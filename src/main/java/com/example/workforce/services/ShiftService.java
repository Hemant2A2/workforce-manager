package com.example.workforce.services;

import org.springframework.stereotype.Service;

import com.example.workforce.dtos.CreateShiftRequest;
import com.example.workforce.dtos.ShiftDto;
import com.example.workforce.mappers.ShiftMapper;
import com.example.workforce.repositories.ShiftRepository;
import com.example.workforce.utils.ShiftNotFoundException;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ShiftService {

  private final ShiftRepository shiftRepository;
  private final ShiftMapper shiftMapper;

  public Iterable<ShiftDto> getAllShifts() {
    return shiftRepository.findAll()
            .stream()
            .map(shiftMapper::toDto)
            .toList();
  }

  public ShiftDto getShift(Integer id) {
    var shift = shiftRepository.findById(id)
                .orElseThrow(ShiftNotFoundException::new);
    return shiftMapper.toDto(shift);
  }

  public ShiftDto createShift(CreateShiftRequest request) {
    var shift = shiftMapper.toEntity(request);
    shiftRepository.save(shift);
    return shiftMapper.toDto(shift);
  }

}
