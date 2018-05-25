// getviaducemails.user.js
// version 1.0
// 2006-11-8
// Copyright (c) 2006, Eric Mariacher
// Released under the GPL license
// http://www.gnu.org/copyleft/gpl.html
// ==UserScript==
// @name          New Contacts Xing
// @namespace     http://z.z
// @description	  Invite les nouveaux sur Xing
// @include       https://www.xing.com/app/search*
// ==/UserScript==


Initialize();


function evtMoissonne() {
	if (window.confirm("Regarde les nouveaux?")) {
		var btn = document.getElementById("moissonne_des_mails");
		btn.setAttribute("value", "Stop");
		li_dbg('evtMoissonne1');
		btn.removeEventListener('click', evtMoissonne, true);
		li_dbg('evtMoissonne2');
		getKeyword();
		li_dbg('evtMoissonne3');
	}
}

function Initialize() {
	var ovl = document.createElement("input");
	ovl.setAttribute("id", "moissonne_des_mails");
	ovl.setAttribute("type", "button");
	ovl.setAttribute("value", "Regarde");
	ovl.style.position = "absolute";
	ovl.style.top = "100px";
	ovl.style.right = "12px";
	ovl.style.backgroundColor = "wheat";
	ovl.addEventListener('click', evtMoissonne, true);
	document.getElementsByTagName("body")[0].appendChild(ovl);
}


function getKeyword() {
	li_dbg('YES!');
	var allTextareas, thisTextarea;
	allTextareas = document.getElementsByTagName('a');
	for (var i = 0; i < allTextareas.length; i++) {
		thisTextarea = allTextareas[i];
		li_dbg('y1');
		var zclass=thisTextarea.getAttribute("class");
		var href=thisTextarea.getAttribute("href");
		li_dbg('y2 '+href+', '+zclass);
		if((href.indexOf("profile")>0)&&(zclass)) {
			GM_openInTab('https://www.xing.com'+href);
		}
	}
}


function trim(string) {
	return string.replace(/(^\s*)|(\s*$)/g,'');
}


function li_dbg(str) {
	var date = new Date();
	GM_log(date+':'+str);
}



