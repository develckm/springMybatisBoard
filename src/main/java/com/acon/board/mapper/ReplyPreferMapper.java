package com.acon.board.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.acon.board.dto.ReplyPrefer;

//com.acon.board.mapper.ReplyPreferMapper
@Mapper
public interface ReplyPreferMapper {
	int insertOne(ReplyPrefer replyPrefer);
	int updateOne(ReplyPrefer replyPrefer);
	int deleteOne(ReplyPrefer replyPrefer);

}
