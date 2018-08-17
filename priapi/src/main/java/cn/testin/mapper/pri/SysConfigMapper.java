package cn.testin.mapper.pri;

import cn.testin.entry.ConfigEntry;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author gaojindan
 * @date 2018/8/15 10:58
 * @des
 */
public interface SysConfigMapper {
    /**
     *
     * @author Gaojindan
     * @date 2018/8/15 14:32 
     * @des 获取配置信息
     * @param: type:resource_alarm资源警告
     * @return 
     */ 
    public List<Map<String,Object>> getConfByType(@Param("type") String type);

    /**
     *
     * @author Gaojindan
     * @date 2018/8/15 14:42
     * @des 保存配置信息
     * @param:
     * @return
     */
    public void saveConfigInfo(@Param("itemList")List<ConfigEntry> itemList);
}
