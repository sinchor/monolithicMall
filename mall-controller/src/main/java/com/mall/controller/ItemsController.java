package com.mall.controller;

import com.mall.config.I18nMessage;
import com.mall.pojo.Items;
import com.mall.pojo.ItemsImg;
import com.mall.pojo.ItemsParam;
import com.mall.pojo.ItemsSpec;
import com.mall.pojo.vo.CommentLevelCountsVO;
import com.mall.pojo.vo.ItemInfoVO;
import com.mall.pojo.vo.ShopcartVO;
import com.mall.service.ItemService;
import com.mall.utils.MallJSONResult;
import com.mall.utils.PagedGridResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "商品信息展示的相关接口")
@RestController
@RequestMapping("/items")
public class ItemsController {
    final ItemService itemService;
    final static Integer DEFAULT_COMMENT_SIZE = 10;
    final static Integer DEFAULT_SEARCH_SIZE = 20;

    @Autowired
    public ItemsController(ItemService itemService) {
        this.itemService = itemService;
    }

    @Operation(summary = "查询商品详情")
    @GetMapping("/info/{itemId}")
    public MallJSONResult info(@PathVariable("itemId") String itemId){
        if (StringUtils.isBlank(itemId)) {
            return MallJSONResult.errorMsg(I18nMessage.message("item.error.itemId_null"));
        }
        Items items = itemService.queryItemById(itemId);
        List<ItemsImg> itemsImgs = itemService.queryItemImgList(itemId);
        ItemsParam itemsParam = itemService.queryItemParam(itemId);
        List<ItemsSpec> itemsSpecs = itemService.queryItemSpecList(itemId);

        ItemInfoVO itemInfoVO = new ItemInfoVO();
        itemInfoVO.setItem(items);
        itemInfoVO.setItemParams(itemsParam);
        itemInfoVO.setItemImgList(itemsImgs);
        itemInfoVO.setItemSpecList(itemsSpecs);

        return MallJSONResult.ok(itemInfoVO);
    }

    @Operation(summary = "查询用户评价信息")
    @GetMapping("/commentLevel")
    public MallJSONResult commentLevel(@RequestParam String itemId) {
        if (StringUtils.isBlank(itemId)) {
            return MallJSONResult.errorMsg(I18nMessage.message("item.error.itemId_null"));
        }
        CommentLevelCountsVO countsVO = itemService.queryItemCommentsCount(itemId);
        return MallJSONResult.ok(countsVO);
    }

    @Operation(summary = "查询商品分类")
    @GetMapping("/comments")
    public MallJSONResult comments(
            @Parameter(description = "item id to be queried", required = true)
            @RequestParam(value = "itemId", required = true) String itemId,
            @Parameter(description = "comment level (good, normal, bad)", required = false)
            @RequestParam(value = "level", required = false) Integer level,
            @Parameter(description = "current page", required = false)
            @RequestParam(required = false) Integer page,
            @Parameter(description = "comment record num per page", required = false)
            @RequestParam(required = false) Integer pageSize){

        if (StringUtils.isBlank(itemId)) {
            return MallJSONResult.errorMsg(I18nMessage.message("item.error.itemId_null"));
        }

        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = DEFAULT_COMMENT_SIZE;
        }
        PagedGridResult result = itemService.queryAndPagingItemComments(itemId, level, page, pageSize);
        return MallJSONResult.ok(result);
    }

    @Operation(summary = "搜索商品列表")
    @GetMapping("/search")
    public MallJSONResult search(@Parameter(required = true, description = "keywords")
                                     @RequestParam(required = true) String keywords,
                                 @Parameter(description = "sort method", required = false)
                                 @RequestParam(required = false) String sort,
                                 @Parameter(description = "current page", required = false)
                                     @RequestParam(required = false) Integer page,
                                 @Parameter(description = "comment record num per page", required = false)
                                     @RequestParam(required = false) Integer pageSize){

        if (StringUtils.isBlank(keywords)) {
            return MallJSONResult.errorMsg(I18nMessage.message("item.error.keyword_null"));
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = DEFAULT_SEARCH_SIZE;
        }

        PagedGridResult pagedGridResult = itemService.searchItems(keywords, sort, page, pageSize);
        return MallJSONResult.ok(pagedGridResult);
    }
    @Operation(summary = "通过商品ID搜索商品列表")
    @GetMapping("/catItems")
    public MallJSONResult search(@Parameter(required = true, description = "item level")
                                 @RequestParam(required = true) Integer catId,
                                 @Parameter(description = "sort method", required = false)
                                 @RequestParam(required = false) String sort,
                                 @Parameter(description = "current page", required = false)
                                 @RequestParam(required = false) Integer page,
                                 @Parameter(description = "comment record num per page", required = false)
                                 @RequestParam(required = false) Integer pageSize){

        if (catId == null) {
            return MallJSONResult.errorMsg(null);
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = DEFAULT_SEARCH_SIZE;
        }

        PagedGridResult pagedGridResult = itemService.searchItems(catId, sort, page, pageSize);
        return MallJSONResult.ok(pagedGridResult);
    }

    // 用于用户长时间未登录网站，刷新购物车中的数据（主要是商品价格），类似京东淘宝
    @Operation(summary = "根据商品规格ids查找最新的商品数据")
    @GetMapping("/refresh")
    public MallJSONResult refresh(
            @Parameter(required = true)
            @RequestParam String itemSpecIds) {

        if (StringUtils.isBlank(itemSpecIds)) {
            return MallJSONResult.ok();
        }

        List<ShopcartVO> list = itemService.queryItemsBySpecIds(itemSpecIds);

        return MallJSONResult.ok(list);
    }
}
