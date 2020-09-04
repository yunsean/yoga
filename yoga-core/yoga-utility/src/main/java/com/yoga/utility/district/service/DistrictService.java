package com.yoga.utility.district.service;

import com.yoga.core.base.BaseService;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.mybatis.MapperQuery;
import com.yoga.utility.district.mapper.DistrictMapper;
import com.yoga.utility.district.model.District;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DistrictService extends BaseService {
    @Autowired
    private DistrictMapper districtMapper;

    public List<District> getChildren(long parentId) {
        return new MapperQuery<>(District.class)
                .andEqualTo("parentId", parentId)
                .query(districtMapper);
    }

    public District getByCode(String code, Integer level) {
        List<District> districts = new MapperQuery<>(District.class)
                .andEqualTo("code", code)
                .query(districtMapper);
        if (districts == null || districts.size() < 1) throw new BusinessException("未找到对应的行政区域！");
        District district = districts.get(0);
        if (level == null) return district;
        if (district.getLevel() == level) return district;
        if (district.getLevel() < level) throw new BusinessException("未找到对应的行政区域！");
        while (true) {
            if (district.getParentId() == 0) throw new BusinessException("未找到对应的行政区域！");
            district = districtMapper.selectByPrimaryKey(district.getParentId());
            if (district == null || district.getLevel() < level) throw new BusinessException("未找到对应的行政区域！");
            if (district.getLevel() == level) return district;
        }
    }

    public District getDistrict(long id) {
        District district = districtMapper.selectByPrimaryKey(id);
        if (district == null) throw new BusinessException("未找到该行政区域！");
        return district;
    }
}
