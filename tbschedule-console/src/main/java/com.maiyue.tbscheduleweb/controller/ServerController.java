package com.maiyue.tbscheduleweb.controller;

import com.taobao.pamirs.schedule.ConsoleManager;
import com.taobao.pamirs.schedule.taskmanager.ScheduleServer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ServerController {

    @RequestMapping("server/list")
    @ResponseBody
    List<ScheduleServer> getServerList(String managerFactoryUUID,String baseTaskType,String  ownSign ,String ip,String orderStr) throws Exception {

        List<ScheduleServer> serverList = null;
        if (managerFactoryUUID != null && managerFactoryUUID.trim().length() > 0) {
            serverList = ConsoleManager.getScheduleDataManager()
                    .selectScheduleServerByManagerFactoryUUID(managerFactoryUUID);
        } else {
            serverList = ConsoleManager.getScheduleDataManager()
                    .selectScheduleServer(baseTaskType, ownSign, ip, orderStr);
        }
        return serverList;
    }

}
