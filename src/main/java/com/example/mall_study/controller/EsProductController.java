package com.example.mall_study.controller;


import com.example.mall_study.common.api.CommonPage;
import com.example.mall_study.common.api.CommonResult;
import com.example.mall_study.nosql.elasticsearch.document.EsProduct;
import com.example.mall_study.service.EsProductService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 搜索商品管理Controller
 */
@Controller
@Api(tags = {"EsProductController"})
@RequestMapping("/esProduct")
public class EsProductController {
    @Autowired
    private EsProductService esProductService;

    @ApiOperation(value = "导入所有数据库中商品到ES")
    @RequestMapping(value = "/importAll", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Integer> importAllList() {
        int count = esProductService.importAll();
        return CommonResult.success(count);
    }

    @ApiOperation(value = "根据id删除商品")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id",value = "商品id")
    })
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<Object> delete(@RequestParam("id") Long id) {
        esProductService.delete(id);
        return CommonResult.success(null);
    }

    @ApiOperation(value = "根据id批量删除商品")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "ids",value = "商品id")
    })
    @RequestMapping(value = "/delete/batch", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Object> delete(@RequestParam("ids") List<Long> ids) {
        esProductService.delete(ids);
        return CommonResult.success(null);
    }

    @ApiOperation(value = "根据id创建商品")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id",value = "商品id")
    })
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<EsProduct> create(@RequestParam("id") Long id) {
        EsProduct esProduct = esProductService.create(id);
        if (esProduct != null) {
            return CommonResult.success(esProduct);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation(value = "简单搜索")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "keyword",value = "商品名"),
            @ApiImplicitParam(name = "pageNum",value = "页"),
            @ApiImplicitParam(name = "pageSize",value = "一页数据量")
    })
    @RequestMapping(value = "/search/simple", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<EsProduct>> search(@RequestParam(value = "keyword",required = false) String keyword,
                                                      @RequestParam(value = "pageNum",required = false, defaultValue = "0") Integer pageNum,
                                                      @RequestParam(value = "pageSize",required = false, defaultValue = "5") Integer pageSize) {
        Page<EsProduct> esProductPage = esProductService.search(keyword, pageNum, pageSize);
        return CommonResult.success(CommonPage.restPage(esProductPage));
    }
    @ApiOperation(value = "注解编辑DLS语句查询商品")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "name",value = "商品名"),
            @ApiImplicitParam(name = "pageNum",value = "页"),
            @ApiImplicitParam(name = "pageSize",value = "一页数据量")
    })
    @RequestMapping(value = "/searchTest/simple",method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<EsProduct>> searchTest(@RequestParam(value = "name",required = false) String name,
                                                          @RequestParam(value = "pageNum",required = false, defaultValue = "0") Integer pageNum,
                                                          @RequestParam(value = "pageSize",required = false, defaultValue = "5") Integer pageSize) {
        Page<EsProduct> esProducts = esProductService.noteSearch(name, pageNum, pageSize);
        return CommonResult.success(CommonPage.restPage(esProducts));
    }

}