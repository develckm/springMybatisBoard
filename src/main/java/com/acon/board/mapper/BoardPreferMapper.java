package com.acon.board.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.acon.board.dto.BoardPrefer;

//com.acon.board.mapper.BoardPreferMapper
@Mapper
public interface BoardPreferMapper {
	BoardPrefer selectUserIdBoardNo(String userId,int boardNo);
	int insertOne (BoardPrefer boardPrefer);
	int updateOne (boolean prefer,int boardPreferNo);
	int deleteOne (int boardPreferNo);

}
