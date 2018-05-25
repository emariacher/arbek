// contactsviaduc.user.js
// version 1.0
// 2006-11-8
// Copyright (c) 2006, Eric Mariacher
// Released under the GPL license
// http://www.gnu.org/copyleft/gpl.html
// ==UserScript==
// @name          Efface Contacts Viaduc
// @namespace     http://mariacr.multimania.com/scripts
// @description	  Affiche sur une seule page tous mes contacts Viaduc
// @include       http://www.viaduc.com/contacts/carnetadresses/*
// @version	  1.0
// ==/UserScript==

 // Global variables
  var var_fifo_buffer;
  var var_fifo_temp;
  var var_fifo_sepchar;
  var var_fifo_errorstatus;
  var urlasked=0;
  var urlprocessed=0;



var resultpage=[''];

Initialize();


function evtEfface() {
	if (window.confirm("Efface mes contacts?")) {
		var btn = document.getElementById("efface_mes_contacts");
		btn.setAttribute("value", "Stop");
		li_dbg('there');
		btn.removeEventListener('click', evtEfface, true);
		btn.addEventListener('click', evtStop, true);
		li_dbg('evtEfface1');
		getKeyword();
		li_dbg('evtEfface2');
		searchUrl(fifo_pop());
		li_dbg('evtEfface3');
	}
}

function evtStop() {
	if (window.confirm("Finished")) {
		var btn = document.getElementById("efface_mes_contacts");
		btn.setAttribute("value", "Stopped");
		for (var i = 0; i < resultpage.length; i++) {
			if(resultpage[i].length>4) {
				outputToTab('<html><head><title>Tous mes contacts Viaduc['+i+']</title></head><body>'+resultpage[i].replace(new RegExp('</table><table>', "g"),'')+'<body><html>');
			}
		}
		fifo_initialise();
		btn.removeEventListener('click', evtStop, true);
	}
}

function Initialize() {
	fifo_initialise();
	var ovl = document.createElement("input");
	ovl.setAttribute("id", "efface_mes_contacts");
	ovl.setAttribute("type", "button");
	ovl.setAttribute("value", "Efface");
	ovl.style.position = "absolute";
	ovl.style.top = "24px";
	ovl.style.right = "12px";
	ovl.style.backgroundColor = "orange";
	ovl.addEventListener('click', evtEfface, true);
	document.getElementsByTagName("body")[0].appendChild(ovl);
}

function outputToTab(str) {
	GM_openInTab("data:text/html;charset=UTF-8," + encodeURI(str));
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
					output=Math.floor(Math.random()*resultpage.length);
					resultpage[output]=resultpage[output].concat(parseProfile4mail(responseDetails.responseText));
					var btn = document.getElementById("efface_mes_contacts");
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
								outputToTab('<html><head><title>Emails Linkedin['+i+']</title></head><body>'+resultpage[i]+'<body><html>');
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

function parseProfile4mail(txt) {
	li_dbg('z0 '+txt.length);
	var zetext='';
	var emailRegExp = /\b([a-zA-Z0-9_\-])+(\.([a-zA-Z0-9_\-])+)*@((\[(((([0-1])?([0-9])?[0-9])|(2[0-4][0-9])|(2[0-5][0-5])))\.(((([0-1])?([0-9])?[0-9])|(2[0-4][0-9])|(2[0-5][0-5])))\.(((([0-1])?([0-9])?[0-9])|(2[0-4][0-9])|(2[0-5][0-5])))\.(((([0-1])?([0-9])?[0-9])|(2[0-4][0-9])|(2[0-5][0-5]))\]))|((([a-zA-Z0-9])+(([\-])+([a-zA-Z0-9])+)*\.)+([a-zA-Z])+(([\-])+([a-zA-Z0-9])+)*))/ig;
	var arr2 = txt.match(emailRegExp);
	if (arr2) {
		for (var j = 0; j<arr2.length; j++) {
			var email = arr2[j];
			zetext = zetext.concat(email,'<br/>');
		}
	}
	li_dbg('z5 '+zetext);
	return zetext;
}

function getKeyword() {
	li_dbg('YES!');
	var allTextareas, thisTextarea;
	allTextareas = document.getElementsByTagName('td');
	for (var i = 0; i < allTextareas.length; i++) {
		thisTextarea = allTextareas[i];
		//li_dbg(thisTextarea.textContent+'=3========');
		var re=new RegExp('\\d+ personnes dont',"g");
		var arr1 = thisTextarea.textContent.match(re);
		if (arr1) {
			for (var i = 0; i<arr1.length; i++) {
				var s = arr1[i];
				var indexbk = s.indexOf(' personnes');
				li_dbg(indexbk+'['+s+']');
				maxpageresult = Math.ceil(parseInt(trim(s.substring(0,indexbk))/15))+1;
			}
			break;
		}
	}
	li_dbg('maxpage ['+maxpageresult+']');
	for (var i = maxpageresult; i > 0; i--) {
		fifo_push('http://www.viaduc.com/contacts/carnetadresses/index.jsp?&pageNumber='+i);
	}
	
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



