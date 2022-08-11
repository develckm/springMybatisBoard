package com.acon.board.dto;

import lombok.Data;

/*
+-----------------+--------------+------+-----+---------+----------------+
| Field           | Type         | Null | Key | Default | Extra          |
+-----------------+--------------+------+-----+---------+----------------+
| board_prefer_no | int          | NO   | PRI | NULL    | auto_increment |
| board_no        | int          | NO   | MUL | NULL    |                |
| prefer          | tinyint(1)   | YES  |     | NULL    |                |
| user_id         | varchar(255) | NO   | MUL | NULL    |                |
+-----------------+--------------+------+-----+---------+----------------+ 
 * */
@Data
public class BoardPrefer {
	private int board_prefer_no;
	private int board_no;
	private boolean prefer;
	private String user_id;
}

