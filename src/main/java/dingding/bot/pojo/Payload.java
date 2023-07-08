package dingding.bot.pojo;

import dingding.bot.pojo.TextContent;
import lombok.Data;

@Data
public class Payload {
//    private String conversationId;

    //@的用户数及用户id
//    private String[] atUsers;

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



