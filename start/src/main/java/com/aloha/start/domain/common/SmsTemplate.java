package com.aloha.start.domain.common;

import org.apache.ibatis.type.Alias;

import com.aloha.start.domain.Base;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
@TableName("sms_template")
@Alias("SmsTemplate")
public class SmsTemplate extends Base {
    private String title;       // 템플릿 제목
    private String content;     // 문자 내용
}
