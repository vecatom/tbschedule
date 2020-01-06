package com.maiyue.tbscheduleweb.controller;

import com.maiyue.tbscheduleweb.ajax.AjaxResult;
import com.maiyue.tbscheduleweb.modul.MyScheduleStrategy;
import com.taobao.pamirs.schedule.ConsoleManager;
import com.taobao.pamirs.schedule.strategy.ScheduleStrategy;
import com.taobao.pamirs.schedule.strategy.ScheduleStrategyRunntime;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/scheduleStrategy")
public class ScheduleStrategyController {

    @RequestMapping("list")
    public String toPage(ModelMap mp){
        mp.put("list",list());
        return "scheduleStrategy/list";
    }

    public List<ScheduleStrategy> list(){
        try {
            List<ScheduleStrategy> scheduleStrategyList = ConsoleManager.getScheduleStrategyManager()
                    .loadAllScheduleStrategy();
            return scheduleStrategyList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<ScheduleStrategy>();
    }

    @GetMapping("edit")
    public String toEdit(String taskTypeName, ModelMap mp){
        MyScheduleStrategy mys = new MyScheduleStrategy();


        ScheduleStrategy scheduleStrategy = null;
        try {
            scheduleStrategy = ConsoleManager.getScheduleStrategyManager().loadStrategy(taskTypeName);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        boolean isNew = false;
//        String actionName = "editScheduleStrategy";
//        String editSts = "";
        String ips = "";
        if (scheduleStrategy != null) {
//            String[] ipList = scheduleStrategy.getIPList();
//            for (int i = 0; ipList != null && i < ipList.length; i++) {
//                if (i > 0) {
//                    ips = ips + ",";
//                }
//                ips = ips + ipList[i];
//            }
//            editSts = "style=\"background-color: blue\" readonly=\"readonly\"";
            BeanUtils.copyProperties(scheduleStrategy,mys);
            mys.setEditFlag("edit");
            mys.setIPListToIps();
        } else {
            scheduleStrategy = new ScheduleStrategy();
            scheduleStrategy.setStrategyName("");
            scheduleStrategy.setKind(ScheduleStrategy.Kind.Schedule);
            scheduleStrategy.setTaskName("");
            scheduleStrategy.setTaskParameter("");
            scheduleStrategy.setNumOfSingleServer(0);
            scheduleStrategy.setAssignNum(2);
            ips = "127.0.0.1";
            BeanUtils.copyProperties(scheduleStrategy,mys);
            mys.setIps(ips);
            mys.setEditFlag("create");
        }
        mp.put("scheduleStrategy",mys);
        return "scheduleStrategy/edit";
    }

    /**
     * 创建策略
     * @return
     */
    @PostMapping("save")
    @ResponseBody
    public AjaxResult save(MyScheduleStrategy scheduleStrategy){
        if("create".equals(scheduleStrategy.getEditFlag())){
            return create(scheduleStrategy);
        }else if("edit".equals(scheduleStrategy.getEditFlag())){
            return edit(scheduleStrategy);
        }else{
            return AjaxResult.filed(404);
        }
    }


    private AjaxResult create(MyScheduleStrategy scheduleStrategy){
        scheduleStrategy.setIPListToIps();
        try {
            ConsoleManager.getScheduleStrategyManager().createScheduleStrategy(scheduleStrategy);
        } catch (Exception e) {
            e.printStackTrace();
            AjaxResult.filed(500);
        }
        return AjaxResult.sucess();
    }

    /**
     * 编辑策略
     * @return
     */
    private AjaxResult edit(MyScheduleStrategy scheduleStrategy){
        scheduleStrategy.setIPListToIps();
        try {
            ConsoleManager.getScheduleStrategyManager().updateScheduleStrategy(scheduleStrategy);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.filed(500);
        }
        return AjaxResult.sucess();
    }

    /**
     * 删除策略
     * @return
     */
    @PostMapping("remove")
    @ResponseBody
    public AjaxResult remove(String strategyName){
        try {
            ConsoleManager.getScheduleStrategyManager().deleteMachineStrategy(strategyName);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.filed(500);
        }
        return AjaxResult.sucess();
    }

    /**
     *  暂停
      */
    @PostMapping("pause")
    @ResponseBody
    public AjaxResult pauseTaskType(String strategyName){
        try {
            ConsoleManager.getScheduleStrategyManager().pause(
                    strategyName);
        } catch (Exception e) {
            e.printStackTrace();
            AjaxResult.filed(500);
        }
        return AjaxResult.sucess();
    }

    /**
     * 恢复
     * @return
     */
    @PostMapping("resume")
    @ResponseBody
    public AjaxResult resumeTaskType(String strategyName){
        try {
            ConsoleManager.getScheduleStrategyManager().resume(
                    strategyName);
        } catch (Exception e) {
            e.printStackTrace();
            AjaxResult.filed(500);
        }
        return AjaxResult.sucess();
    }


    /**
     * 查看任务运行时
     * @param strategyName
     * @param uuid
     * @return
     */
    @RequestMapping("runtime")
    public String getRuntime(String strategyName, String uuid , ModelMap mp){
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
        mp.put("list",runntimeList);
        return "/scheduleStrategy/runtime";
    }

}
