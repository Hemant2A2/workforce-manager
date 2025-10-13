package com.example.workforce.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Data
public class AssignMembersRequest {
  private Integer shiftId;
  private boolean autoAssign; // if true, let server pick eligible members for requirements
  // manual assignments (optional) - pairs of memberId and roleId
  private List<ManualAssignment> manualAssignments;

  @Getter @Setter @NoArgsConstructor @AllArgsConstructor
  public static class ManualAssignment {
    private Integer memberId;
    private Integer roleId;
  }
}
