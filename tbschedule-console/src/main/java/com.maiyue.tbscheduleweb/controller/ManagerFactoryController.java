package com.maiyue.tbscheduleweb.controller;

import com.maiyue.tbscheduleweb.modul.MyManagerFactoryInfo;
import com.taobao.pamirs.schedule.ConsoleManager;
import com.taobao.pamirs.schedule.strategy.ManagerFactoryInfo;

import java.util.ArrayList;
import java.util.List;

public class ManagerFactoryController {

    public List<MyManagerFactoryInfo> list(){
        List<ManagerFactoryInfo> list = null;
        try {
            list = ConsoleManager.getScheduleStrategyManager().loadAllManagerFactoryInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<MyManagerFactoryInfo> mlist = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            ManagerFactoryInfo info = list.get(i);
            MyManagerFactoryInfo minfo = new MyManagerFactoryInfo();
            minfo.setUuid(info.getUuid());
            if (info.isStart() == true) {
                minfo.setSts("运行");
                minfo.setAction("stopManagerFactory");
                minfo.setActionName("停止");

            } else {

                minfo.setSts("休眠");
                minfo.setAction("startManagerFactory");
                minfo.setActionName("启动");

            }
            mlist.add(minfo);
        }
        return mlist;
    }

    public String start(String uuid){
        try {
            ConsoleManager.getScheduleStrategyManager().updateManagerFactoryInfo(uuid, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String stop(String uuid){
        try {
            ConsoleManager.getScheduleStrategyManager().updateManagerFactoryInfo(uuid, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
