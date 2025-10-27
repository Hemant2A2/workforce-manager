// package com.example.workforce.repositories;

// import org.springframework.data.jpa.repository.JpaRepository;

// import com.example.workforce.models.LeaveRequest;
// import com.example.workforce.models.keys.LeaveRequestId;

// import java.util.List;

// public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, LeaveRequestId> {
//   List<LeaveRequest> findByApproval(String approval);

//   List<LeaveRequest> findByShift_Id(Integer shiftId);

//   List<LeaveRequest> findByMember_Id(Integer memberId);

//   List<LeaveRequest> findByShift_Location_Id(Integer locationId);

//   default LeaveRequest findByMemberIdAndShiftId(Integer memberId, Integer shiftId) {
//     return findById(new LeaveRequestId(memberId, shiftId)).orElse(null);
//   }
// }
