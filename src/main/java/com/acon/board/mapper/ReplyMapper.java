package com.acon.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.acon.board.dto.Reply;

//com.acon.board.mapper.ReplyMapper
@Mapper
public interface ReplyMapper {
	Reply selectOne(int replyNo);
	List<Reply> selectBoardNo(int boardNo);
	List<Reply> selectUserId(String userId);
	
	int insertOne(Reply reply);
	int updateOne(Reply reply);
	int updateGood(int replyNo);
	int updateBad(int replyNo);
	int deleteOne(int replyNo);
	
}
