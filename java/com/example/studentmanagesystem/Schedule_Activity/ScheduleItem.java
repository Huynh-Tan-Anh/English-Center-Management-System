package com.example.studentmanagesystem.Schedule_Activity;

public class ScheduleItem {
    private String className;
    private int studentCount;
    private String day;
    private String time;

    public ScheduleItem(String className, int studentCount, String day, String time) {
        this.className = className;
        this.studentCount = studentCount;
        this.day = day;
        this.time = time;
    }

    public String getClassName() {
        return className;
    }

    public int getStudentCount() {
        return studentCount;
    }

    public String getDay() {
        return day;
    }

    public String getTime() {
        return time;
    }
}
