$(window).scroll(function(){  
   if ($(window).scrollTop()>100){  
        $("#top").fadeIn("slow");  
    }else {  
         $("#top").fadeOut("slow");  
    }  
});  


function backtop(){
	$("body").animate({scrollTop:0},1500);  
};

function setTitle(name) {
	var title = document.getElementById("title");
	title.innerHTML=name;
}

function setDec(time,source) {
	var tm = document.getElementById("time");
	tm.innerHTML=time;
	var fro = document.getElementById("from");
	fro.innerHTML=source;
}

function setBody(body){
	var bd = document.getElementById("article_body");
	bd.innerHTML=body;
	removeImg();
}

function removeImg(){
	var imgarray = document.getElementsByTagName("img");
	for(var i=0; i< imgarray.length;i++){
		var img = imgarray[i];
		var src = img.getAttributeNode("src");
		if(src.value == "%s"){
			img.parentNode.removeChild(img);
		}
		
	}
}