package com.acon.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.acon.board.dto.Reply;

//com.acon.board.mapper.ReplyMapper
@Mapper
public interface ReplyMapper {
	List<Reply> selectBoardNoPage(int boardNo,int startRow,int pageSize);
	List<Reply> selectBoardNoPage(int boardNo,int startRow,int pageSize,String loginUserId);
	int selectBoardNoCount(int boardNo);

	
	Reply selectOne(int replyNo);
	Reply selectOnePrefers(int replyNo);
	Reply selectOnePrefers(int replyNo,String LoginUserId);
	//로그인한 유저가 해당 댓글에 좋아요 싫어요를 했는지 확인

	
	List<Reply> selectBoardNo(int boardNo);
	List<Reply> selectUserId(String userId);
	
	int insertOne(Reply reply);
	int updateOne(Reply reply);
	int updateGood(int replyNo);
	int updateBad(int replyNo);
	int deleteOne(int replyNo);
	
}
