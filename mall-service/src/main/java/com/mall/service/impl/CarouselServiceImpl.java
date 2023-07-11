package com.mall.service.impl;

import com.mall.mapper.CarouselMapper;
import com.mall.pojo.Carousel;
import com.mall.pojo.CarouselExample;
import com.mall.service.CarouselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CarouselServiceImpl implements CarouselService {

    CarouselMapper carouselMapper;

    @Autowired
    public CarouselServiceImpl(CarouselMapper carouselMapper) {
        this.carouselMapper = carouselMapper;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Carousel> queryAll(Integer isShow) {
        CarouselExample carouselExample = new CarouselExample();
        CarouselExample.Criteria criteria = carouselExample.createCriteria();
        criteria.andIsShowEqualTo(isShow);
        carouselExample.setOrderByClause("sort desc");
        List<Carousel> carousels = carouselMapper.selectByExample(carouselExample);
        return carousels;
    }
}
