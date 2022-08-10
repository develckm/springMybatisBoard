package com.acon.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.acon.board.dto.BoardImg;

//com.acon.board.mapper.BoardImgMapper
@Mapper
public interface BoardImgMapper {
	int insertOne (BoardImg boardImg);
	List<BoardImg> selectBoardNo(int boardNo);
	List<BoardImg> selectUserId(String userId);
	BoardImg selectOne(int boardImgNo);
	int deleteOne(int boardImgNo);
	int selectCountBoardNo(int boardNo);
}
