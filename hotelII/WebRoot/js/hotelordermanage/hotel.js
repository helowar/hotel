function chg_btn(num){
	var id_name="btn"+num;
	var tr_name="cont"+num;
	for(i=1;i<=10;i++){
		document.getElementById("btn"+i).className="kuang02";
		document.getElementById("cont"+i).style.display="none";
	}
	document.getElementById(id_name).className="kuang01";
	document.getElementById(tr_name).style.display="block";
}

function chg_button(number){
	var id_name="button"+number;
	var tr_name="content"+number;
	for(i=1;i<=4;i++){
		document.getElementById("button"+i).className="kuang04";
		document.getElementById("content"+i).style.display="none";
	}
	document.getElementById(id_name).className="kuang03";
	document.getElementById(tr_name).style.display="block";
}

function chg_tr(shu){
	var id_name="butt"+shu;
	var tr_name="con"+shu;
	for(i=1;i<=3;i++){
		document.getElementById("butt"+i).className="kuang06";
		document.getElementById("con"+i).style.display="none";
	}
	document.getElementById(id_name).className="kuang05";
	document.getElementById(tr_name).style.display="block";
}
