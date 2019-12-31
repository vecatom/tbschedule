package com.maiyue.tbscheduleweb.modul;

import com.taobao.pamirs.schedule.strategy.ManagerFactoryInfo;

public class MyManagerFactoryInfo extends ManagerFactoryInfo {

    private String sts = "";
    private String action;
    private String actionName;

    public String getSts() {
        return sts;
    }

    public void setSts(String sts) {
        this.sts = sts;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }
}
