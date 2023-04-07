package com.example.mall_study.mbg.mapper;

import com.example.mall_study.mbg.model.UmsResource;
import com.example.mall_study.mbg.model.UmsResourceExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UmsResourceMapper {
    long countByExample(UmsResourceExample example);

    int deleteByExample(UmsResourceExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UmsResource record);

    int insertSelective(UmsResource record);

    List<UmsResource> selectByExample(UmsResourceExample example);

    UmsResource selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") UmsResource record, @Param("example") UmsResourceExample example);

    int updateByExample(@Param("record") UmsResource record, @Param("example") UmsResourceExample example);

    int updateByPrimaryKeySelective(UmsResource record);

    int updateByPrimaryKey(UmsResource record);
}