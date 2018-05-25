// ==UserScript==
// @name	  transmet
// @namespace	  http://z.z
// @description	  Transmet une requete Viaduc et/ou LinkedIn
// @include	  http://www.viaduc.com/miseenrelation/transfertdemande/*
// @version   	  0.1
// ==/UserScript==


/*function createButton(id,value,top,color,evtlistener) {
	var ovl = document.createElement("input");
	ovl.setAttribute("id", id);
	ovl.setAttribute("type", "button");
	ovl.setAttribute("value", value);
	ovl.style.position = "absolute";
	ovl.style.top = top;
	ovl.style.right = "12px";
	ovl.style.backgroundColor = color;
	ovl.addEventListener('click', evtlistener, true);
	return ov1;
}


function evtATransmettre() {
	li_dbg('evtTransmet1');
}


function evtPourVous() {
	li_dbg('evtPourVous1');
}*/



function Initialize() {
	li_dbg('Initialize1');
	//document.getElementsByTagName("body")[0].appendChild(createButton("atransmettre","A transmettre","200px","green",evtATransmettre()));
	//document.getElementsByTagName("body")[0].appendChild(createButton("pourvous","Pour Vous","250px","green",evtPourVous()));
}


Initialize();

function li_dbg(str) {
	var date = new Date();
	GM_log(date+':'+str);
}

