package com.yyq.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class keyword {
    private Integer id;
    private String content;
    private String polarity;
}
