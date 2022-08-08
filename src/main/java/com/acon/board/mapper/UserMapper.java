package com.acon.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.acon.board.dto.User;

//com.acon.board.mapper.UserMapper

@Mapper
public interface UserMapper {

	List<User> selectPageAll(int startRow,int pageSize);
	int selectPageAllCount();
	User selectOne( String userId);
	User selectPwOne(String userId, String pw);
	int deleteOne(String userId);
	int updateOne(User user);
	int insertOne(User user);
}
