package com.aloha.start.domain.common;

import org.apache.ibatis.type.Alias;

import com.aloha.start.domain.Base;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@TableName("board")
@Alias("Board")
public class Board extends Base {

  private String type;
  private String title;
  private String content;
  private Integer views;
  private Integer seq;
  
}
