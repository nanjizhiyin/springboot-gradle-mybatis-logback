package cn.testin.controller;

import cn.testin.entry.result.ResultEntry;
import cn.testin.entry.result.ResultError;
import cn.testin.entry.result.ResultSuccess;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import cn.testin.utils.FileUtils;
import cn.testin.utils.OSinfo;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author gaojindan
 * @date 2018/8/9 17:14
 * @des
 */
@RequestMapping("/admin/license")
@RestController
public class LicenseController {

    private static Logger logger = LogManager.getLogger(LicenseController.class);

    /**
     *
     * @author Gaojindan
     * @date 2018/8/9 18:17
     * @des 接收认证文件,处理后保存到数据库中
     * @param: file:文件
     * @return
     */
    @RequestMapping(value = "/uplicense", method = RequestMethod.POST)
    private ResultEntry uplicense(@RequestParam(value = "file",required = true) MultipartFile file) {
        //form表单提交的参数测试为String类型

        try {

            String path = "."+File.separator+"temp";
            //取文件格式
            String prefix = FileUtils.getExtensionName(file.getOriginalFilename());
            //保存文件
            FileUtils.saveFileToPath(file,path,"license."+prefix);

            return new ResultError(200,"保存文件成功");
        } catch (IOException e) {
            e.printStackTrace();
            return new ResultError(500,"处理接收失败");
        }

    }

    /**
     *
     * @author Gaojindan
     * @date 2018/8/9 18:21
     * @des 读取配置信息
     * @param:
     * @return
     */
    @RequestMapping(value = "/info",method = RequestMethod.GET)
    private ResultEntry getconfi(){
        HashMap<String,String> resultMap = new HashMap<>();
        resultMap.put("version","按时间版");
        resultMap.put("No","23234-34FSDFAS-ASDF-45G45");
        resultMap.put("level","高级");
        resultMap.put("endTime","2020-05-05");
        resultMap.put("nowTime","2022-05-05");
        return new ResultSuccess(resultMap);
//        return new ResultError(200,"保存文件成功");
    } 

    @RequestMapping(value = "/export",method = RequestMethod.GET)
    private ResultEntry exportconf(HttpServletRequest request){
        try {
            String tmpPath = "./tmp/text.txt";
            FileUtils.createFile("23234234234",tmpPath);
            //计算地址
            StringBuffer tmpUrl = request.getRequestURL();
            String tempContextUrl = tmpUrl.delete(tmpUrl.length() - request.getRequestURI().length(), tmpUrl.length()).toString();
            String callback = tempContextUrl + "/tmp/text.txt";
            return new ResultSuccess(callback);
        }catch (IOException e){
            e.printStackTrace();
            return new ResultError(500,"服务器错误");
        }

    }
}
