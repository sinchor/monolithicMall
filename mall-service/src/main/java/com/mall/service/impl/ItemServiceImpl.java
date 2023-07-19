package com.mall.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall.enums.CommentLevel;
import com.mall.enums.YesOrNo;
import com.mall.mapper.*;
import com.mall.pojo.*;
import com.mall.pojo.vo.CommentLevelCountsVO;
import com.mall.pojo.vo.ItemCommentVO;
import com.mall.pojo.vo.SearchItemsVO;
import com.mall.pojo.vo.ShopcartVO;
import com.mall.service.ItemService;
import com.mall.service.utils.ServiceHelper;
import com.mall.utils.DesensitizationUtil;
import com.mall.utils.PagedGridResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

@Service
public class ItemServiceImpl implements ItemService {

    final ItemsMapperExt itemsMapper;
    final ItemsImgMapper itemsImgMapper;
    final ItemsSpecMapper itemsSpecMapper;
    final ItemsParamMapper itemsParamMapper;
    final ItemsCommentsMapper itemsCommentsMapper;

    @Autowired
    public ItemServiceImpl(ItemsMapperExt itemsMapper, ItemsImgMapper itemsImgMapper,
                           ItemsSpecMapper itemsSpecMapper, ItemsParamMapper itemsParamMapper,
                           ItemsCommentsMapper itemsCommentsMapper) {
        this.itemsMapper = itemsMapper;
        this.itemsImgMapper = itemsImgMapper;
        this.itemsSpecMapper = itemsSpecMapper;
        this.itemsParamMapper = itemsParamMapper;
        this.itemsCommentsMapper = itemsCommentsMapper;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Items queryItemById(String id) {
        return itemsMapper.selectByPrimaryKey(id);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemsImg> queryItemImgList(String itemId) {
        ItemsImgExample example = new ItemsImgExample();
        ItemsImgExample.Criteria criteria = example.createCriteria();
        criteria.andItemIdEqualTo(itemId);
        List<ItemsImg> itemsImgs = itemsImgMapper.selectByExample(example);
        return itemsImgs;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemsSpec> queryItemSpecList(String itemId) {
        ItemsSpecExample example = new ItemsSpecExample();
        ItemsSpecExample.Criteria criteria = example.createCriteria();
        criteria.andItemIdEqualTo(itemId);
        List<ItemsSpec> itemsSpecs = itemsSpecMapper.selectByExample(example);
        return itemsSpecs;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ItemsParam queryItemParam(String itemId) {
        ItemsParamExample itemsParamExample = new ItemsParamExample();
        ItemsParamExample.Criteria criteria = itemsParamExample.createCriteria();
        criteria.andItemIdEqualTo(itemId);
        List<ItemsParam> itemsParams = itemsParamMapper.selectByExample(itemsParamExample);
        if (itemsParams == null || itemsParams.isEmpty()) {
            return null;
        }
        return itemsParams.get(0);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public CommentLevelCountsVO queryItemCommentsCount(String itemId) {
        CommentLevelCountsVO result = new CommentLevelCountsVO();
        result.setGoodCounts(queryCommentsCount(itemId, CommentLevel.GOOD.type));
        result.setNormalCounts(queryCommentsCount(itemId, CommentLevel.NORMAL.type));
        result.setBadCounts(queryCommentsCount(itemId, CommentLevel.BAD.type));
        result.setTotalCounts(result.getGoodCounts() + result.getBadCounts() + result.getNormalCounts());

        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult queryAndPagingItemComments(String itemId, Integer level, Integer page, Integer pageSize) {
        Map<String, Object> queryParams = new TreeMap<>();
        queryParams.put("itemId", itemId);
        queryParams.put("level", level);

        PageHelper.startPage(page, pageSize);
        List<ItemCommentVO> voList = itemsMapper.queryItemComments(queryParams);
        PageHelper.clearPage();

        for (ItemCommentVO vo: voList) {
            vo.setNickname(DesensitizationUtil.commonDisplay(vo.getNickname()));
        }

        return ServiceHelper.setPagedGridResult(voList);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult searchItems(String keyword, String sort, Integer page, Integer pageSize) {
        Map<String, Object> map = new TreeMap<>();
        map.put("keyword", keyword);
        map.put("sort", sort);

        return getPagedGridResult(page, pageSize, map);
    }

    private PagedGridResult getPagedGridResult(Integer page, Integer pageSize, Map<String, Object> map) {
        PageHelper.startPage(page, pageSize);
        List<SearchItemsVO> searchItemsVOS = itemsMapper.searchItems(map);
        PageHelper.clearPage();
        return ServiceHelper.setPagedGridResult(searchItemsVOS);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult searchItems(Integer catId, String sort, Integer page, Integer pageSize) {
        Map<String, Object> map = new TreeMap<>();
        map.put("catId", catId);
        map.put("sort", sort);

        PageHelper.startPage(page, pageSize);
        List<SearchItemsVO> searchItemsVOS = itemsMapper.searchItemsByThirdCat(map);
        PageHelper.clearPage();
        return ServiceHelper.setPagedGridResult(searchItemsVOS);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ShopcartVO> queryItemsBySpecIds(String specIds) {
        String[] ids = specIds.split(",");
        List<String> list = new ArrayList<>();
        Collections.addAll(list, ids);
        return itemsMapper.queryItemsBySpecIds(list);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ItemsSpec queryItemSpecById(String specId) {
        ItemsSpec itemsSpec = itemsSpecMapper.selectByPrimaryKey(specId);
        return itemsSpec;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public String queryItemMainImgById(String itemId) {
        ItemsImgExample example = new ItemsImgExample();
        ItemsImgExample.Criteria criteria = example.createCriteria();
        criteria.andItemIdEqualTo(itemId);
        criteria.andIsMainEqualTo(YesOrNo.YES.type);
        List<ItemsImg> itemsImgs = itemsImgMapper.selectByExample(example);
        return itemsImgs != null ? itemsImgs.get(0).getUrl() : "";
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void decreaseItemSpecStock(String specId, int buyCounts) {

        // synchronized 不推荐使用，集群下无用，性能低下
        // 锁数据库: 不推荐，导致数据库性能低下
        // 分布式锁 zookeeper redis

        // lockUtil.getLock(); -- 加锁

        // 1. 查询库存
//        int stock = 10;

        // 2. 判断库存，是否能够减少到0以下
//        if (stock - buyCounts < 0) {
        // 提示用户库存不够
//            10 - 3 -3 - 5 = -1
//        }

        // lockUtil.unLock(); -- 解锁


        int result = itemsMapper.decreaseItemSpecStock(specId, buyCounts);
        if (result != 1) {
            throw new RuntimeException("订单创建失败，原因：库存不足!");
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public Integer queryCommentsCount(String itemId, Integer level) {
        ItemsCommentsExample example = new ItemsCommentsExample();
        ItemsCommentsExample.Criteria criteria = example.createCriteria();
        criteria.andItemIdEqualTo(itemId);
        criteria.andCommentLevelEqualTo(level);
        long count = itemsCommentsMapper.countByExample(example);
        return (int)count;
    }
}
