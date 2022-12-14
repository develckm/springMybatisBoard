package com.acon.board.dto;
/*
+-----------+--------------+------+-----+-------------------+-------------------+
| Field     | Type         | Null | Key | Default           | Extra             |
+-----------+--------------+------+-----+-------------------+-------------------+
| board_no  | int          | NO   | PRI | NULL              | auto_increment    |
| title     | varchar(255) | NO   |     | NULL              |                   |
| contents  | varchar(255) | YES  |     |                   |                   |
| post_time | datetime     | YES  |     | CURRENT_TIMESTAMP | DEFAULT_GENERATED |
| user_id   | varchar(255) | NO   | MUL | NULL              |                   |
| good      | int          | NO   |     | 0                 |                   |
| bad       | int          | NO   |     | 0                 |                   |
| views     | int          | NO   |     | 0                 |                   |
+-----------+--------------+------+-----+-------------------+-------------------+
 * */
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
@Data
public class Board {
	private int board_no;  
	private String title;     
	private String contents;  
	@DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
	private Date post_time; 
	private int good;      //board_prefer에 좋아요 수 
	private int bad;      //board_prefer에 싫어요 수 
	private Boolean prefer_active; //해당 보드를 검색하는 로그인한 유저가 좋아요 싫어요를 했는지 (null:안함,true:좋아요,false:싫어요)
	private int views;     

	private User user;//N:1  fk user_id 
	private List<Reply> replys;//1:N REPLY.board_no fk
	private List<BoardImg> boardImgs; //1:N BOARD_IMG.board_no fk
	private int replys_size;
}
