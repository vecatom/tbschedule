package com.maiyue.tbscheduleweb.controller;

import com.maiyue.tbscheduleweb.modul.MyScheduleStrategy;
import com.taobao.pamirs.schedule.ConsoleManager;
import com.taobao.pamirs.schedule.strategy.ScheduleStrategy;
import com.taobao.pamirs.schedule.strategy.ScheduleStrategyRunntime;

import java.util.ArrayList;
import java.util.List;

public class ScheduleStrategyController {

    public List<ScheduleStrategy> list(){
        try {
            List<ScheduleStrategy> scheduleStrategyList = ConsoleManager.getScheduleStrategyManager()
                    .loadAllScheduleStrategy();
            return scheduleStrategyList;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 创建策略
     * @return
     */
    public String create(MyScheduleStrategy scheduleStrategy){
        scheduleStrategy.setIPListToIps();
        try {
            ConsoleManager.getScheduleStrategyManager().createScheduleStrategy(scheduleStrategy);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 编辑策略
     * @return
     */
    public String edit(MyScheduleStrategy scheduleStrategy){
        scheduleStrategy.setIPListToIps();
        try {
            ConsoleManager.getScheduleStrategyManager().updateScheduleStrategy(scheduleStrategy);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除策略
     * @return
     */
    public String remove(String strategyName){
        try {
            ConsoleManager.getScheduleStrategyManager().deleteMachineStrategy(strategyName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     *  暂停
      */
    public String pauseTaskType(String strategyName){
        try {
            ConsoleManager.getScheduleStrategyManager().pause(
                    strategyName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 恢复
     * @return
     */
    public String resumeTaskType(String strategyName){
        try {
            ConsoleManager.getScheduleStrategyManager().resume(
                    strategyName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查看任务详情
     * @return
     */
    public String strategyDetail(){
        return null;
    }

    /**
     * 查看任务运行时
     * @param strategyName
     * @param uuid
     * @return
     */
    public List<ScheduleStrategyRunntime> getRuntime(String strategyName,String uuid ){
        List<ScheduleStrategyRunntime> runntimeList = null;
        if (strategyName != null && strategyName.trim().length() > 0) {
            try {
                runntimeList = ConsoleManager.getScheduleStrategyManager()
                        .loadAllScheduleStrategyRunntimeByTaskType(strategyName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (uuid != null && uuid.trim().length() > 0) {
            try {
                runntimeList = ConsoleManager.getScheduleStrategyManager().loadAllScheduleStrategyRunntimeByUUID(uuid);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            runntimeList = new ArrayList<ScheduleStrategyRunntime>();
        }
        return runntimeList;
    }

}
