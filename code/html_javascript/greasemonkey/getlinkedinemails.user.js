// getlinkedinemails.user.js
// version 1.0
// 2006-11-8
// Copyright (c) 2006, Eric Mariacher
// Released under the GPL license
// http://www.gnu.org/copyleft/gpl.html
// ==UserScript==
// @name          Get Linkedin emails
// @namespace     http://z.z
// @description	  Moissonne les emails sur Linkedin
// @include       http://www.linkedin.com/profile?viewConns*
// ==/UserScript==

 // Global variables
  var var_fifo_buffer;
  var var_fifo_temp;
  var var_fifo_sepchar;
  var var_fifo_errorstatus;

var kwd='';
var lieu='';
var maxpageresult=0;
var urlasked=0;
var urlprocessed=0;

var resultpage=[''];

Initialize();


function evtMoissonne() {
	if (window.confirm("Moissonne des mails?")) {
		var btn = document.getElementById("moissonne_des_mails");
		btn.setAttribute("value", "Stop");
		li_dbg('evtMoissonne1');
		btn.removeEventListener('click', evtMoissonne, true);
		btn.addEventListener('click', evtStop, true);
		li_dbg('evtMoissonne2');
		getKeyword();
		li_dbg('evtMoissonne3');
		searchUrl(fifo_pop());
		li_dbg('evtMoissonne4');
	}
}

function evtStop() {
	if (window.confirm("Finished")) {
		li_dbg('evtStop1');
		var btn = document.getElementById("moissonne_des_mails");
		btn.setAttribute("value", "Stopped");
		for (var i = 0; i < resultpage.length; i++) {
			if(resultpage[i].length>4) {
				outputToTab('<html><head><title>Tous mes contacts Linkedin['+i+']</title></head><body>'+resultpage[i]+'<body><html>');
			}
		}
		btn.removeEventListener('click', evtStop, true);
		fifo_initialise();
		li_dbg('evtStop2');
	}
}

function Initialize() {
	li_dbg('YES!');
	fifo_initialise();
	var ovl = document.createElement("input");
	ovl.setAttribute("id", "moissonne_des_mails");
	ovl.setAttribute("type", "button");
	ovl.setAttribute("value", "Moissonne");
	ovl.style.position = "absolute";
	ovl.style.top = "100px";
	ovl.style.right = "12px";
	ovl.style.backgroundColor = "lightgreen";
	ovl.addEventListener('click', evtMoissonne, true);
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
			//li_dbg('-['+responseDetails.readyState+']');
			if (responseDetails.readyState == 4) {
				// only if "OK"
				//li_dbg('+['+responseDetails.status+']');
				if (responseDetails.status == 200) {
					li_dbg(' searchUrl('+urlStr+' returned ' + responseDetails.status +
					' ' + responseDetails.statusText + ' ' + responseDetails.responseText.length);
					output=Math.floor(Math.random()*resultpage.length);
					if(isSharedConnections(responseDetails.responseText)==0) {
						resultpage[output]=resultpage[output].concat(parseProfile4mail(responseDetails.responseText));
						var btn = document.getElementById("moissonne_des_mails");
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
function isSharedConnections(txt) {
		var deja_connecte=new RegExp('Shared Connections',"g");
	//li_dbg('z0');
	var arr0 = txt.match(deja_connecte);
	if(arr0) {
		return 1;
	} else {
		return 0;
	}

}

function parseProfile4mail(txt) {
	//li_dbg('z0 '+txt.length);
	var zetext='';
	var emailRegExp = /\b([a-zA-Z0-9_\-])+(\.([a-zA-Z0-9_\-])+)*@((\[(((([0-1])?([0-9])?[0-9])|(2[0-4][0-9])|(2[0-5][0-5])))\.(((([0-1])?([0-9])?[0-9])|(2[0-4][0-9])|(2[0-5][0-5])))\.(((([0-1])?([0-9])?[0-9])|(2[0-4][0-9])|(2[0-5][0-5])))\.(((([0-1])?([0-9])?[0-9])|(2[0-4][0-9])|(2[0-5][0-5]))\]))|((([a-zA-Z0-9])+(([\-])+([a-zA-Z0-9])+)*\.)+([a-zA-Z])+(([\-])+([a-zA-Z0-9])+)*))/ig;
	var arr2 = txt.match(emailRegExp);
	if (arr2) {
		for (var j = 0; j<arr2.length; j++) {
			var email = arr2[j];
				zetext = zetext.concat(email,', ');
		}
	}
	li_dbg('z5 '+zetext);
	return zetext;
}


function getKeyword() {
	li_dbg('getKeyword()-0');
	//var re=new RegExp('/profile\\p{Punct}viewConns=\\p{Punct}key=\\d+\\p{Punct}split_page=\\d+',"g");
	var re=new RegExp('key=\\d+',"g");
	var arr = document.URL.match(re);
	if (arr) {
		li_dbg('getKeyword()-1');
		for (var i = 0; i<arr.length; i++) {
			li_dbg('getKeyword()-2['+i+']'+arr[i]);
			kwd=arr[i];
			break;
		}
	}	
	allTextareas = document.getElementsByTagName('span');
	for (var i = 0; i < allTextareas.length; i++) {
		thisTextarea = allTextareas[i];
		//li_dbg(thisTextarea.textContent+'=3========');
		var re=new RegExp('\\(\\d+\\+*\\)',"g");
		var arr1 = thisTextarea.textContent.match(re);
		if (arr1) {
			for (var i = 0; i<arr1.length; i++) {
				var s = arr1[i];
				var indexbp = s.indexOf('+');
				var indexbk = s.indexOf(')');
				if(indexbp<0) {
					li_dbg(indexbk+')['+s+']');
					maxpageresult = Math.ceil(parseInt(trim(s.substring(1,indexbk))/60))+2;
					break;
				} else {
					li_dbg(indexbp+'+['+s+']');
					var allElements, thisElement;
					allElements = document.evaluate(
						'//*[@href]',
						document,
						null,
						XPathResult.UNORDERED_NODE_SNAPSHOT_TYPE,
						null);
					for (var i = 0; i < allElements.snapshotLength; i++) {
						thisElement = allElements.snapshotItem(i);
						var num=parseInt(thisElement.text);
						//li_dbg('a['+thisElement.text+'='+num+'='+isNumber(num)+']');
						if(num>maxpageresult) {
							maxpageresult = num;
						}
					}
					break;
				}
			}
			break;
		}
	}
	li_dbg('keyword['+kwd+'] maxpage ['+maxpageresult+']');	
		for (var i = maxpageresult; i > 0; i--) {
		fifo_push('http://www.linkedin.com/profile?viewConns=&'+kwd+'&split_page='+i);
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

function isNumber(a)
 {
      return typeof a == 'number' && isFinite(a);
 }

function li_dbg(str) {
	var date = new Date();
	GM_log(date+':'+str);
}



