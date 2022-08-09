package com.acon.board.service;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.acon.board.dto.Board;
import com.acon.board.dto.BoardImg;
import com.acon.board.dto.Reply;
import com.acon.board.mapper.BoardImgMapper;
import com.acon.board.mapper.BoardMapper;
import com.acon.board.mapper.ReplyMapper;

@Service
public class BoardService {

	@Autowired
	private BoardMapper boardMapper;
	@Autowired
	private BoardImgMapper boardImgMapper;
	@Autowired
	private ReplyMapper replyMapper;
	@Value("${spring.servlet.multipart.location}")
	String savePath;
	public int removeBorad(int boardNo) throws Exception{
		int remove=0;
		//보드를 참조하는 리플의 이미지를 삭제하기위해 리플 리스트를 검색 
		List<Reply> replies=replyMapper.selectBoardNo(boardNo);
		if(replies!=null) {
			replies.stream()
				.filter((r)->(r.getImg_path()!=null))
				.map(Reply::getImg_path).
				forEach((img)->{
					File f=new File(savePath+"/"+img);
					System.out.println("리플 이미지 삭제:"+f.delete());
				});
		}
		//Board를 참조하는 Board_img도 삭제
		List<BoardImg> boardImgs=boardImgMapper.selectBoardNo(boardNo);
		if(boardImgs!=null) {
			boardImgs.stream()
				.map(BoardImg::getImg_path)
				.forEach((img)->{
					File f=new File(savePath+"/"+img);
					System.out.println("보드 이미지 삭제:"+f.delete());
				});
		}
		remove=boardMapper.deleteOne(boardNo);
		return remove;
	}
	
	
	public int registBoard(Board board) throws Exception {
		int regist=0;
		// useGeneratedKeys="true" keyProperty="board_no"  :  등록한 pk 를 board에 저장함
		regist=boardMapper.insertOne(board);
		int imgRegist=0;
		if(regist>0 && board.getBoardImgs()!=null) {
			for(BoardImg boardImg : board.getBoardImgs()) {
				boardImg.setBoard_no(board.getBoard_no()); //Auto Increment 로 저장된 대표키 값
				imgRegist+=boardImgMapper.insertOne(boardImg);
			}
			
		}
		System.out.println("보드 등록 :"+regist);
		System.out.println("보드 이미지 등록 :"+imgRegist);
		return regist;
	}
	
	public Board readBoardUpdateViews(int boardNo) {
		boardMapper.updateViews(boardNo);
		return boardMapper.selectOne(boardNo);
	}
	
}








