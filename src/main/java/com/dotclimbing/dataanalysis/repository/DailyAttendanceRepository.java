package com.dotclimbing.dataanalysis.repository;

import com.dotclimbing.dataanalysis.entity.DailyAttendance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyAttendanceRepository extends JpaRepository<DailyAttendance, Long> {

}
