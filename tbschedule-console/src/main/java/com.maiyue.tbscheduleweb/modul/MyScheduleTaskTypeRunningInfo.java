package com.maiyue.tbscheduleweb.modul;

import com.taobao.pamirs.schedule.taskmanager.ScheduleTaskItem;
import com.taobao.pamirs.schedule.taskmanager.ScheduleTaskTypeRunningInfo;

import java.util.List;

public class MyScheduleTaskTypeRunningInfo extends ScheduleTaskTypeRunningInfo {


    private List<MyScheduleServer> myScheduleServers;

    private List<ScheduleTaskItem> scheduleTaskItems;


    public List<MyScheduleServer> getMyScheduleServers() {
        return myScheduleServers;
    }

    public void setMyScheduleServers(List<MyScheduleServer> myScheduleServers) {
        this.myScheduleServers = myScheduleServers;
    }

    public List<ScheduleTaskItem> getScheduleTaskItems() {
        return scheduleTaskItems;
    }

    public void setScheduleTaskItems(List<ScheduleTaskItem> scheduleTaskItems) {
        this.scheduleTaskItems = scheduleTaskItems;
    }
}
