package com.acon.board.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

/* Board table에서 이미지를 복수로 참조하기 위해 만들어진 테이블
+--------------+--------------+------+-----+---------+----------------+
| Field        | Type         | Null | Key | Default | Extra          |
+--------------+--------------+------+-----+---------+----------------+
| board_img_no | int          | NO   | PRI | NULL    | auto_increment |
| board_no     | int          | NO   | MUL | NULL    |                |
| img_path     | varchar(255) | NO   |     | NULL    |                |
+--------------+--------------+------+-----+---------+----------------+
 * 
 * */
@Data
public class BoardImg {
	private int board_img_no;
	private int board_no;//fk
	private String img_path;
}
