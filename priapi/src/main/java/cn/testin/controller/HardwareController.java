package cn.testin.controller;

import cn.testin.entry.result.ResultEntry;
import cn.testin.entry.result.ResultError;
import cn.testin.entry.result.ResultSuccess;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hyperic.sigar.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;
import java.util.*;

@RequestMapping("/admin/hardware")
@RestController
public class HardwareController {

    private static Logger logger = LogManager.getLogger(ConfController.class);

    /**
     * @Author gaojindan
     * @Date 16:19 2018/8/1
     * @Desc 读取硬件信息
     * @Param
     * @return
     **/
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    private ResultEntry info() {

        try {
            DecimalFormat df = new DecimalFormat("0.00");

            String cpuUseFloat = null;
            String memUseFloat = null;
            String fileUseFloat = null;

            Sigar sigar = new Sigar();

            //计算CPU使用
            CpuPerc cpuList[] = sigar.getCpuPercList();
            //总数
            double tmpCombined = 0.0;
            for (int i = 0; i < cpuList.length; i++) {// 不管是单块CPU还是多CPU都适用
                CpuPerc cpuPerc = cpuList[i];
                double combined = cpuPerc.getCombined();
                tmpCombined += combined;
            }
            cpuUseFloat = df.format((float)tmpCombined);

            //计算内存使用
            Mem mem = sigar.getMem();
            double memUse = (mem.getUsed()*1.0)/ mem.getTotal()*100;
            memUseFloat = df.format((float)memUse);

            //计算硬盘使用
            //总空间
            double tmpUsePercent = 0.0;
            FileSystem fslist[] = sigar.getFileSystemList();
            for (int i = 0; i < fslist.length; i++) {
                FileSystem fs = fslist[i];
                FileSystemUsage usage = sigar.getFileSystemUsage(fs.getDirName());
                switch (fs.getType()) {
                    case 0: // TYPE_UNKNOWN ：未知
                        break;
                    case 1: // TYPE_NONE
                        break;
                    case 2: // TYPE_LOCAL_DISK : 本地硬盘
                        double usePercent = usage.getUsePercent() * 100D;
                        tmpUsePercent += usePercent;
                        break;
                }
            }
            tmpUsePercent = tmpUsePercent/fslist.length;
            fileUseFloat = df.format((float)tmpUsePercent);


            //返回数据
            HashMap<String,String> resultMap = new HashMap<>();
            resultMap.put("file",fileUseFloat);
            resultMap.put("cpu",cpuUseFloat);
            resultMap.put("mem",memUseFloat);
            ResultSuccess resultSuccess = new ResultSuccess(resultMap);
            return resultSuccess;
        }
        catch (SigarException e){
            e.printStackTrace();
            logger.debug("Sigar异常");
            return new ResultError(500,"服务器错误");
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResultError(500,"服务器错误");
        }
    }

}
