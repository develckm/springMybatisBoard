package com.acon.board.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.acon.board.dto.Board;
import com.acon.board.dto.BoardImg;
import com.acon.board.mapper.BoardImgMapper;
import com.acon.board.mapper.BoardMapper;

@Service
public class BoardService {

	@Autowired
	private BoardMapper boardMapper;
	@Autowired
	private BoardImgMapper boardImgMapper;
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








