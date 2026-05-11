package com.example.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.example.app.domain.CategoryColor;

@Mapper
public interface CategoryColorMapper {

	// 全件
	@Select("SELECT * FROM category_colors ORDER BY color_index")
	List<CategoryColor> findAll();

	// 渡されたcolor_indexを参照
	@Select("SELECT * FROM category_colors "
			+ "WHERE color_index = #{colorIndex}")
	CategoryColor findByIndex(Long colorIndex);

}
