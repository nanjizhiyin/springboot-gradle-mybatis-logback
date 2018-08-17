package cn.testin.controller;

import cn.testin.entry.UserEntry;
import cn.testin.entry.result.ResultEntry;
import cn.testin.entry.result.ResultError;
import cn.testin.entry.result.ResultSuccess;
import cn.testin.mapper.pri.UserMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * @author gaojindan
 * @date 2018/8/15 17:25
 * @des 列表查询(状态,邮箱(模糊搜索),创建时间(开始和结束),分页,倒序),用户申请审核.
 */

@RequestMapping("/admin/user")
@RestController
public class UserController {

    private static Logger logger = LogManager.getLogger(UserController.class);

    @Autowired
    private UserMapper userMapper;

    /**
     *
     * @author Gaojindan
     * @date 2018/8/15 18:10
     * @des 审核
     * @param examine:审核状态 0:待审核 1:通过 2:拒绝
     * @return
     */
    @RequestMapping(value = "examine", method = RequestMethod.PUT)
    private ResultEntry examine(@RequestParam(value = "uid") Long uid,
                                @RequestParam(value = "examine") Integer examine) {

        try {
            UserEntry userEntry = new UserEntry();
            userEntry.setUid(uid);
            userEntry.setExamine(examine);
            userMapper.updateUser(userEntry);

            ResultSuccess resultSuccess = new ResultSuccess("审核成功",null);
            return resultSuccess;
        } catch (Exception ex) {
            logger.error("审核失败", ex);
            return new ResultError(500,"审核失败");
        }
    }
    /**
     *
     * @author Gaojindan
     * @date 2018/8/16 10:40
     * @des 搜索用户
     * @param page:页码
     * @param pageSize:每页数量
     * @param status:状态 0:停用 1:启用
     * @param email:邮箱
     * @param startTime:开始时间
     * @param endTime:结束时间
     * @return 用户列表
     */

    @RequestMapping(value = "search", method = RequestMethod.GET)
    private ResultEntry search(
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "pageSize") Integer pageSize,
            @RequestParam(required = false, value = "status") Integer status,
            @RequestParam(required = false, value = "email") String email,
            @RequestParam(required = false, value = "startTime") String startTime,
            @RequestParam(required = false, value = "endTime") String endTime){
        try {
            List<UserEntry> userEntryList = userMapper.search(
                    (page-1)*pageSize,
                    pageSize,
                    status,
                    email,
                    startTime,
                    endTime);

            Map<String,Object> resultMap = new HashMap<>();
            resultMap.put("total",201);
            resultMap.put("list",userEntryList);
            ResultSuccess resultSuccess = new ResultSuccess(resultMap);
            return resultSuccess;
        } catch (Exception ex) {
            logger.error("获取配置信息失败", ex);
            return new ResultError(500,"获取配置信息失败");
        }
    }








    /**
     *
     * @author Gaojindan
     * @date 2018/8/15 17:51
     * @des 添加用户
     * @param username:用户名
     * @param password:密码
     * @param job:职位
     * @param email:邮箱
     * @param mobile:手机号
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    private ResultEntry add(@RequestParam(value = "username") String username,
                                @RequestParam(value = "password") String password,
                                @RequestParam(required = false, value = "email") String email,
                                @RequestParam(required = false, value = "job") String job,
                                @RequestParam(required = false, value = "mobile") String mobile) {

        try {
            Long uid = new Date().getTime();
            UserEntry userEntry = new UserEntry();
            userEntry.setUid(uid);
            userEntry.setEmail(email);
            userEntry.setJob(job);
            userEntry.setUsername(username);
            userEntry.setPassword(password);
            userEntry.setMobile(mobile);
            userMapper.addUser(userEntry);

            ResultSuccess resultSuccess = new ResultSuccess("成功");
            return resultSuccess;
        } catch (Exception ex) {
            logger.error("获取配置信息失败", ex);
            return new ResultError(500,"获取配置信息失败");
        }
    }
    /**
     *
     * @author Gaojindan
     * @date 2018/8/15 18:10
     * @des 重置用户密码
     * @param uid:用户id
     * @return
     */
    @RequestMapping(value = "resetpassword", method = RequestMethod.PUT)
    private ResultEntry resetpassword(@RequestParam(value = "uid") Long uid) {

        try {
            String password = "123456";
            UserEntry userEntry = new UserEntry();
            userEntry.setUid(uid);
            userEntry.setPassword(password);
            userMapper.updateUser(userEntry);

            Map<String,String> resultMap = new HashMap<>();
            resultMap.put("password",password);
            ResultSuccess resultSuccess = new ResultSuccess(resultMap);
            return resultSuccess;
        } catch (Exception ex) {
            logger.error("获取配置信息失败", ex);
            return new ResultError(500,"获取配置信息失败");
        }
    }

}
