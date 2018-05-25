// getviaducnames.user.js
// version 1.0
// 2006-11-8
// Copyright (c) 2006, Eric Mariacher
// Released under the GPL license
// http://www.gnu.org/copyleft/gpl.html
// ==UserScript==
// @name          Get Viaduc names
// @namespace     http://z.z
// @description	  Moissonne les names sur Viaduc
// @include       http://www.viaduc.com/recherche/*
// ==/UserScript==

 // Global variables
  var var_fifo_buffer;
  var var_fifo_temp;
  var var_fifo_sepchar;
  var var_fifo_errorstatus;

var societe='';
var lieu='';
var maxpageresult=0;
var urlasked=0;
var urlprocessed=0;

var resultpage=['',''];

Initialize();


function evtMoissonne() {
	if (window.confirm("Moissonne des noms?")) {
		var btn = document.getElementById("moissonne_des_noms");
		btn.setAttribute("value", "Stop");
		li_dbg('evtMoissonne1');
		btn.removeEventListener('click', evtMoissonne, true);
		btn.addEventListener('click', evtStop, true);
		li_dbg('evtMoissonne2');
		getKeyword();
		li_dbg('evtMoissonne3');
		parcoursLesFenetresResultats();
		li_dbg('evtMoissonne4');
	}
}

function evtStop() {
	if (window.confirm("Finished")) {
		li_dbg('evtStop1');
		var btn = document.getElementById("moissonne_des_noms");
		btn.setAttribute("value", "Stopped");
		for (var i = 0; i < resultpage.length; i++) {
			if(resultpage[i].length>4) {
				outputToTab('<html><head><title>['+societe+']['+lieu+']['+i+']</title></head><body>'+resultpage[i]+'<body><html>');
			}
		}
		btn.removeEventListener('click', evtStop, true);
		fifo_initialise();
		li_dbg('evtStop2');
	}
}

function Initialize() {
	fifo_initialise();
	var ovl = document.createElement("input");
	ovl.setAttribute("id", "moissonne_des_noms");
	ovl.setAttribute("type", "button");
	ovl.setAttribute("value", "MoissonneNoms");
	ovl.style.position = "absolute";
	ovl.style.top = "150px";
	ovl.style.right = "12px";
	ovl.style.backgroundColor = "wheat";
	ovl.addEventListener('click', evtMoissonne, true);
	document.getElementsByTagName("body")[0].appendChild(ovl);
}

function outputToTab(str) {
	GM_openInTab("data:text/html;charset=UTF-8," + encodeURI(str));
}

function parcoursLesFenetresResultats() {
	for (var i = 1; i < maxpageresult; i++) {
		fifo_push('http://www.viaduc.com/recherche/resultat/index.jsp?language=fr&company='+societe+'&location='+lieu+'&type=0&pageNumber='+i);
	}
	searchUrl(fifo_pop());
}

function searchUrl(urlStr) {
	// li_dbg('q '+urlStr);
	var output;
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
						//output=Math.floor(Math.random()*resultpage.length);
						resultpage[0]=resultpage[0].concat(parseProfile4nom(responseDetails.responseText));
					var btn = document.getElementById("moissonne_des_noms");
					urlprocessed++;
					btn.setAttribute("value", "["+fifo_countelements()+"-"+urlasked+"-"+urlprocessed+"]");
					//btn.setAttribute("value", "["+urlasked+"-"+urlprocessed+"]");
					li_dbg('u9['+fifo_countelements()+'-'+urlasked+'-'+urlprocessed+']');
					if(fifo_countelements()!=0) {
						pausecomp(Math.random()*2000);
						searchUrl(fifo_pop());
					} else {
						for (var i = 0; i < resultpage.length; i++) {
							if(resultpage[i].length>4) {
								outputToTab('<html><head><title>['+societe+']['+lieu+']['+i+']</title></head><body>'+resultpage[i]+'<body><html>');
							}
						}
					}
				}
			}
		}
	};
	urlasked++;
	GM_xmlhttpRequest(request);
}

function parseProfile4nom(txt) {
	li_dbg('z0 '+txt.length);
	var zetext='';
	//li_dbg('z1');
	var re=new RegExp('span class="bold">[^<]*</span',"g");
	var arr1 = txt.match(re);
	if (arr1) {
		for (var i = 0; i<arr1.length; i++) {
			var s = arr1[i];
			nom=s.substring(s.indexOf('>')+1,s.indexOf('<'));
			if(nom.length>6) {
				zetext = zetext.concat('=',nom,'%<br/>');
			}
		}
	}
	li_dbg('z5 '+zetext);
	return zetext;
}

function getKeyword() {
	li_dbg('YES!');
	var allTextareas, thisTextarea;
	allTextareas = document.getElementsByTagName('div');
	for (var i = 0; i < allTextareas.length; i++) {
		thisTextarea = allTextareas[i];
		var ktag='Société : ';
		var indexbk = thisTextarea.textContent.indexOf(ktag);
		var ltag='Lieu : ';
		var indexbl = thisTextarea.textContent.indexOf(ltag);
		li_dbg(thisTextarea.textContent+'=1========'+indexbk+'=2========'+indexbl);
		if(indexbk>=0) {
			indexbk+=ktag.length;
			if(indexbl>=indexbk) {
				societe=trim(thisTextarea.textContent.substring(indexbk,indexbl));
			} else {
				societe=trim(thisTextarea.textContent.substring(indexbk));
			}
			li_dbg('keyword['+societe+'] '+indexbk);
		}
		if(indexbl>=0) {
			indexbl+=ltag.length;
			var indexbs=thisTextarea.textContent.indexOf('\n',indexbl);
			if(indexbs>=indexbl) {
				lieu=trim(thisTextarea.textContent.substring(indexbl,indexbs));
			} else {
				lieu=trim(thisTextarea.textContent.substring(indexbl));
			}
			li_dbg('lieu['+lieu+'] '+indexbl+ ' ' + indexbs);
		}
		if((indexbl>=0)||(indexbk>=0)) {
			break;
		}
	}
	allTextareas = document.getElementsByTagName('td');
	for (var i = 0; i < allTextareas.length; i++) {
		thisTextarea = allTextareas[i];
		//li_dbg(thisTextarea.textContent+'=3========');
		var re=new RegExp('\\d+ membres correspondent à votre recherche',"g");
		var arr1 = thisTextarea.textContent.match(re);
		if (arr1) {
			for (var i = 0; i<arr1.length; i++) {
				var s = arr1[i];
				var indexbk = s.indexOf(' membre');
				li_dbg(indexbk+'['+s+']');
				maxpageresult = Math.ceil(parseInt(trim(s.substring(0,indexbk))/10))+1;
				if(maxpageresult>40) {
					maxpageresult=40;
				}
			}
			break;
		}
	}
	li_dbg('societe['+societe+'] lieu['+lieu+'] maxpage ['+maxpageresult+']');	
}


  // Call to initialise system
  function fifo_initialise(){
   var_fifo_buffer=""
   var_fifo_temp=""
   var_fifo_sepchar="~"
  }

  // Call to finalise system
  function fifo_finalise(){
  }

  // Push a new element into the buffer
  function fifo_push(newelement){
   output=true
   var_fifo_temp=newelement
   if (var_fifo_temp.length==0){
    fifo_seterror("there is nothing to push!")
    output=false
    } else {
     if (var_fifo_temp.indexOf(var_fifo_sepchar)>0){
      fifo_geterror("illegal character");
      } else {
      var_fifo_buffer=var_fifo_buffer+var_fifo_temp+var_fifo_sepchar
     }
    }
   return(output)
  }

  // Pop an element from the buffer
  function fifo_pop(){
   newlen=0
   var_fotjeff_temp=""
   if (var_fifo_buffer.length==0){
    fifo_geterror("there is nothing to pop!")
    } else {
     if (var_fifo_buffer.indexOf(var_fifo_sepchar)==-1){
      fifo_geterror("the "+var_fifo_sepchar+" character is illegal in the buffer")
      } else {
      var_fifo_temp=var_fifo_buffer.substr(0,(var_fifo_buffer.indexOf(var_fifo_sepchar)))
      newlen=var_fifo_buffer.length-(var_fifo_buffer.indexOf(var_fifo_sepchar))
      var_fifo_buffer=var_fifo_buffer.substr((var_fifo_buffer.indexOf(var_fifo_sepchar)+1),newlen)
     }
    }
    return(var_fifo_temp)
  }

  // Read the current buffer
  function fifo_readbuffer(){
   return(var_fifo_buffer)
  }

  // Set the current buffer
  function fifo_writebuffer(newvalue){
   var_fifo_buffer=newvalue
  }

  // Reset the current buffer
  function fifo_reset(){
   var_fifo_buffer=""
  }

  // Count the number of elements in the buffer
  function fifo_countelements(){
   var count=0;
   for (i=0; i<var_fifo_buffer.length; i++){
    if (var_fifo_buffer.substr(i,1)==var_fifo_sepchar){
    count=count+1;
    }
   }
	/*var arr0 = txt.match(new RegExp(var_fifo_sepchar,"g"));
	if(arr0) {
		count = arr0.length;
	}*/
   return(count)
  }

  // Set the error status flag
  function fifo_seterror(message){
   var_fifo_errorstatus=message
  }

  // Get the error status flag
  function fifo_geterror(message){
   return(var_fifo_errorstatus)
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



