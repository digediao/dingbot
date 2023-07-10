package dingding.bot.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;


@TableName("azure_openai_history")
@Data
public class azure_openai_history implements Serializable {
    private Integer id;

    private String sessionWebhook;

    private String question;

    private String answer;

}
