package com.mall.controller;

import com.mall.config.I18nMessage;
import com.mall.enums.YesOrNo;
import com.mall.pojo.Carousel;
import com.mall.pojo.Category;
import com.mall.pojo.vo.CategoryVO;
import com.mall.pojo.vo.NewItemsVO;
import com.mall.service.CarouselService;
import com.mall.service.CategoryService;
import com.mall.utils.MallJSONResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "首页")
@RestController
@RequestMapping("/index")
public class IndexController {

    private CarouselService carouselService;
    private CategoryService categoryService;

    @Autowired
    public IndexController(CarouselService carouselService, CategoryService categoryService) {
        this.carouselService = carouselService;
        this.categoryService = categoryService;
    }

    @Operation(summary = "查询轮播信息")
    @GetMapping("/carousel")
    public MallJSONResult carousel() {
        List<Carousel> carousels = carouselService.queryAll(YesOrNo.YES.type);
        return MallJSONResult.ok(carousels);
    }

    @Operation(summary = "获取商品分类（根分类）")
    @GetMapping("/cats")
    public MallJSONResult rootCategories(){
        List<Category> categories = categoryService.queryRootCategories();
        return MallJSONResult.ok(categories);
    }

    @Operation(summary = "根据父ID获取商品子分类")
    @GetMapping("/subCat/{rootCatId}")
    public MallJSONResult subCategories(@Parameter(description = "父类ID") @PathVariable("rootCatId") Integer fatherId){
        if (fatherId == null) {
            return MallJSONResult.errorMsg(I18nMessage.message("index.error.fatherId_null"));
        }
        List<CategoryVO> categories = categoryService.querySubCategories(fatherId);
        return MallJSONResult.ok(categories);
    }

    @Operation(summary = "查询某个一级分类下的6条商品数据")
    @GetMapping("/sixNewItems/{rootCatId}")
    public MallJSONResult sixNewItems(@PathVariable("rootCatId") Integer rootCatId) {
        if (rootCatId == null) {
            return MallJSONResult.errorMsg(I18nMessage.message("index.error.fatherId_null"));
        }
        List<NewItemsVO> newItemsVOS = categoryService.queryNewItems(rootCatId, 6);
        return MallJSONResult.ok(newItemsVOS);
    }
}
