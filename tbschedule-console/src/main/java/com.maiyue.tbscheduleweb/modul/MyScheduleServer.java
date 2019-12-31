package com.maiyue.tbscheduleweb.modul;

import com.taobao.pamirs.schedule.taskmanager.ScheduleServer;

public class MyScheduleServer extends ScheduleServer {

    private Integer index;
    private String bgColor;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }
}
