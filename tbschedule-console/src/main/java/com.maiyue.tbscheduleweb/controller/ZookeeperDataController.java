package com.maiyue.tbscheduleweb.controller;

import com.taobao.pamirs.schedule.ConsoleManager;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.StringWriter;

//@RestController
//@RequestMapping("zkData")
public class ZookeeperDataController {


//    @RequestMapping("query")
    String queryZKTree(String path) throws Exception {
        if(StringUtils.isEmpty(path)){
            path=ConsoleManager.getScheduleStrategyManager().getRootPath();
        }
        StringWriter writer = new StringWriter();
        ConsoleManager.getScheduleStrategyManager().printTree(path,writer,"<br/>");
        return writer.getBuffer().toString();
    }

//    @RequestMapping("delete")
    String deleteZKpath(String path){

        StringWriter writer = new StringWriter();
        try {
            ConsoleManager.getScheduleStrategyManager().deleteTree(path);
            writer.write("删除目录 ："+path+" 成功！");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return writer.getBuffer().toString();
    }

}
