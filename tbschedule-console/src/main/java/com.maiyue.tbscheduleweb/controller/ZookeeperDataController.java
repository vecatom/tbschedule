package com.maiyue.tbscheduleweb.controller;

import com.maiyue.tbscheduleweb.ajax.AjaxResult;
import com.taobao.pamirs.schedule.ConsoleManager;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.StringWriter;

@Controller
@RequestMapping("zkData")
public class ZookeeperDataController {


    @RequestMapping("query")
    public String queryZKTree(String path, ModelMap mp) throws Exception {

        if(StringUtils.isEmpty(path)){
            path=ConsoleManager.getScheduleStrategyManager().getRootPath();
        }
        StringWriter writer = new StringWriter();
        ConsoleManager.getScheduleStrategyManager().printTree(path,writer,"<br/>");
//        return writer.getBuffer().toString();

        mp.put("data",writer.getBuffer().toString());
        return "/zookeeper/zookeeper";
    }

    @RequestMapping("delete")
    @ResponseBody
    public AjaxResult deleteZKpath(String path){

//        StringWriter writer = new StringWriter();
        try {
            ConsoleManager.getScheduleStrategyManager().deleteTree(path);
//            writer.write("删除目录 ："+path+" 成功！");
            AjaxResult sucess = AjaxResult.sucess();
            return sucess;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return AjaxResult.filed(null);
    }

}
