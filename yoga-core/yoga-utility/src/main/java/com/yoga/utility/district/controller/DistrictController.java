package com.yoga.utility.district.controller;

import com.yoga.core.base.BaseController;
import com.yoga.core.data.ChainMap;
import com.yoga.core.data.CommonResult;
import com.yoga.core.data.MapConverter;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.utility.district.dto.DistrictGetDto;
import com.yoga.utility.district.dto.DistrictListDto;
import com.yoga.utility.district.dto.DistrictChildDto;
import com.yoga.utility.district.model.District;
import com.yoga.utility.district.service.DistrictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "行政区划")
@RestController
@EnableAutoConfiguration
@RequestMapping("/admin/system/district")
public class DistrictController extends BaseController {

    @Autowired
    private DistrictService districtService;

    @GetMapping("/list.json")
    @ApiOperation(value = "获取子区域")
    public CommonResult list(@Valid @ModelAttribute DistrictChildDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        List<District> districts = districtService.getChildren(dto.getParentId());
        return new CommonResult(new MapConverter<>((District item, ChainMap<String, Object> map)-> {
            map.set("id", item.getId())
                    .set("name", item.getName())
                    .set("suffix", item.getSuffix())
                    .set("fullname", item.getFullname())
                    .set("code", item.getCode())
                    .set("pinyin", item.getPinyin())
                    .set("initial", item.getInitial())
                    .set("initials", item.getInitials());
        }).build(districts));
    }

    @GetMapping("/get.json")
    @ApiOperation(value = "获取区域详情")
    public CommonResult get(@Valid @ModelAttribute DistrictGetDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        District district = districtService.getDistrict(dto.getId());
        return new CommonResult(new MapConverter<>((District item, ChainMap<String, Object> map)-> {
            map.set("id", item.getId())
                    .set("name", item.getName())
                    .set("suffix", item.getSuffix())
                    .set("fullname", item.getFullname())
                    .set("code", item.getCode())
                    .set("pinyin", item.getPinyin())
                    .set("initial", item.getInitial())
                    .set("initials", item.getInitials());
        }).build(district));
    }

    @GetMapping("/code.json")
    @ApiOperation(value = "通过CODE获取区域")
    public CommonResult getByCode(@Valid @ModelAttribute DistrictListDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        District district = districtService.getByCode(dto.getCode(), dto.getLevel());
        return new CommonResult(new MapConverter<>((District item, ChainMap<String, Object> map)-> {
            map.set("id", item.getId())
                    .set("name", item.getName())
                    .set("suffix", item.getSuffix())
                    .set("fullname", item.getFullname())
                    .set("code", item.getCode())
                    .set("pinyin", item.getPinyin())
                    .set("initial", item.getInitial())
                    .set("initials", item.getInitials());
        }).build(district));
    }
}
