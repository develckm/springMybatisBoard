package com.acon.board.controller;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import com.acon.board.dto.Board;
import com.acon.board.dto.BoardImg;
import com.acon.board.dto.BoardPrefer;
import com.acon.board.dto.Reply;
import com.acon.board.dto.User;
import com.acon.board.mapper.BoardImgMapper;
import com.acon.board.mapper.BoardMapper;
import com.acon.board.mapper.BoardPreferMapper;
import com.acon.board.service.BoardService;

@Controller
@RequestMapping("/board")
public class BoardController {	
	//board > board_img 의 수를 5개로 제한
	private final static int BOARD_IMG_LIMIT=5; 
	
	//application.properties 의 설정 경로 받아오기 
	@Value("${spring.servlet.multipart.location}") //파일이 임시저장되는 경로+파일을 저장할 경로
	private String savePath;
	
	//mybatis 컨테이너(sqlSessionFactory)가 의존성 주입
	@Autowired
	private BoardMapper boardMapper;

	@Autowired
	private BoardImgMapper boardImgMapper;
	
	@Autowired
	private BoardPreferMapper boardPreferMapper;
	
	@Autowired
	private BoardService boardService;
	@GetMapping("/list/{page}")
	public String list(@PathVariable int page,Model model,HttpServletRequest req) {
		List<Board> boardList=boardMapper.selectPageAll();
		System.out.println(boardList);
		model.addAttribute(boardList);
		return "/board/list";
	}
	@GetMapping("/detail/{boardNo}")
	public String detail(
			@PathVariable int boardNo,
			Model model,
			@SessionAttribute(required = false) User loginUser) {
		Board board=null;
		BoardPrefer boardPrefer=null; //로그인 안되면 null
		try {
			if(loginUser!=null) {
				boardPrefer=boardPreferMapper.selectUserIdBoardNo(loginUser.getUser_id(), boardNo);
				//좋아요 싫어요를 1번도 안했으면 null
			}
			board = boardService.readBoardUpdateViews(boardNo);
		} catch (Exception e) {e.printStackTrace();}
		
		System.out.println(board);
		if(board!=null) {
			model.addAttribute("boardPrefer",boardPrefer);
			model.addAttribute("board",board);
			return "/board/detail";			
		}else {
			return "redirect:/board/list/1";
		}
	}
	@GetMapping("/insert.do")
	public String insert(HttpSession session) {
		//로그인 한사람만 등록 페이지 가능
		if(session.getAttribute("loginUser")!=null) {
			return "/board/insert";
		}else {
			return "redirect:/";
		}
	}
	@PostMapping(path = "/insert.do",consumes = "multipart/form-data")
	public String insert(Board  board
			,List <MultipartFile> imgFiles
			) {
		//model2(톰캣) 전송된 파일 저장하는 방법
		//1.cos.jar 추가 : 톰캣에서 blob으로 넘어온 data를 multipartRequest로 받을 수 있다.(파일 저장)
		//2.form 에 enctype="multipart/form-data" 추가 : 모든 파라미터를 blob으로 전송 
		//3.서버에서 multipartRequest 객체로 파일과 문자열 파라미터를 구분해서 처리
		System.out.println(board);
		System.out.println(savePath);
		//MultipartFile 받아온 파일은 임시로 저장된 파일
		//transferTo 임시로 저장된 파일을 실제로 저장하는 함수
		//Paths.get("경로+/+파일이름) : 임시 파일을 실제로 저장할 경로를 지정
		int insert=0;
		try {
			//이미지 저장 및 처리
			if(imgFiles!=null) {
				List<BoardImg> boardImgs=new ArrayList<BoardImg>();
				for(MultipartFile imgFile:imgFiles) {		
					String type=imgFile.getContentType();//"image/jpeg"
					if(type.split("/")[0].equals("image")) {
						String newFileName="board_"+System.nanoTime()+"."+type.split("/")[1];
						Path newFilePath=Paths.get(savePath+"/"+newFileName);
						imgFile.transferTo(newFilePath);
						BoardImg boardImg=new BoardImg();
						boardImg.setImg_path(newFileName);
						boardImgs.add(boardImg);
					}
				}
				if(boardImgs.size()>0) {
					board.setBoardImgs(boardImgs);
				}
			}			
			insert=boardService.registBoard(board);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(insert>0) {
			return "redirect:/board/list/1";
		}else {
			return "redirect:/board/insert.do";
		}
		
	}
	@GetMapping("/update/{boardNo}")
	public String update(
			@PathVariable int boardNo,
			Model model,
			HttpSession session) {
		Board board=boardMapper.selectOne(boardNo);
		Object loginUser_obj=session.getAttribute("loginUser");
		if(loginUser_obj!=null && ((User)loginUser_obj).getUser_id().equals(board.getUser().getUser_id())) {
			model.addAttribute(board);
			return "/board/update";			
		}else {
			return "redirect:/user/login.do";			
		}
	}
	@PostMapping("/update.do")
	public String update(
			Board board,
			@RequestParam(name = "boardImgNo",required = false) int [] boardImgNos,
			@RequestParam(name = "imgFile", required = false) MultipartFile[]imgFiles,
			HttpSession session) {
		int update=0;
		Object loginUser_obj=session.getAttribute("loginUser");
		System.out.println(Arrays.toString(boardImgNos)); //삭제할 이미지 번호들
		System.out.println(Arrays.toString(imgFiles)); //등록할 이미지 파일 (blob)
		System.out.println(board);
		if(loginUser_obj!=null && ((User)loginUser_obj).getUser_id().equals(board.getUser().getUser_id()))  {
			try {
				int boardImgCount=boardImgMapper.selectCountBoardNo(board.getBoard_no()); //3개가 등록되어 있다면..
				int insertBoardImgLength= BOARD_IMG_LIMIT-boardImgCount+( (boardImgNos!=null)?boardImgNos.length:0 );
				//0 
				if(imgFiles!=null && insertBoardImgLength>0) {
					List<BoardImg> boardImgs=new ArrayList<BoardImg>();
					for(MultipartFile imgFile : imgFiles) {
						
						String[]types=imgFile.getContentType().split("/");
						if(types[0].equals("image")) {
							String newFileName="board_"+System.nanoTime()+"."+types[1];
							Path path=Paths.get(savePath+"/"+newFileName);
							imgFile.transferTo(path);
							BoardImg boardImg=new BoardImg();
							boardImg.setBoard_no(board.getBoard_no());
							boardImg.setImg_path(newFileName);
							boardImgs.add(boardImg);
							if(--insertBoardImgLength == 0) break; //이미지 수가 5개면 반복문 종료
						}
					}
					if(boardImgs.size()>0) {
						board.setBoardImgs(boardImgs);
					}
				}
				update=boardService.modifyBoardRemoveBoardImg(board, boardImgNos);
			} catch (Exception e) {e.printStackTrace();}
			
			if(update>0) {
				return "redirect:/board/detail/"+board.getBoard_no();
			}else {
				return "redirect:/board/update/"+board.getBoard_no();
			}
		}else {
			return "redirect:/user/login.do";
		}
	}
	@GetMapping("/delete/{boardNo}/{userId}")
	public String delete(@PathVariable int boardNo
						,@PathVariable String userId,
						@SessionAttribute(name = "loginUser",required = false) User loginUser) {
		if(loginUser!=null && loginUser.getUser_id().equals(userId)) {
			int delete=0;
			try {				
				delete=boardService.removeBorad(boardNo);
			} catch (Exception e) {e.printStackTrace();}
			if(delete>0) {
				return "redirect:/board/list/1";			
			}else {
				return "redirect:/board/update/"+boardNo;			
			}			
		}else {
			return "redirect:/user/login.do";
		}
		
	}
	@GetMapping("/prefer/delete/{boardNo}/{boardPreferNo}")
	public String preferDelete(
			@PathVariable int boardNo,
			@PathVariable int boardPreferNo,
			@SessionAttribute (required = false) User loginUser,
			HttpSession session) {
		String msg="";
		if(loginUser!=null) {
			int delete=0;
			try {
				delete=boardPreferMapper.deleteOne(boardPreferNo);
				if(delete>0) {
					msg="평가가 삭제되었습니다.";
				}else {
					msg="평가가 삭제 실패!";
				}
			} catch (Exception e) {
				e.printStackTrace();
				msg="평가가 삭제 실패!(오류)";
			}
			session.setAttribute("msg", msg);
			return "redirect:/board/detail/"+boardNo;	
		}else {
			msg="좋아요,싫어요는 로그인 하셔야 이용하실 수 있습니다.";
			session.setAttribute("msg", msg);
			session.setAttribute("redirectPage", "/board/detail/"+boardNo);
			return "redirect:/user/login.do";
		}
	}
	
	@GetMapping("/prefer/update/{boardNo}/{boardPreferNo}/{prefer}")
	public String preferUpdate(
			@PathVariable int boardNo,
			@PathVariable int boardPreferNo,
			@PathVariable boolean prefer,
			@SessionAttribute(required = false) User loginUser,
			HttpSession session) {
		String msg="";
		if(loginUser!=null) {
			int update=0;
			try {
				update=boardPreferMapper.updateOne(prefer, boardPreferNo);
				if(update>0) {
					msg=(prefer)?"좋아요 수정 성공!":"싫어요 수정 성공!";					
				}else {
					msg=(prefer)?"좋아요 수정 실패!":"싫어요 수정 실패!";
				}
			} catch (Exception e) {
				e.printStackTrace();
				msg=(prefer)?"좋아요 수정 실패!(오류)":"싫어요 수정 실패!(오류)";
			}
			session.setAttribute("msg", msg);
			return "redirect:/board/detail/"+boardNo;
		}else {
			msg="좋아요,싫어요는 로그인 하셔야 이용하실 수 있습니다.";
			session.setAttribute("msg", msg);
			session.setAttribute("redirectPage", "/board/detail/"+boardNo);
			return "redirect:/user/login.do";
		}	
	}
	
	@GetMapping("/prefer/insert/{boardNo}/{prefer}")
	public String preferInsert(
			@PathVariable int boardNo,
			@PathVariable boolean prefer,
			@SessionAttribute(required = false) User loginUser,
			HttpSession session) {
		String msg="";
		if(loginUser!=null) {
			BoardPrefer boardPrefer=new BoardPrefer();
			boardPrefer.setBoard_no(boardNo);
			boardPrefer.setPrefer(prefer);
			boardPrefer.setUser_id(loginUser.getUser_id());
			int insert=0;
			try {
				insert=boardPreferMapper.insertOne(boardPrefer);
				if(insert>0) {
					msg=(prefer)? "좋아요 성공!":"싫어요 성공";
				}else {
					msg=(prefer)? "좋아요 다시 시도!":"싫어요 다시 시도!";	
				}
			} catch (Exception e) {
				e.printStackTrace();
				msg=(prefer)? "좋아요 다시 시도(오류)!":"싫어요 다시 시도(오류)!";	
			}
			session.setAttribute("msg", msg);
			return "redirect:/board/detail/"+boardNo;
		}else {
			msg="좋아요,싫어요는 로그인 하셔야 이용하실 수 있습니다.";
			session.setAttribute("msg", msg);
			session.setAttribute("redirectPage", "/board/detail/"+boardNo);
			return "redirect:/user/login.do";
		}
	}
	
}








