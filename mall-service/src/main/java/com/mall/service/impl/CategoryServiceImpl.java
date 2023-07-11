package com.mall.service.impl;

import com.mall.mapper.CategoryMapperExt;
import com.mall.pojo.Category;
import com.mall.pojo.CategoryExample;
import com.mall.pojo.vo.CategoryVO;
import com.mall.pojo.vo.NewItemsVO;
import com.mall.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class CategoryServiceImpl implements CategoryService {

    CategoryMapperExt categoryMapper;

    @Autowired
    public CategoryServiceImpl(CategoryMapperExt categoryMapperExt) {
        this.categoryMapper = categoryMapperExt;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Category> queryRootCategories() {
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        criteria.andTypeEqualTo(1);
        List<Category> categories = categoryMapper.selectByExample(categoryExample);
        return categories;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<CategoryVO> querySubCategories(Integer fatherId) {
        List<CategoryVO> subCategories = categoryMapper.getSubCategories(fatherId);
        return subCategories;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<NewItemsVO> queryNewItems(Integer fatherId, Integer itemNum) {
        Map<String, Object> paramMap = new TreeMap<>();
        paramMap.put("rootCatId", fatherId);
        paramMap.put("limit", itemNum);
        List<NewItemsVO> newItems = categoryMapper.getNewItems(paramMap);
        return newItems;
    }
}
