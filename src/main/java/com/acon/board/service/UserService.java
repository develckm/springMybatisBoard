package com.acon.board.service;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.acon.board.dto.BoardImg;
import com.acon.board.dto.Reply;
import com.acon.board.mapper.BoardImgMapper;
import com.acon.board.mapper.ReplyMapper;
import com.acon.board.mapper.UserMapper;

@Service
public class UserService {
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private BoardImgMapper boardImgMapper;
	@Autowired
	private ReplyMapper replyMapper;
	@Value("${spring.servlet.multipart.location}")
	String savePath;
	
	public int removeUser(String userId) {
		int remove=0;
		//user>board>board_img ,user>reply  on cascade 로 삭제가 됨 
		//but 등록된 이미지를 삭제해야함
		List<BoardImg> boardImgs=boardImgMapper.selectUserId(userId);
		List<Reply> replies=replyMapper.selectUserId(userId);
		if(boardImgs!=null) {
			for(BoardImg boardImg:boardImgs) {
				File f=new File(savePath+"/"+boardImg.getImg_path());
				System.out.println("user가 작성한 board의 이미지 삭제:"+f.delete());
			}
		}
		if(replies!=null) {
			for(Reply reply: replies) {
				if(reply.getImg_path()!=null) {
					File f=new File(savePath+"/"+reply.getImg_path());
					System.out.println("user가 작성함 reply의 이미지 삭제:"+f.delete());
				}
			}
		}
		remove=userMapper.deleteOne(userId);
		return remove;
	}
}
