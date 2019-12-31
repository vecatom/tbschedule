package com.maiyue.tbscheduleweb.controller;

import com.maiyue.tbscheduleweb.modul.MyScheduleServer;
import com.maiyue.tbscheduleweb.modul.MyScheduleStrategy;
import com.maiyue.tbscheduleweb.modul.MyScheduleTaskType;
import com.maiyue.tbscheduleweb.modul.MyScheduleTaskTypeRunningInfo;
import com.taobao.pamirs.schedule.ConsoleManager;
import com.taobao.pamirs.schedule.taskmanager.ScheduleServer;
import com.taobao.pamirs.schedule.taskmanager.ScheduleTaskItem;
import com.taobao.pamirs.schedule.taskmanager.ScheduleTaskType;
import com.taobao.pamirs.schedule.taskmanager.ScheduleTaskTypeRunningInfo;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class TaskController {


    public List<ScheduleTaskType> list(){
        try {
            List<ScheduleTaskType> taskTypes = ConsoleManager.getScheduleDataManager().getAllTaskTypeBaseInfo();
            return taskTypes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建任务
     * @return
     */
    public String create(MyScheduleTaskType taskType){

        ScheduleTaskType scheduleTaskType = init(taskType);
        try {
            ConsoleManager.getScheduleDataManager().createBaseTaskType(scheduleTaskType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 编辑任务
     * @return
     */
    public String edit(MyScheduleTaskType taskType){
        ScheduleTaskType scheduleTaskType = init(taskType);
        try {
            ConsoleManager.getScheduleDataManager().updateBaseTaskType(scheduleTaskType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除任务
     * @return
     */
    public String remove(String baseTaskType){

        try {
            ConsoleManager.getScheduleDataManager().clearTaskType( baseTaskType);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String puase(String baseTaskType){
        try {
            ConsoleManager.getScheduleDataManager().pauseAllServer(baseTaskType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String resume(String baseTaskType){
        try {
            ConsoleManager.getScheduleDataManager().resumeAllServer(baseTaskType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查看任务运行详情
     * @param baseTaskType
     * @param ownSign
     * @return
     */
    public String taskInfo(String baseTaskType,String ownSign){
        try {
            List<ScheduleTaskTypeRunningInfo> taskTypeRunningInfoList = ConsoleManager.getScheduleDataManager()
                    .getAllTaskTypeRunningInfo(baseTaskType);
            if(taskTypeRunningInfoList==null||taskTypeRunningInfoList.size()==0){
                // 任务没有运行期信息
            }
            List<MyScheduleTaskTypeRunningInfo> myScheduleTaskTypeRunningInfos = new ArrayList<>();
            for (ScheduleTaskTypeRunningInfo info:taskTypeRunningInfoList) {
                MyScheduleTaskTypeRunningInfo myInfo = new MyScheduleTaskTypeRunningInfo();


                // 查看每台机器运行情况
                List<MyScheduleServer> myScheduleServers = new ArrayList<>();
                List<ScheduleServer> scheduleServers = ConsoleManager.getScheduleDataManager().selectAllValidScheduleServer(info.getTaskType());
                for (ScheduleServer server: scheduleServers ) {
                    MyScheduleServer myServer = new MyScheduleServer();
                    BeanUtils.copyProperties(server, myServer);
                    ScheduleTaskType base = ConsoleManager.getScheduleDataManager().loadTaskTypeBaseInfo(server.getBaseTaskType());
                    if(server.getCenterServerTime().getTime() - server.getHeartBeatTime().getTime()> base.getJudgeDeadInterval()){
                        myServer.setBgColor("BGCOLOR='#A9A9A9'");
                    }else if(server.getLastFetchDataTime()==null||server.getCenterServerTime().getTime() - server.getLastFetchDataTime().getTime()>base.getHeartBeatRate()*20){
                        myServer.setBgColor("BGCOLOR='#FF0000'");
                    }
                    myScheduleServers.add(myServer);
                }
                myInfo.setMyScheduleServers(myScheduleServers);

                // 查看每台机器的队列情况
                List<ScheduleTaskItem> scheduleTaskItems = ConsoleManager.getScheduleDataManager().loadAllTaskItem(info.getTaskType());
                myInfo.setScheduleTaskItems(scheduleTaskItems);

                myScheduleTaskTypeRunningInfos.add(myInfo);
            }

            // TODO 添加返回值

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private ScheduleTaskType init(MyScheduleTaskType taskType){
        taskType.setHeartBeatRate(taskType.getHeartBeatRate() == 0 ? 0 : taskType.getHeartBeatRate() * 1000);
        taskType.setJudgeDeadInterval(taskType.getJudgeDeadInterval() == 0 ? 0
                : taskType.getJudgeDeadInterval() * 1000);
        taskType.setSleepTimeNoData(taskType.getSleepTimeNoData() == 0 ? 0
                : taskType.getSleepTimeNoData() * 1000);
        taskType.setSleepTimeInterval(taskType.getSleepTimeInterval() == 0 ? 0
                : taskType.getSleepTimeInterval() * 1000);
        //taskType.setExpireOwnSignInterval(request.getParameter("expireOwnSignInterval")==null?0: Integer.parseInt(request.getParameter("threadNumber")));

        String itemDefines = taskType.getItemDefines();
        itemDefines = itemDefines.replaceAll("\r", "");
        itemDefines = itemDefines.replaceAll("\n", "");
        taskType.setTaskItems(ScheduleTaskType.splitTaskItem(itemDefines));
        ScheduleTaskType scheduleTaskType = new ScheduleTaskType();
        BeanUtils.copyProperties(taskType,scheduleTaskType);
        return scheduleTaskType;
    }
}
