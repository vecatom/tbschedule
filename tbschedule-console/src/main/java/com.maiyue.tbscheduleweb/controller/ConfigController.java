package com.maiyue.tbscheduleweb.controller;

import com.maiyue.tbscheduleweb.ajax.AjaxResult;
import com.maiyue.tbscheduleweb.modul.ZookeeperConfig;
import com.taobao.pamirs.schedule.ConsoleManager;
import com.taobao.pamirs.schedule.zk.ZKManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Properties;

@Controller
public class ConfigController {

    @RequestMapping("/toConfig")
    public String getConfig(ModelMap mv){
        try {
            Properties p = ConsoleManager.loadConfig();

            ZookeeperConfig config = new ZookeeperConfig();
            config.setZkConnectString(p.getProperty(ZKManager.keys.zkConnectString.toString()));
            config.setZkSessionTimeout(p.getProperty(ZKManager.keys.zkSessionTimeout.toString()));
            config.setRootPath(p.getProperty(ZKManager.keys.rootPath.toString()));
            config.setUserName(p.getProperty(ZKManager.keys.userName.toString()));
            config.setPassword(p.getProperty(ZKManager.keys.password.toString()));

            mv.put("config",config);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "/config/edit";
    }

    @RequestMapping("/config")
    @ResponseBody
    public AjaxResult setConfig(ZookeeperConfig config){

        Properties p = new Properties();
        p.setProperty(ZKManager.keys.zkConnectString.toString(), config.getZkConnectString());
        p.setProperty(ZKManager.keys.rootPath.toString(), config.getRootPath());
        p.setProperty(ZKManager.keys.userName.toString(), config.getUserName());
        p.setProperty(ZKManager.keys.password.toString(), config.getPassword());
        p.setProperty(ZKManager.keys.zkSessionTimeout.toString(),config.getZkSessionTimeout());
        try {
            ConsoleManager.saveConfigInfo(p);
        } catch (Exception e) {
            e.printStackTrace();
            // TODO 出现异常，配置不成功
//            response.sendRedirect("config.jsp?error=" + e.getMessage());
            return AjaxResult.filed(500);
        }
        return AjaxResult.sucess();
    }
}
