package com.example.workforce.services;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

import com.example.workforce.dtos.CreateShiftRequest;
import com.example.workforce.dtos.CreateWeeklyShiftRequest;
import com.example.workforce.dtos.RequirementDto;
import com.example.workforce.dtos.ShiftDto;
import com.example.workforce.mappers.ShiftMapper;
import com.example.workforce.models.Location;
import com.example.workforce.models.Requirement;
import com.example.workforce.models.Role;
import com.example.workforce.models.Shift;
import com.example.workforce.models.keys.RequirementId;
import com.example.workforce.repositories.LocationRepository;
import com.example.workforce.repositories.RequirementRepository;
import com.example.workforce.repositories.RoleRepository;
import com.example.workforce.repositories.ShiftRepository;
import com.example.workforce.utils.ShiftNotFoundException;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ShiftService {
  private final ShiftRepository shiftRepository;
  private final RequirementRepository requirementRepository;
  private final RoleRepository roleRepository;
  // private final WeekRepository weekRepository;
  private final LocationRepository locationRepository;

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

  @Transactional
  public ShiftDto createShift(CreateShiftRequest req) {
    Location loc = locationRepository.findById(req.getLocationId())
      .orElseThrow(() -> new IllegalArgumentException("Location not found"));

    Shift shift = new Shift();
    shift.setDay(req.getDay());
    shift.setStartTime(req.getStartTime());
    shift.setEndTime(req.getEndTime());
    shift.setTitle(req.getTitle());
    shift.setLocation(loc);

    Shift saved = shiftRepository.save(shift);

    if (req.getRequirements() != null) {
      for (RequirementDto r : req.getRequirements()) {
        Role role = roleRepository.findById(r.getRoleId())
            .orElseThrow(() -> new IllegalArgumentException("Role not found: " + r.getRoleId()));

        Requirement requirement = new Requirement();
        requirement.setId(new RequirementId(role.getId(), saved.getId()));
        requirement.setCount(r.getCount());
        requirement.setRole(role);
        requirement.setShift(saved);
        requirementRepository.save(requirement);
        var savedRequirement = requirementRepository.save(requirement);
        saved.getRequirements().add(savedRequirement);
      }
    }

    return shiftMapper.toDto(saved);
  }

  @Transactional
  public List<ShiftDto> createWeeklyShifts(CreateWeeklyShiftRequest req) {
    // LocalDate start = req.getWeekStartDate();
    // Week week = weekRepository.findByStartDate(start).orElse(null);
    // if(week == null) {
    //   week = new Week();
    //   week.setStartDate(start);
    //   weekRepository.save(week);
    // }

    List<ShiftDto> results = new ArrayList<>();
    for (CreateShiftRequest dayShift : req.getPerDayShifts()) {
      CreateShiftRequest perDay = new CreateShiftRequest(
        dayShift.getTitle(),
        dayShift.getDay(),
        dayShift.getStartTime(),
        dayShift.getEndTime(),
        dayShift.getLocationId(),
        dayShift.getRequirements()
      );
      ShiftDto dto = createShift(perDay);
      results.add(dto);
    }

    return results;
  }

}
