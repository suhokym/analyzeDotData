package com.dotclimbing.dataanalysis.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "daily_attendance")
public class DailyAttendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String memberId;
    private String name;
    private String title;
    private String nickname;
    @Column(name = "member_group")
    private String group;
    private String gender;
    private String ageGroup;
    private String phone;
    private String entryTime;
    private String exitTime;
    private String stayDuration;
    private String manager;

    @Column(length = 500)
    private String address;

    private String email;
    private String birthday;
    private String etc1;
    private String etc2;
    private String point;
    private String registeredDate;
}
