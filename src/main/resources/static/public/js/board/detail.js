
function replyPreferHandler(replyNo,preferActive,btn){
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
	fetch(url,{method:method})
		.then((res)=>{
			if(res.status==200){
				return res.text();
			}else if(res.status==400){
				alert("로그인 하세요!");
			}else{
				alert("잘못된 시도입니다.");
			}
			
		})
		.then((text)=>{document.getElementById(replyLiId).innerHTML=text})
		.catch((err)=>{alert('다시 시도하세요!'+err)});
}