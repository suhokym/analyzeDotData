package com.dotclimbing.dataanalysis.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DailyAttendanceDto {

    @CsvBindByName(column = "회원번호")
    private String memberId;

    @CsvBindByName(column = "이름")
    private String name;

    @CsvBindByName(column = "호칭")
    private String title;           // "님" / ""

    @CsvBindByName(column = "별명")
    private String nickname;

    @CsvBindByName(column = "그룹")
    private String group;           // "[자유]", "10회권", "만기"

    @CsvBindByName(column = "성별")
    private String gender;          // "남" / "여"

    @CsvBindByName(column = "연령")
    private String ageGroup;        // "20대", "30대", ""

    @CsvBindByName(column = "휴대폰")
    private String phone;

    @CsvBindByName(column = "입장")
    private String entryTime;       // "15:34"

    @CsvBindByName(column = "퇴장")
    private String exitTime;        // "" 빈 값 있음

    @CsvBindByName(column = "머문시간")
    private String stayDuration;    // "" 빈 값 있음

    @CsvBindByName(column = "담당자")
    private String manager;         // "오양호"

    @CsvBindByName(column = "주소")
    private String address;

    @CsvBindByName(column = "이메일")
    private String email;

    @CsvBindByName(column = "생일")
    private String birthday;        // "1998-09-03" or ""

    @CsvBindByName(column = "기타1")
    private String etc1;

    @CsvBindByName(column = "기타2")
    private String etc2;

    @CsvBindByName(column = "포인트")
    private String point;           // =""0"" 형태 → 파싱 후 변환 필요

    @CsvBindByName(column = "등록일자")
    private String registeredDate;  // "2023-12-01"
}
