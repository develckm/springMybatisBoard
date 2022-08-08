package com.acon.board.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.acon.board.dto.BoardImg;

//com.acon.board.mapper.BoardImgMapper
@Mapper
public interface BoardImgMapper {
	int insertOne (BoardImg boardImg);
}
