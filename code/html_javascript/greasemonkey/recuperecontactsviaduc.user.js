/*
recuperecontactsviaduc.user.js
version 0.1
*/
// ==UserScript==
// @name          Contacts Viaduc
// @namespace     http://z.z
// @description	  Affiche sur une seule page tous mes contacts Viaduc
// @include       http://www.viaduc.com/contacts/contactsdirects/*
// ==/UserScript==

var resultpage='';

Initialize();


function evtRecupere() {
	if (window.confirm("Récupère mes contacts?")) {
		var btn = document.getElementById("recupere_mes_contacts");
		btn.setAttribute("value", "Stop");
		li_dbg('there');
		btn.removeEventListener('click', evtRecupere, true);
		btn.addEventListener('click', evtStop, true);
	}
}

function evtStop() {
	if (window.confirm("Finished?")) {
		var btn = document.getElementById("stop_btn");
		btn.setAttribute("value", "Stopped");
		outputToTab('<html><head><title>Tous mes contacts Viaduc</title></head><body>'+resultpage+'<body><html>');
		btn.removeEventListener('click', evtStop, true);
	}
}

function Initialize() {
	var ovl = document.createElement("input");
	ovl.setAttribute("id", "recupere_mes_contacts");
	ovl.setAttribute("type", "button");
	ovl.setAttribute("value", "Recupere");
	ovl.style.position = "absolute";
	ovl.style.top = "12px";
	ovl.style.right = "12px";
	ovl.style.backgroundColor = "lime";
	ovl.addEventListener('click', evtRecupere, true);
	document.getElementsByTagName("body")[0].appendChild(ovl);
}

function outputToTab(str) {
	GM_openInTab("data:text/html;charset=UTF-8," + encodeURI(str));
}

function searchUrls() {	
	for (var i = 0; i < 112; i++) {
		li_dbg('+'+i+'+ ');
		searchUrl('http://www.viaduc.com/contacts/contactsdirects/index.jsp?pageNumber='+i);
	}
}

function searchUrl(urlStr) {
	var txt;
	request = {
		method:'GET',
		url: urlStr,
		headers: {
			'User-agent': 'Mozilla/4.0 (compatible) Greasemonkey',
			'Accept': 'text/xml',
		},
		onreadystatechange: function(responseDetails){
			// only if req is "loaded"
			if (responseDetails.readyState == 4) {
				// only if "OK"
				if (responseDetails.status == 200) {
					li_dbg(' searchUrl('+urlStr+' returned ' + responseDetails.status +
					' ' + responseDetails.statusText + ' ' + responseDetails.responseText.length);
					resultpage=resultpage.concat(parseLinkedInResults(responseDetails.responseText))
				} else {
					li_dbg(' searchUrl('+urlStr+' returned ' + responseDetails.status +
					' ' + responseDetails.statusText + ' ' + responseDetails.responseText.length);
				}
			} else {
				li_dbg(responseDetails.readyState);
			}
		}
	};
		GM_xmlhttpRequest(request);
}

function parseLinkedInResults(txt) {
	li_dbg(txt.length);
	var indexa = txt.indexOf('index.jsp?index=A');
	var indexbt = txt.indexOf('<table class="recordset">',indexa);
	var indexet = txt.indexOf('</table>',indexbt);
	return txt.substring(indexbt,indexet+8);
}


// www.sean.co.uk
function pausecomp(millis)
{
	var date = new Date();
	var curDate = null;

	li_dbg('wait '+ millis +' milliseconds.');
	do { curDate = new Date(); }
	while(curDate-date < millis);
}


function li_dbg(str) {
	var date = new Date();
	GM_log(date+':'+str);
}



