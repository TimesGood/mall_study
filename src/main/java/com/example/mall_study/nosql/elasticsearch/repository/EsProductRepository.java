package com.example.mall_study.nosql.elasticsearch.repository;

import com.example.mall_study.nosql.elasticsearch.document.EsProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * 商品ES操作类
 */
public interface EsProductRepository extends ElasticsearchRepository<EsProduct, Long>{
    /**
     * 搜索查询，使用内置已经封装好的
     * @param name              商品名称
     * @param subTitle          商品标题
     * @param keywords          商品关键字
     * @param page              分页信息
     * @return
     */
    Page<EsProduct> findByNameOrSubTitleOrKeywords(String name, String subTitle, String keywords, Pageable page);

    /**
     * 通过注解编辑DLS语法查询
     * @param name 商品名
     * @param page 分页
     * @return
     */
    @Query(value="{\"term\":{\"name\":\"?0\"}}")
    Page<EsProduct> findByBrandName(String name,Pageable page);

}
