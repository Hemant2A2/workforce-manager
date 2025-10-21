package com.example.workforce.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.workforce.dtos.CreateShiftRequest;
import com.example.workforce.dtos.CreateWeeklyShiftRequest;
import com.example.workforce.dtos.ShiftDto;
import com.example.workforce.services.ShiftService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/shifts")
public class ShiftController {

  private final ShiftService shiftService;

  @GetMapping
  public Iterable<ShiftDto> getAllShifts() {
    return shiftService.getAllShifts();
  }

  @GetMapping("/{id}")
  public ShiftDto getShift(@PathVariable Integer id) {
    return shiftService.getShift(id);
  }

  @PostMapping
  public ResponseEntity<?> createShift(
    @RequestBody CreateShiftRequest request,
    UriComponentsBuilder uriBuilder) {

    ShiftDto shiftDto = shiftService.createShift(request);
    var uri = uriBuilder.path("/shifts/{id}").buildAndExpand(shiftDto.getId()).toUri();
    return ResponseEntity.created(uri).body(shiftDto);
  }

  @PostMapping("/weekly")
  public ResponseEntity<?> createWeeklyShift(
    @RequestBody CreateWeeklyShiftRequest request,
    UriComponentsBuilder uriBuilder) {

    List<ShiftDto> shiftDtoList = shiftService.createWeeklyShifts(request);
    // shiftDtoList.forEach(dto -> System.out.println("created shift id = " + dto.getId()));
    // List<ResponseEntity<?>> responseEntities = new ArrayList<>();
    // for(ShiftDto shiftDto: shiftDtoList) {
    //   var uri = uriBuilder.path("/shifts/{id}").buildAndExpand(shiftDto.getId()).toUri();
    //   responseEntities.add(ResponseEntity.created(uri).body(shiftDto));
    // }
    // return responseEntities;
    return ResponseEntity.ok(shiftDtoList);
  }

}
