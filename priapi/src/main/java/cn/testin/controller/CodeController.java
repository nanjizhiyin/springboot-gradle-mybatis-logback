package cn.testin.controller;

import cn.testin.entry.result.ResultEntry;
import cn.testin.entry.result.ResultError;
import cn.testin.entry.result.ResultSuccess;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

/**
 * @author gaojindan
 * @date 2018/8/16 15:47
 * @des
 */
@RequestMapping("/admin/code")
@RestController
public class CodeController {

    private static Logger logger = LogManager.getLogger(CodeController.class);
    /**
     *
     * @author Gaojindan
     * @date 2018/8/16 15:51
     * @des 生成验证码
     * @param
     * @return
     */
    @RequestMapping("getCode")
    public void getCode(HttpSession session,HttpServletResponse resp) {
        // 验证码图片的宽度。
        int width = 80;

        // 验证码图片的高度。
        int height = 20;

        // 验证码字符个数
        int codeCount = 4;

        int x = 0;

        // 字体高度
        int fontHeight;

        int codeY;

        char[] codeSequence = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
                'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
                'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
        x = width / (codeCount + 1);
        fontHeight = height - 2;
        codeY = height - 4;
        BufferedImage buffImg = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = buffImg.createGraphics();

        // 创建一个随机数生成器类
        Random random = new Random();

        // 将图像填充为白色
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        // 创建字体，字体的大小应该根据图片的高度来定。
        Font font = new Font("Fixedsys", Font.PLAIN, fontHeight);
        // 设置字体。
        g.setFont(font);

        // 画边框。
//        g.setColor(Color.BLACK);
//        g.drawRect(0, 0, width - 1, height - 1);

        // 随机产生160条干扰线，使图象中的认证码不易被其它程序探测到。
        g.setColor(Color.BLACK);
        for (int i = 0; i < 1; i++) {
            int x2 = random.nextInt(width);
            int y2 = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            g.drawLine(x2, y2, x + xl, y2 + yl);
        }

        // randomCode用于保存随机产生的验证码，以便用户登录后进行验证。
        StringBuffer randomCode = new StringBuffer();

        int red = 0, green = 0, blue = 0;

        // 随机产生codeCount数字的验证码。
        for (int i = 0; i < codeCount; i++) {
            // 得到随机产生的验证码数字。
            String strRand = String.valueOf(codeSequence[random.nextInt(36)]);
            // 产生随机的颜色分量来构造颜色值，这样输出的每位数字的颜色值都将不同。
            red = random.nextInt(255);
            green = random.nextInt(255);
            blue = random.nextInt(255);

            // 用随机产生的颜色将验证码绘制到图像中。
            g.setColor(new Color(red, green, blue));
            g.drawString(strRand, (i + 1) * x, codeY);

            // 将产生的四个随机数组合在一起。
            randomCode.append(strRand);
        }
        // 将四位数字的验证码保存到Session中。
        session.setAttribute("validateCode", randomCode.toString());
        ServletOutputStream sos;
        try {
            sos = resp.getOutputStream();
            ImageIO.write(buffImg, "jpeg", sos);
            sos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     *
     * @author Gaojindan
     * @date 2018/8/16 16:19
     * @des 验证验证码
     * @param
     * @return
     */
    @RequestMapping(value = "checkCode")
    public ResultEntry checkCode(@RequestParam(value = "code") String code,
                                 HttpSession session){

        try {
            String validateCode = (String)session.getAttribute("validateCode");
            if (code.equalsIgnoreCase(validateCode)) {
                return new ResultSuccess("验证通过",null);
            } else {
                return new ResultError(400,"输入的验证码有误");
            }
        } catch (Exception e) {
            logger.debug("验证失败:"+e.toString());
            return new ResultError(500,"服务器错误");
        }
    }

}

