package com.example.workforce.models;

import lombok.*;
import jakarta.persistence.*;

import com.example.workforce.enums.LeaveApproval;
import com.example.workforce.models.keys.LeaveRequestId;

@Entity
@Table(name = "Leave_Request")
@Data
@NoArgsConstructor
public class LeaveRequest {
    @EmbeddedId
    private LeaveRequestId id; // (empid, shiftid)

    @Column(name = "Approval", nullable = false)
    private LeaveApproval approval; // "Pending","Approved","Rejected"

    @MapsId("empId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EmpId")
    private Member member;

    @MapsId("shiftId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ShiftId")
    private Shift shift;
}
