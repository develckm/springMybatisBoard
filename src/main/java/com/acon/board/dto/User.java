package com.acon.board.dto;
/*
+---------+--------------+------+-----+-------------------+-------------------+
| Field   | Type         | Null | Key | Default           | Extra             |
+---------+--------------+------+-----+-------------------+-------------------+
| user_id | varchar(255) | NO   | PRI | NULL              |                   |
| name    | varchar(255) | NO   |     | NULL              |                   |
| pw      | varchar(255) | NO   |     | NULL              |                   |
| phone   | varchar(255) | NO   | UNI | NULL              |                   |
| email   | varchar(255) | NO   | UNI | NULL              |                   |
| birth   | date         | NO   |     | NULL              |                   |
| signup  | datetime     | YES  |     | CURRENT_TIMESTAMP | DEFAULT_GENERATED |
+---------+--------------+------+-----+-------------------+-------------------+
 * 
 * */
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
@Data
public class User {
	private String user_id; //pk user_id
	private String name;   
	private String pw;     
	private String phone;  
	private String email;  
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date birth;
	@DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss") //input type="datetime-local"의 value
	private Date signup; 
	private List<Board> boards; 
	//private List<Reply> replys; 

}






