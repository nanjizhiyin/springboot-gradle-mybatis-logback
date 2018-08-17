package cn.testin.utils;

import com.sun.mail.util.MailSSLSocketFactory;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.security.GeneralSecurityException;
import java.util.LinkedList;
import java.util.Properties;

/**
 * Created by mac on 2018/5/3.
 */

public class MailUtil {

    /*
     * @author: gaojindan
     * @date: 2018/5/3 下午4:55
     * @des: 发送邮件
     * @param: subject:邮件标题,不可为空
     * @param: text:邮件内容,不可为空
     * @param: affix:附件地址
     * @param: affixName:附件名称
     * @return:
     */
    public static Boolean sendMail(String host,
                                   String smtpPort,
                                   String fromUsername,
                                   String fromPassword,
                                   String toUsername,
                                   String subject,
                                   String text,
                                   LinkedList<String> affixPathList,
                                   LinkedList<String> affixNameList){

        try {
//            System.out.println("=======mail======标题:" + subject);
//            System.out.println("=======mail======内容:" + text);
//            System.out.println("=======mail======发送人:" + fromUsername);
//            System.out.println("=======mail======接收人:" + toUsername);

            Properties props = new Properties();
            props.setProperty("mail.smtp.ssl.enable", "true");
            // 发送服务器需要身份验证
            props.setProperty("mail.smtp.auth", "true");
            // 发送邮件协议名称
            props.setProperty("mail.transport.protocol", "smtp");
            // 设置邮件服务器主机名
            props.setProperty("mail.host", host);
            props.setProperty("mail.smtp.port", smtpPort);

            props.put("mail.smtp.starttls.enable","true");
            MailSSLSocketFactory sf = new MailSSLSocketFactory();

            sf.setTrustAllHosts(true);
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.ssl.socketFactory", sf);

            // 设置环境信息
            Session session = Session.getDefaultInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromUsername, fromPassword);
                }
            });
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromUsername));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toUsername));
            message.setSubject(subject);

            if (affixPathList != null
                    && affixNameList != null
                    && affixPathList.size() > 0
                    && affixNameList.size() > 0
                    && affixPathList.size() == affixNameList.size()){
                Multipart multipart = new MimeMultipart();

                // 设置邮件的文本内容
                BodyPart contentPart = new MimeBodyPart();
                contentPart.setText(text);
                multipart.addBodyPart(contentPart);

                for (int i = 0; i < affixPathList.size(); i++){
                    String affixPath = affixPathList.get(i);
                    String affixName = affixNameList.get(i);
                    // 添加附件
                    BodyPart messageBodyPart = new MimeBodyPart();
                    DataSource source = new FileDataSource(affixPath);
                    // 添加附件的内容
                    messageBodyPart.setDataHandler(new DataHandler(source));
                    // 添加附件的标题
                    // 这里很重要，通过下面的Base64编码的转换可以保证你的中文附件标题名在发送时不会变成乱码
                    messageBodyPart.setFileName(MimeUtility.encodeText(affixName));

                    // 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
                    multipart.addBodyPart(messageBodyPart);
                }

                // 将multipart对象放到message中
                message.setContent(multipart);
            }else{
                message.setText(text);
            }
            Transport.send(message);
//            System.out.println("发送成功！");
            return true;
        }
        catch (GeneralSecurityException e) {
            e.printStackTrace();
//            System.out.println("发送失败！");
            return false;
        }
        catch (Exception e) {
            e.printStackTrace();
//            System.out.println("发送失败！");
            return false;
        }
    }
}
