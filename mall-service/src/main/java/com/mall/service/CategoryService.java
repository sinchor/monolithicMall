package com.mall.service;

import com.mall.pojo.Category;
import com.mall.pojo.vo.CategoryVO;
import com.mall.pojo.vo.NewItemsVO;

import java.util.List;
import java.util.Map;

public interface CategoryService {

    /**
     * query all the root categories which level = 1
     * @return all the root categories
     */
    public List<Category> queryRootCategories();

    /**
     * query sub categories accroding to the father category
     * @param fatherId id which represents the father category
     * @return a list of category to be used in the frontend
     */
    public List<CategoryVO> querySubCategories(Integer fatherId);

    /**
     * query items belonging to the specified category.
     * @param fatherId id which represents the father category
     * @return a list of items to be used in the frontend
     */
    public List<NewItemsVO> queryNewItems(Integer fatherId, Integer itemNum);
}
