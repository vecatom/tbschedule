package com.maiyue.tbscheduleweb.modul;

import com.taobao.pamirs.schedule.strategy.ScheduleStrategy;

import java.util.Arrays;

public class MyScheduleStrategy extends ScheduleStrategy {

    private String ips;

    public String getIps() {
        return ips;
    }

    public void setIps(String ips) {
        this.ips = ips;
    }

    public void setIPListToIps() {
        if(this.ips==null){
            setIPList(new String[0]);
        }else{
            setIPList(this.ips.split(","));
        }
    }

    public void setIps4IPList(){
        String[] ipList = this.getIPList();
        if(ipList!=null&&ipList.length>0){
            this.setIps(Arrays.toString(ipList).replace("[","").replace("]",""));
        }else{
            this.setIps("");
        }
    }
}
