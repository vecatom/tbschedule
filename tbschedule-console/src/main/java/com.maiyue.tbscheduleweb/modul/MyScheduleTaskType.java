package com.maiyue.tbscheduleweb.modul;

import com.taobao.pamirs.schedule.taskmanager.ScheduleTaskType;

public class MyScheduleTaskType extends ScheduleTaskType {

    private String itemDefines;

    public String getItemDefines() {
        return itemDefines;
    }

    public void setItemDefines(String itemDefines) {
        this.itemDefines = itemDefines;
    }
}
