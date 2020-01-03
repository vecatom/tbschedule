package com.maiyue.tbscheduleweb.controller;

import com.maiyue.tbscheduleweb.ajax.AjaxResult;
import com.maiyue.tbscheduleweb.modul.MyScheduleServer;
import com.maiyue.tbscheduleweb.modul.MyScheduleTaskType;
import com.maiyue.tbscheduleweb.modul.MyScheduleTaskTypeRunningInfo;
import com.taobao.pamirs.schedule.ConsoleManager;
import com.taobao.pamirs.schedule.taskmanager.ScheduleServer;
import com.taobao.pamirs.schedule.taskmanager.ScheduleTaskItem;
import com.taobao.pamirs.schedule.taskmanager.ScheduleTaskType;
import com.taobao.pamirs.schedule.taskmanager.ScheduleTaskTypeRunningInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("task")
public class TaskController {

    @RequestMapping("list")
    public String toTask(ModelMap mp){
        List<ScheduleTaskType> list = list();
        mp.put("list",list);
        return "/task/list";
    }

    @RequestMapping("edit")
    public String toEdit(String taskType,ModelMap mp){
        MyScheduleTaskType mtt = new MyScheduleTaskType();
        if(!"-1".equals(taskType)){
            mtt = getType(taskType);
            mtt.setEditFlag("create");
        }else{
            mtt.setEditFlag("edit");
        }
        mp.put("taskType",mtt);
        return "/task/edit";
    }

    @RequestMapping("save")
    @ResponseBody
    public AjaxResult saveTaskType(MyScheduleTaskType taskType){
        String editFlag = taskType.getEditFlag();
        try {
            if ("edit".equals(editFlag)) {
                edit(taskType);
            } else if ("create".equals(editFlag)) {
                create(taskType);
            } else {
                return AjaxResult.filed(404);
            }
        }catch (Exception e){
            return AjaxResult.filed(500);
        }
        return AjaxResult.sucess();
    }

    private MyScheduleTaskType getType(String taskTypeName){

        MyScheduleTaskType mtt = new MyScheduleTaskType();

        try {
            ScheduleTaskType taskType = ConsoleManager.getScheduleDataManager().loadTaskTypeBaseInfo(taskTypeName);
            BeanUtils.copyProperties(taskType,mtt);
            mtt.items2Str();
            return mtt;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mtt;
    }

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
    public void create(MyScheduleTaskType taskType) throws Exception {

        ScheduleTaskType scheduleTaskType = init(taskType);
        ConsoleManager.getScheduleDataManager().createBaseTaskType(scheduleTaskType);

    }

    /**
     * 编辑任务
     * @return
     */
    public void edit(MyScheduleTaskType taskType) throws Exception {
        ScheduleTaskType scheduleTaskType = init(taskType);

        ConsoleManager.getScheduleDataManager().updateBaseTaskType(scheduleTaskType);

    }

    /**
     * 删除任务
     * @return
     */
    @RequestMapping("remove")
    @ResponseBody
    public AjaxResult remove(String baseTaskType){

        try {
            ConsoleManager.getScheduleDataManager().clearTaskType( baseTaskType);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.filed(500);
        }
        return AjaxResult.sucess();
    }

    @RequestMapping("puase")
    @ResponseBody
    public AjaxResult puase(String baseTaskType){
        try {
            ConsoleManager.getScheduleDataManager().pauseAllServer(baseTaskType);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.filed(500);
        }
        return AjaxResult.sucess();
    }

    @RequestMapping("resume")
    @ResponseBody
    public AjaxResult resume(String baseTaskType){
        try {
            ConsoleManager.getScheduleDataManager().resumeAllServer(baseTaskType);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.filed(500);
        }
        return AjaxResult.sucess();
    }

    /**
     * 查看任务运行详情
     * @param baseTaskType
     * @param ownSign
     * @return
     */
    @RequestMapping("taskInfo")
    public String taskInfo(String baseTaskType,String ownSign,ModelMap mp){
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

            mp.put("info",myScheduleTaskTypeRunningInfos);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "/task/info";
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
