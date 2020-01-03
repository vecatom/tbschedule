package com.maiyue.tbscheduleweb.modul;

import com.taobao.pamirs.schedule.taskmanager.ScheduleTaskType;

import java.util.Arrays;

public class MyScheduleTaskType extends ScheduleTaskType {

    private String editFlag;

    private String itemDefines;

    private String itemStr;

    public String getItemDefines() {
        return itemDefines;
    }

    public void setItemDefines(String itemDefines) {
        this.itemDefines = itemDefines;
    }

    public String getItemStr() {
        return itemStr;
    }

    public void setItemStr(String itemStr) {
        this.itemStr = itemStr;
    }

    public void items2Str(){

        String[] taskItems = this.getTaskItems();
        String s = Arrays.toString(taskItems).replace("[","").replace("]","");
        this.setItemStr(s);
    }

    public String getEditFlag() {
        return editFlag;
    }

    public void setEditFlag(String editFlag) {
        this.editFlag = editFlag;
    }
}
