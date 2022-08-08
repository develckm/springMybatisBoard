package com.acon.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.acon.board.dto.Board;

//com.acon.board.mapper.BoardMapper
@Mapper
public interface BoardMapper {
	//질의어 SELECT 의 결과는 무조건 복수지만 Mybatis가 단수로도 반환해준다.
	List<Board>	selectPageAll();
	
	Board selectOne(int boardNo);
	int updateGood(int boardNo);
	int updateBad(int boardNo);
	int updateViews(int boardNo);
	int insertOne(Board board);
	int updateOne(Board board);
	int deleteOne(int boardNo);
}
