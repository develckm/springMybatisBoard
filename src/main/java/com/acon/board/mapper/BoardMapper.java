package com.acon.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.acon.board.dto.Board;

//com.acon.board.mapper.BoardMapper
@Mapper
public interface BoardMapper {
	//질의어 SELECT 의 결과는 무조건 복수지만 Mybatis가 단수로도 반환해준다.
	List<Board>	selectPageAll(int startRow,int pageSize);
	int	selectPageAllCount();

	Board selectOne(int boardNo);
	Board selectOne(int boardNo,String loginUserId);//loginUserId 로그인한 유저가 좋아요 싫어요를 했는지 확인
	int updateGood(int boardNo);
	int updateBad(int boardNo);
	int updateViews( int boardNo);
	int insertOne(Board board);
	int updateOne(Board board);
	int deleteOne(int boardNo);
}
