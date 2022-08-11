package com.acon.board.dto;

import lombok.Data;

/*
+-----------------+--------------+------+-----+---------+----------------+
| Field           | Type         | Null | Key | Default | Extra          |
+-----------------+--------------+------+-----+---------+----------------+
| reply_prefer_no | int          | NO   | PRI | NULL    | auto_increment |
| reply_no        | int          | NO   | MUL | NULL    |                |
| prefer          | tinyint(1)   | YES  |     | NULL    |                |
| user_id         | varchar(255) | NO   | MUL | NULL    |                |
+-----------------+--------------+------+-----+---------+----------------+ 
 * */
@Data
public class ReplyPrefer {
	private int reply_prefer_no;
	private int reply_no;
	private boolean prefer;
	private String user_id;
}

