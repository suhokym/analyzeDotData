package com.dotclimbing.dataanalysis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyAttendanceResultDto {

    private String name;
    private String entryTime;
    private String group;


}
