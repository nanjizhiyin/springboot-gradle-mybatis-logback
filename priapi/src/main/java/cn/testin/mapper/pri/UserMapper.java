package cn.testin.mapper.pri;

import cn.testin.entry.UserEntry;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author gaojindan
 * @date 2018/8/15 17:28
 * @des
 */
@Mapper
public interface UserMapper {

    /**
     *
     * @author Gaojindan
     * @date 2018/8/15 17:35
     * @des 添加用户
     * @param: user:用户信息
     * @return
     */
    public void addUser(@Param("user") UserEntry user);

    /**
     *
     * @author Gaojindan
     * @date 2018/8/15 18:09
     * @des 修改用户资料
     * @param: user:用户信息
     * @return
     */
    public void updateUser(@Param("user") UserEntry user);

    /**
     *
     * @author Gaojindan
     * @date 2018/8/15 19:01
     * @des 搜索用户
     * @param:
     * @return
     */
    public List<UserEntry> search(
            @Param("start") Integer start,
            @Param("size") Integer size,
            @Param("status") Integer status,
            @Param("email") String email,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime);
}
