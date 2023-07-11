package com.mall.service;

import com.mall.pojo.*;
import com.mall.pojo.vo.CommentLevelCountsVO;
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
}
