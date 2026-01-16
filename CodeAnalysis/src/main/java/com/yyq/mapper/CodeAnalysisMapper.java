package com.yyq.mapper;

import com.yyq.entity.keyword;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;

@Mapper
public interface CodeAnalysisMapper {
    @Select("select * from keyword")
    ArrayList<keyword> findAllKeywords();
}
