package dingding.bot.pojo;

import dingding.bot.pojo.TextContent;
import lombok.Data;

/**
    * https://open.dingtalk.com/document/orgapp/custom-robots-send-group-messages
    * conversationId
    * chatbotCorpId
    * chatbotUserId
    * msgId
    * senderNick：发送人
    * isAdmin
    * snederStaffId
    * sessionWebhookExpiredTime：会话时间
    * createAt
    * senderCorpId
    * conversationType
    * senderId
    * sessionWebhook:回调地址
    * robotCode
    * msgtype：发送消息类型
    *      text:{content：回答}
    *      at:{
    *          isAtAll:是否@所有人
    *          atMobile:被@的群成员手机号，String[]
    *          atUserIds：被@的群成员userId，String[]
    *      }
    *      link：{  可发媒体类型信息，比较麻烦具体看文旦——https://open.dingtalk.com/document/orgapp/upload-media-files
    *          messageUrl：点击消息跳转的URL
    *          title
    *          picUrl：链接消息内的图片地址，建议使用上传媒体文件接口获取
    *          text：链接消息的内容
    *      }
    *      markdown:{
    *          text
    *          title
    *      }
    *      actionCard——了解卡片相关文档
    *      feedCard
*/

@Data
public class Payload {
    //当前发送人名称
    private String senderNick;

    //是否管理员
    private Boolean isAdmin;

    //webhook缓存存在时间
    private Long sessionWebhookExpiredTime;

    //webhook的session地址
    private String sessionWebhook;

    //@机器人发送的消息内容
    private TextContent text;


    //发送消息类型
    private String msgtype;
}



