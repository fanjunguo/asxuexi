package cn.asxuexi.tool;


import java.util.Properties;

import javax.mail.Address;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailUtil {

/**
 * @author 张顺
 * @param email 接收方邮箱
 * @param ran 随机验证码
 * 
 * */	 
	 public boolean sendMail(String email,String ran) {

//	String result = "{\"result\":%b,\"msg\":\"%s\"}";
    String from = "asxuexi@asxuexi.cn";        //邮件发送人的邮件地址
    String to = email;                     //需要发送邮件的地址
    String username = "asxuexi@asxuexi.cn";   //邮件发送人的邮件地址
    String password = "fNoDh5V7";             //发件人的邮件密码


    //定义Properties对象，设置环境信息
    Properties props = new Properties();

    //设置邮件服务器的地址
    props.setProperty("mail.smtp.host","smtp.mxhichina.com");//指定的smtp服务器 指定发送邮件的主机smtp.qq.com(QQ)|smtp.163.com(网易)
    props.setProperty("mail.smtp.auth","true");
    props.setProperty("mail.transport.protocol","smtp"); //设置发送邮件使用的协议
    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    //创建Session对象，session对象表示整个邮件的环境信息
    try {
        Session session = Session.getInstance(props);
        //设置不输出调试信息（在控制台）
        session.setDebug(false);
        //Message的实例对象表示一封电子邮件
        MimeMessage message = new MimeMessage(session);
//         //防止成为垃圾邮件，披上outlook的马甲
        message.addHeader("X-Mailer","Microsoft Outlook Express 6.00.2900.2869");
        //设置发件人的地址
        message.setFrom(new InternetAddress(from,"爱上学习", "UTF-8"));
//        设置收件人地址
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(email, email, "UTF-8"));
        //设置主题
        message.setSubject("爱上学习—验证码","UTF-8");
        //设置邮件的文本内容
        String context = "<h3>您好，这是爱上学习的邮箱验证码：%s，如非本人操作请忽略本信息。</h3>";
        context = String.format(context,ran);
        message.setContent(context,"text/html;charset=utf-8");
        //从session的环境中获取发送邮件的对象
        Transport transport = session.getTransport();
        //连接邮件服务器
        transport.connect("smtp.mxhichina.com",465,username,password);
        //设置收件人地址，并发送消息
        transport.sendMessage(message,new Address[]{new InternetAddress(to)});
        transport.close();
        return true;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }


	 }

}
