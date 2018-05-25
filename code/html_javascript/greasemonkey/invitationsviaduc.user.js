// invitationsviadeo.user.js
// version 1.0
// 2006-11-8
// Copyright (c) 2006, Eric Mariacher
// Released under the GPL license
// http://www.gnu.org/copyleft/gpl.html
// ==UserScript==
// @name          Invitations Viadeo
// @namespace     http://mariacr.multimania.com/scripts
// @description	  Affiche sur une seule page tous mes invitations Viadeo
// @include       http://www.viadeo.com/invitations/invitationsencours/*
// @version	  1.0
// ==/UserScript==

 // Global variables
  var urlasked=0;
  var urlprocessed=0;



var resultpage=['','','','','','','','','','','','','','','','','','',''];

Initialize();


function evtRecupere() {
	if (window.confirm("Recupere mes invitations?")) {
		var btn = document.getElementById("recupere_mes_invitations");
		btn.setAttribute("value", "Stop");
		li_dbg('there');
		btn.removeEventListener('click', evtRecupere, true);
		btn.addEventListener('click', evtStop, true);
		getNumberOfInvitations();
	}
}

function evtStop() {
	if (window.confirm("Finished")) {
		var btn = document.getElementById("recupere_mes_invitations");
		btn.setAttribute("value", "Stopped");
		for (var i = 0; i < resultpage.length; i++) {
			if(resultpage[i].length>4) {
				outputToTab('<html><head><title>Tous mes invitations Viadeo['+i+']</title></head><body>'+resultpage[i].replace(new RegExp('</table><table>', "g"),'')+'<body><html>');
				//break;
			}
		}
		fifo_initialise();
		btn.removeEventListener('click', evtStop, true);
	}
}

function evtPub() {
	if (window.confirm("2 tabs ouverts")) {
		GM_openInTab('http://www.viadeo.com/contacts/invitationsimple/?from=adressbook&lastname=Mariacher&firstname=Eric&email=eric.mariacher@gmail.com&isMember=true');
		GM_openInTab('http://www.viadeo.com/recherche/profil/index.jsp?memberId=002rw1qp9bm4c75');
	}
}


function Initialize() {
	li_dbg('coucou');
	var ovl = document.createElement("input");
	ovl.setAttribute("id", "recupere_mes_invitations");
	ovl.setAttribute("type", "button");
	ovl.setAttribute("value", "Recupere");
	ovl.style.position = "absolute";
	ovl.style.top = "12px";
	ovl.style.right = "12px";
	ovl.style.backgroundColor = "orange";
	ovl.addEventListener('click', evtRecupere, true);
	document.getElementsByTagName("body")[0].appendChild(ovl);
	
	var ov2 = document.createElement("input");
	ov2.setAttribute("id", "pub");
	ov2.setAttribute("type", "button");
	ov2.setAttribute("value", "Connectez vous avec Eric Mariacher");
	ov2.style.position = "absolute";
	ov2.style.top = "50px";
	ov2.style.right = "12px";
	ov2.style.backgroundColor = "turquoise";
	ov2.addEventListener('click', evtPub, true);
	document.getElementsByTagName("body")[0].appendChild(ov2);
}

function outputToTab(str) {
	GM_openInTab("data:text/html;charset=UTF-8," + encodeURI(str));
}


function searchUrl(counter,mxresultpage,ni) {
	var txt, output, maaxresultpage=mxresultpage, zecounter=counter, nia=ni;
	var urlStr='http://www.viadeo.com/contacts/invitationsencours/index.jsp?pageNumber='+zecounter;
	li_dbg(' searchUrl('+urlStr+') ['+urlasked+'-'+urlprocessed+']');
	request = {
		method:'GET',
		url: urlStr,
		headers: {
			'User-agent': 'Mozilla/4.0 (compatible) Greasemonkey',
			'Accept': 'text/xml',
		},
		onreadystatechange: function(responseDetails){
			var  muxresultpage=maaxresultpage;
			var zacounter=zecounter;
			var numofpages=nia;
			// only if req is "loaded"
			if (responseDetails.readyState == 4) {
				// only if "OK"
				if (responseDetails.status == 200) {
					output=Math.floor(Math.random()*muxresultpage);
					li_dbg('[' +maaxresultpage+'/'+output+']');
					li_dbg(' searchUrl('+urlStr+') returned ' + responseDetails.status +
					' ' + responseDetails.statusText + ' ' + responseDetails.responseText.length);
					resultpage[output]=resultpage[output].concat(parseLinkedInResults(responseDetails.responseText));
					var btn = document.getElementById("recupere_mes_invitations");
					btn.setAttribute("value", "["+zacounter+"/"+numofpages+"]");
					urlprocessed++;
					if(zacounter<numofpages) {
						pausecomp(Math.random()*2000);
						searchUrl(zacounter+1,mxresultpage,numofpages);
					} else {
						for (var i = 0; i < resultpage.length; i++) {
							if(resultpage[i].length>4) {
								outputToTab('<html><head><title>Tous mes invitations Viadeo['+i+']</title></head><body>'+resultpage[i].replace(new RegExp('</table><table>', "g"),'')+'<body><html>');
							}
						}
					}
				} else {
					//li_dbg('status '+responseDetails.status);
				}
			} else {
				//li_dbg('readyState '+responseDetails.readyState);
			}
		}
	};
	urlasked++;
	GM_xmlhttpRequest(request);
}

function parseLinkedInResults(txt) {
	//li_dbg(txt.length);
	var indexa = txt.indexOf('index.jsp?index=A');
	var indexbt = txt.indexOf('<table class="recordset">',indexa);
	var indexet = txt.indexOf('</table>',indexbt);
	//li_dbg(txt);
	//li_dbg(indexbt+' '+indexet);
	var zetext = txt.substring(indexbt,indexet+8);
	//li_dbg(zetext);
	zetext = zetext.replace(new RegExp(' class="\\w*"', "g"),'');
	//zetext = zetext.replace(new RegExp('<a.*envoimessage.*</a>', "g"),'');
	//zetext = zetext.replace(new RegExp('<a.*action=deleteInvitation.*</a>', "g"),'');
	zetext = zetext.replace(new RegExp('<img .*/>', "g"),'');
	zetext = zetext.replace(new RegExp('&nbsp;', "g"),'');
	zetext = zetext.replace(new RegExp('title="\\w*"', "g"),'');
	zetext = zetext.replace(new RegExp('valign="\\w*"', "g"),'');
	zetext = zetext.replace(new RegExp('</*div>', "g"),'');
	zetext = zetext.replace(new RegExp('<img style.*">', "g"),'');
	//zetext = zetext.replace(new RegExp('<a [^>]*></a>', "g"),'');
	zetext = zetext.replace(new RegExp('<br>', "g"),'</td><td>');
	zetext = zetext.replace(new RegExp('<td>\\s*</td>', "g"),'<td>N</td>');
	zetext = zetext.replace(new RegExp('<span>\\s*</span>', "g"),'');
	//zetext = zetext.replace(new RegExp('<td>[^>]*>D&eacute;tail<[^<]*</td>', "g"),'');
	//zetext = zetext.replace(new RegExp('<td>[^>]*>Relancer<[^<]*</td>', "g"),'');
	//zetext = zetext.replace(new RegExp('<td>[^>]*>Retirer<[^<]*</td>', "g"),'');
	zetext = zetext.replace(new RegExp('<td>\\s*<input type="checkbox[^<]*</td>', "g"),'');
	zetext = zetext.replace(new RegExp('<td>\\s*<img[^<]*</td>', "g"),'<td>O</td>');
	zetext = zetext.replace(new RegExp('<tr>[^\\d]*</tr>', "g"),'');
	zetext = zetext.replace(new RegExp('/recherche', "g"),'http://www.viadeo.com/recherche');
	return zetext;
}

function remplace(txt, tin, tout) {
  var re = new RegExp(tin, "g");
  return txt.replace(tin,tout);
}

function getNumberOfInvitations() {
	li_dbg('YES!');
	var allTextareas, thisTextarea;
	allTextareas = document.getElementsByTagName('td');
	li_dbg('YES3 '+allTextareas.length);
	for (var i = 0; i < allTextareas.length; i++) {
		thisTextarea = allTextareas[i];
		var zob = ' personnes dont ';
		var index1 = thisTextarea.textContent.indexOf(zob);
		//li_dbg(thisTextarea.textContent+'=1========'+index1);
		var number=trim(thisTextarea.textContent.substring(index1-13,index1).replace(new RegExp(' ', "g"),''));
		var numofinvitations = (parseInt(number)+1)*1000;
		li_dbg(numofinvitations+ ' [' +number+']');
		var maxresultpage=Math.floor(numofinvitations/600)+1;
		li_dbg('maxresultpage [' +maxresultpage+']');
		searchUrl(1, maxresultpage, Math.floor(numofinvitations/15)+3);
		if((index1>=0)||(index2>=0)) {
			break;
		}
	}
}


// www.sean.co.uk
function pausecomp(millis) {
	var date = new Date();
	var curDate = null;

	li_dbg('wait '+ millis +' milliseconds.');
	do { curDate = new Date(); }
	while(curDate-date < millis);
}

function trim(string) {
	return string.replace(/(^\s*)|(\s*$)/g,'');
}


function li_dbg(str) {
	var date = new Date();
	GM_log(date+':'+str);
}





