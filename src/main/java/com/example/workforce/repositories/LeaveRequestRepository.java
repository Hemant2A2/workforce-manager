package com.example.workforce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.workforce.enums.LeaveApproval;
import com.example.workforce.models.LeaveRequest;
import com.example.workforce.models.keys.LeaveRequestId;

import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, LeaveRequestId> {
  List<LeaveRequest> findByApproval(LeaveApproval approval);

  List<LeaveRequest> findByShiftId(Integer shiftId);

  List<LeaveRequest> findByMemberId(Integer memberId);

  List<LeaveRequest> findByShiftLocationId(Integer locationId);

  default LeaveRequest findByMemberIdAndShiftId(Integer memberId, Integer shiftId) {
    return findById(new LeaveRequestId(memberId, shiftId)).orElse(null);
  }
}
