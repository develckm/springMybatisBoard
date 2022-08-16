
async function replyPreferHandler(replyNo,preferActive,btn){
	console.log(preferActive);
	let url="/reply/prefer/";
	let prefer=((btn=="good")?true:false);
	let method;
	let replyLiId="replyLi"+replyNo;
	if(preferActive==null){
		url+=replyNo+"/"+prefer;
		method="post";
	}else if( (preferActive && prefer) || (!preferActive && !prefer)){
		url+=replyNo;		
		method="delete";
	}else {
		url+=replyNo+"/"+prefer;
		method="put";
	}
	try{
		let res=await fetch(url,{method:method});
		if(res.status==200){
			let htmlText=await res.text();
			document.getElementById(replyLiId).innerHTML=htmlText;
		}else if(res.status==400){
			alert("로그인 하세요!");
	
		}else{
			alert("잘못된 시도입니다.");
		}
	}catch(err){
		alert('다시 시도하세요!'+err);
	}
}