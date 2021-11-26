package com.example.mall_study.service;



import com.example.mall_study.mbg.model.PmsBrand;

import java.util.List;

/**
 * PmsBrandService
 */
public interface PmsBrandService {
    /**
     * 获取表中所有数据
     * @return
     */
    List<PmsBrand> listAllBrand();

    /**
     * 添加一条数据
     * @param brand 添加的参数
     * @return 受影响的条目数量
     */
    int createBrand(PmsBrand brand);

    /**
     * 更新某条目
     * @param id 目标id
     * @param brand 更新参数
     * @return 受影响的条目数量
     */
    int updateBrand(Long id, PmsBrand brand);

    /**
     * 删除某条数据
     * @param id 目标id
     * @return 受影响的条目数量
     */
    int deleteBrand(Long id);

    /**
     * 分页查找
     * @param pageNum 页码
     * @param pageSize 显示数量
     * @return
     */
    List<PmsBrand> listBrand(int pageNum, int pageSize);

    /**
     * 根据id查找某条数据
     * @param id 目标id
     * @return
     */
    PmsBrand getBrand(Long id);
}
