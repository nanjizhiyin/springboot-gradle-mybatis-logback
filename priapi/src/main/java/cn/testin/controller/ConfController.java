package cn.testin.controller;

import cn.testin.mapper.pri.SysConfigMapper;
import cn.testin.entry.ConfigEntry;
import cn.testin.entry.result.ResultEntry;
import cn.testin.entry.result.ResultError;
import cn.testin.entry.result.ResultSuccess;
import cn.testin.scheduled.Diskmanager;
import cn.testin.utils.FileUtils;
import cn.testin.utils.OSinfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequestMapping("/admin/sysconfig")
@RestController
public class ConfController {

    private static Logger logger = LogManager.getLogger(ConfController.class);

    @Autowired
    private SysConfigMapper sysConfigMapper;

    /**
     * @Author gaojindan
     * @Date 16:20 2018/8/1
     * @Desc 保存资源配置信息
     * @param resource_alarm 资源告警设置开关 0:关 1:开
     * @param resource_alarm_file 硬盘比例
     * @param resource_alarm_cpu cpu比例
     * @param resource_alarm_mem 内存比例
     *
     * @return 保存结果
     **/
    @RequestMapping(value = "/saveresource", method = RequestMethod.POST)
    private ResultEntry saveresource(@RequestParam(required = false, value = "resource_alarm") Integer resource_alarm,
                                 @RequestParam(required = false, value = "resource_alarm_file") String resource_alarm_file,
                                 @RequestParam(required = false, value = "resource_alarm_cpu") String resource_alarm_cpu,
                                 @RequestParam(required = false, value = "resource_alarm_mem") String resource_alarm_mem) {

        try {

            List<ConfigEntry> insertParameter = new ArrayList<>();
            if (!StringUtils.isEmpty(resource_alarm)){
                //资源告警设置开关 0:关 1:开
                ConfigEntry configEntry = new ConfigEntry();
                configEntry.setConfKey("resource_alarm");
                configEntry.setConfValue(String.valueOf(resource_alarm));
                insertParameter.add(configEntry);
            }
            if (!StringUtils.isEmpty(resource_alarm_file)){
                //硬盘空间比例
                ConfigEntry configEntry = new ConfigEntry();
                configEntry.setConfKey("resource_alarm_file");
                configEntry.setConfValue(resource_alarm_file);
                insertParameter.add(configEntry);
            }
            if (!StringUtils.isEmpty(resource_alarm_cpu)){
                //cpu比例
                ConfigEntry configEntry = new ConfigEntry();
                configEntry.setConfKey("resource_alarm_cpu");
                configEntry.setConfValue(resource_alarm_cpu);
                insertParameter.add(configEntry);
            }
            if (!StringUtils.isEmpty(resource_alarm_mem)){
                //内存比例
                ConfigEntry configEntry = new ConfigEntry();
                configEntry.setConfKey("resource_alarm_mem");
                configEntry.setConfValue(resource_alarm_mem);
                insertParameter.add(configEntry);
            }
            sysConfigMapper.saveConfigInfo(insertParameter);

            ResultSuccess resultSuccess = new ResultSuccess("保存成功",null);
            return resultSuccess;
        } catch (Exception ex) {
            logger.error("保存配置信息出错", ex);
            return new ResultError(500,"保存配置信息出错");
        }
    }

    /**
     * @Author gaojindan
     * @Date 16:20 2018/8/1
     * @Desc 保存证书配置信息
     * @param license_alarm 证书到期1个月前提醒开关 0:关 1:开
     *
     * @return 保存结果
     **/
    @RequestMapping(value = "/savelicense", method = RequestMethod.POST)
    private ResultEntry savelicense(@RequestParam(required = false, value = "license_alarm") Integer license_alarm) {

        try {
            List<ConfigEntry> insertParameter = new ArrayList<>();
            if (!StringUtils.isEmpty(license_alarm)){
                //到期1个月前提醒

                ConfigEntry configEntry = new ConfigEntry();
                configEntry.setConfKey("license_alarm");
                configEntry.setConfValue(String.valueOf(license_alarm));
                insertParameter.add(configEntry);
            }

            sysConfigMapper.saveConfigInfo(insertParameter);

            ResultSuccess resultSuccess = new ResultSuccess("保存成功",null);
            return resultSuccess;
        } catch (Exception ex) {
            logger.error("保存配置信息出错", ex);
            return new ResultError(500,"保存配置信息出错");
        }
    }

    /**
     *
     * @author Gaojindan
     * @date 2018/8/17 10:53
     * @des 保存基本配置信息
     * @param basic_siteName 网站名称
     * @param basic_alarm_executionTime 执行时间
     * @param basic_alarm_forwardTime 删除N天前的数据
     * @return 执行结束
     */
    @RequestMapping(value = "/savebasic", method = RequestMethod.POST)
    private ResultEntry saveBasic(@RequestParam(required = false, value = "basic_siteName") String basic_siteName,
                                  @RequestParam(required = false, value = "basic_alarm_executionTime") Integer basic_alarm_executionTime,
                                  @RequestParam(required = false, value = "basic_alarm_forwardTime") Integer basic_alarm_forwardTime) {

        try {

            List<ConfigEntry> insertParameter = new ArrayList<>();
            if (!StringUtils.isEmpty(basic_siteName)){
                //资源告警设置开关 0:关 1:开
                ConfigEntry configEntry = new ConfigEntry();
                configEntry.setConfKey("basic_siteName");
                configEntry.setConfValue(String.valueOf(basic_siteName));
                insertParameter.add(configEntry);
            }

            if (!StringUtils.isEmpty(basic_alarm_executionTime) && !StringUtils.isEmpty(basic_alarm_forwardTime)){
                //提醒频率

                ConfigEntry configEntry = new ConfigEntry();
                configEntry.setConfKey("basic_alarm_executionTime");
                configEntry.setConfValue(String.valueOf(basic_alarm_executionTime));
                insertParameter.add(configEntry);

                ConfigEntry configEntry2 = new ConfigEntry();
                configEntry2.setConfKey("basic_alarm_forwardTime");
                configEntry2.setConfValue(String.valueOf(basic_alarm_forwardTime));
                insertParameter.add(configEntry2);

                //执行定时任务
                Diskmanager.startClear(basic_alarm_forwardTime);
            }
            sysConfigMapper.saveConfigInfo(insertParameter);

            ResultSuccess resultSuccess = new ResultSuccess("保存成功",null);
            return resultSuccess;
        } catch (Exception ex) {
            logger.error("保存基本配置信息出错", ex);
            return new ResultError(500,"保存基本配置信息出错");
        }
    }

    /**
     *
     * @author Gaojindan
     * @date 2018/8/9 18:17
     * @des 上传Logo并保存
     * @param file 文件
     * @return
     */
    @RequestMapping(value = "/upsitelogo", method = RequestMethod.POST)
    private ResultEntry upfile(@RequestParam(value = "file") MultipartFile file) {

        try {
            String path = "."+File.separator+"temp";
            //取文件格式
            String prefix = FileUtils.getExtensionName(file.getOriginalFilename());
            //保存文件
            FileUtils.saveFileToPath(file,path,"logo."+prefix);

            return new ResultError(200,"上传Logo成功");
        } catch (IOException e) {
            e.printStackTrace();
            return new ResultError(500,"上传Logo失败");
        }

    }

    /**
     * @Author gaojindan
     * @Date 16:20 2018/8/1
     * @Desc 获取配置信息
     * @Param type:硬盘比例 cpu:cpu比例 mem:内存比例
     * @return 保存结果
     **/
    @RequestMapping(value = "/getconf", method = RequestMethod.GET)
    private ResultEntry getconf(@RequestParam(required = false, value = "type") String type ) {

        try {
            List<Map<String,Object>> resultList = new ArrayList<>();
            resultList = sysConfigMapper.getConfByType(type);
            ResultSuccess resultSuccess = new ResultSuccess(resultList);
            return resultSuccess;
        } catch (Exception ex) {
            logger.error("获取配置信息失败", ex);
            return new ResultError(500,"获取配置信息失败");
        }
    }
}
