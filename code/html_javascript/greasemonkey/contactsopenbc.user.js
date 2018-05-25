// contactsopenbc.user.js
// version 1.0
// 2006-11-8
// Copyright (c) 2006, Eric Mariacher
// Released under the GPL license
// http://www.gnu.org/copyleft/gpl.html
// ==UserScript==
// @name          Contacts Openbc
// @namespace     http://z.z
// @description	  Affiche sur une seule page tous mes contacts OpenBC
// @include       https://www.openbc.com/cgi-bin/contact.fpl*

var resultpage=['','','','','','','','','',''];

Initialize();


function evtGetContacts() {
	if (window.confirm("Get my contacts?")) {
		var btn = document.getElementById("get_my_contacts");
		btn.setAttribute("value", "Stop");
		li_dbg('there');
		btn.removeEventListener('click', evtGetContacts, true);
		btn.addEventListener('click', evtStop, true);
		getNumberOfPages()();
	}
}

function evtStop() {
	if (window.confirm("Finished")) {
		var btn = document.getElementById("get_my_contacts");
		btn.setAttribute("value", "Stopped");
		for (var i = 0; i < resultpage.length; i++) {
			if(resultpage[i].length>4) {
				outputToTab('<html><head><title>Tous mes contacts OpenBC['+i+']</title></head><body>'+resultpage[i].replace(new RegExp('</table><table>', "g"),'')+'<body><html>');
			}
		}
		btn.removeEventListener('click', evtStop, true);
	}
}

function Initialize() {
	var ovl = document.createElement("input");
	ovl.setAttribute("id", "get_my_contacts");
	ovl.setAttribute("type", "button");
	ovl.setAttribute("value", "Recupere");
	ovl.style.position = "absolute";
	ovl.style.top = "12px";
	ovl.style.right = "12px";
	ovl.style.backgroundColor = "lime";
	ovl.addEventListener('click', evtGetContacts, true);
	document.getElementsByTagName("body")[0].appendChild(ovl);
}

function outputToTab(str) {
	GM_openInTab("data:text/html;charset=UTF-8," + encodeURI(str));
}

function searchUrls(numofpage) {
	li_dbg(numofpage);
	
	for (var i = 60; i < numofpage; i++) {
		var offset=i*10;
		searchUrl('https://www.openbc.com/cgi-bin/contact.fpl?search_filter=&tags_filter=&notags_filter=0&op=undecided&offset='+offset,i);
	}
}

function searchUrl(urlStr,index) {
	var txt, i, output;
	i=index;
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
					output=Math.floor(i/62);
					li_dbg(' searchUrl('+urlStr+'['+i+'/'+output+'] returned ' + responseDetails.status +
					' ' + responseDetails.statusText + ' ' + responseDetails.responseText.length);
					resultpage[output]=resultpage[output].concat(parseLinkedInResults(responseDetails.responseText));
					var btn = document.getElementById("get_my_contacts");
					btn.setAttribute("value", "..."+i);
				}
			}
		}
	};
	GM_xmlhttpRequest(request);
}

function parseLinkedInResults(txt) {
	li_dbg(txt.length);
	var indexbt = txt.indexOf('<!-- content table -->');
	var indexet = txt.indexOf('<!-- /content table -->',indexbt);
	li_dbg(indexbt+' '+indexet);
	var zetext = txt.substring(indexbt,indexet+23);
	/*zetext = zetext.replace(new RegExp('<td>\\s*</td>', "g"),'');
	zetext = zetext.replace(new RegExp('/recherche', "g"),'http://www.openbc.com/recherche');*/
	return zetext;
}

function getNumberOfPages() {
	var txt, urlStr='https://www.openbc.com/cgi-bin/contact.fpl?search_filter=&tags_filter=&notags_filter=0&op=undecided&offset=0';
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
					  var re = new RegExp('search_filter=&tags_filter=&notags_filter=0&op=undecided&offset=\\d*', "g");
					  var arr = responseDetails.responseText.match(re);
					  var numofcontacts=0;
					  if (arr) {
						  for (var i = 0; i<arr.length; i++) {
							  li_dbg('['+i+']'+arr[i]);
							  var pg = arr[i].lastIndexOf('=');
							  var num = parseInt(arr[i].substring(pg+1));
							  if(num>numofcontacts) {
								  numofcontacts=num;
							  }
						  }
					  } else {
						  li_dbg(':-(');
					  }
					  li_dbg('['+numofcontacts+']');
					  searchUrls((numofcontacts/10)+1);
				}
			}
		}
	};
	li_dbg("getNumberOFfContacts()");
	GM_xmlhttpRequest(request);
}


function li_dbg(str) {
	var date = new Date();
	GM_log(date+':'+str);
}



