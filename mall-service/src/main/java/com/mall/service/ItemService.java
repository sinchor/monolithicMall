package com.mall.service;

import com.mall.pojo.*;
import com.mall.pojo.vo.CommentLevelCountsVO;
import com.mall.pojo.vo.ShopcartVO;
import com.mall.utils.PagedGridResult;

import java.util.List;

public interface ItemService {
    /**
     * query the details based on the Product ID
     * @param id item id
     * @return
     */
    public Items queryItemById(String id);

    /**
     * Query the list of product images by product ID
     * @param itemId
     * @return
     */
    public List<ItemsImg> queryItemImgList(String itemId);

    /**
     * Query product specifications based on product ID
     * @param itemId
     * @return
     */
    public List<ItemsSpec> queryItemSpecList(String itemId);

    /**
     * Query product parameters based on product id
     * @param itemId
     * @return
     */
    public ItemsParam queryItemParam(String itemId);

    /**
     * Query product comments based on product id
     * @param itemId
     * @return
     */
    public CommentLevelCountsVO queryItemCommentsCount(String itemId);

    /**
     * query the comments for this product according to the item id and comment level
     * @param itemId
     * @param level
     * @return
     */
    public PagedGridResult queryAndPagingItemComments(String itemId, Integer level, Integer page, Integer pageSize);

    /**
     * query product items according to keywords
     * @param keyword
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    public PagedGridResult searchItems(String keyword, String sort, Integer page, Integer pageSize);

    public PagedGridResult searchItems(Integer catId, String sort, Integer page, Integer pageSize);

    /**
     * query items in the shop cart according to specification ids.
     * @param specIds
     * @return
     */
    public List<ShopcartVO> queryItemsBySpecIds(String specIds);

    /**
     * 根据商品规格id获取规格对象的具体信息
     * @param specId
     * @return
     */
    public ItemsSpec queryItemSpecById(String specId);

    /**
     * 根据商品id获得商品图片主图url
     * @param itemId
     * @return
     */
    public String queryItemMainImgById(String itemId);

    /**
     * 减少库存
     * @param specId
     * @param buyCounts
     */
    public void decreaseItemSpecStock(String specId, int buyCounts);

}
