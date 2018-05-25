// ==UserScript==
// @name	  transmet
// @namespace	  http://z.z
// @description	  Transmet une requete Viadeo et/ou LinkedIn
// @include	  http://www.Viadeo.com/miseenrelation/transfertdemande/*
// @version   	  0.1
// ==/UserScript==

var Viadeo=0;

var sigvfat='\n\nConnectez vous avec moi en recopiant cette addresse dans votre navigateur:\nhttp://www.Viadeo.com/contacts/invitationsimple/?from=adressbook&lastname=Mariacher&firstname=Eric&email=eric.mariacher@gmail.com&isMember=true\nMon Profil est accessible à http://www.Viadeo.com/recherche/profil/index.jsp?memberId=002rw1qp9bm4c75\nJ expose de façon détaillée mon but dans ce blog http://eric-mariacher.blogspot.com/2006/07/how-does-quantity-bring-quality.html. Je vous invite aujourd hui car je crois qu il faut faire croître son réseau tant qu on en a pas encore besoin: http://eric-mariacher.blogspot.com/2006/05/my-2-cents-about-online-business.html'; 
var sigveat='\n\nConnect with me by copying this address in your browser:\nhttp://www.Viadeo.com/contacts/invitationsimple/?from=adressbook&lastname=Mariacher&firstname=Eric&email=eric.mariacher@gmail.com&isMember=true\nMy Profile is accessible at http://www.Viadeo.com/recherche/profil/index.jsp?memberId=002rw1qp9bm4c75\nI explain in more details my goals at http://eric-mariacher.blogspot.com/2006/07/how-does-quantity-bring-quality.html. I specifically invite YOU today because we should both grow our network while we don’t need it:http://eric-mariacher.blogspot.com/2006/05/my-2-cents-about-online-business.html'; 
var sigfpv='\n\n-Il faut faire croître son réseau tant qu on en a pas encore besoin: http://eric-mariacher.blogspot.com/2006/05/my-2-cents-about-online-business.html -'; 
var siglfat='\n\nMon email est eric.mariacher@gmail.com. Connectez vous avec moi en recopiant cette addresse dans votre navigateur:\nhttp://www.linkedin.com/inviteFromProfile?from=profile&key=2838075&firstName=Eric&lastName=Mariacher&isFromProfile=true\nMon Profil est accessible à http://www.linkedin.com/in/mariacher\nJ expose de façon détaillée mon but dans ce blog http://eric-mariacher.blogspot.com/2006/07/how-does-quantity-bring-quality.html. Je vous invite aujourd hui car je crois qu il faut faire croître son réseau tant qu on en a pas encore besoin: http://eric-mariacher.blogspot.com/2006/05/my-2-cents-about-online-business.html'; 
var sigleat='\n\nMy email is eric.mariacher@gmail.com. Connect with me by copying this address in your browser:\nhttp://www.linkedin.com/inviteFromProfile?from=profile&key=2838075&firstName=Eric&lastName=Mariacher&isFromProfile=true\nMy Profile is viewable à http://www.linkedin.com/in/mariacher\nI explain in more details my goals at http://eric-mariacher.blogspot.com/2006/07/how-does-quantity-bring-quality.html. I specifically invite YOU today because we should both grow our network while we don’t need it:http://eric-mariacher.blogspot.com/2006/05/my-2-cents-about-online-business.html';
var sigepv='\n\n-Grow your network while you don’t need it: http://eric-mariacher.blogspot.com/2006/05/my-2-cents-about-online-business.html -';

/*var sigvf='\n\n-Il faut faire croître son réseau tant qu on en a pas encore besoin: http://eric-mariacher.blogspot.com/2006/05/my-2-cents-about-online-business.html -'; 
var siglf='\n\n-Il faut faire croître son réseau tant qu on en a pas encore besoin: http://eric-mariacher.blogspot.com/2006/05/my-2-cents-about-online-business.html -'; 
var sigle='\n\n-Grow your network while you don’t need it: http://eric-mariacher.blogspot.com/2006/05/my-2-cents-about-online-business.html -';*/

function evtATransmettre() {
	var sig;
	li_dbg('evtATransmettre1');
	var prenom='AoXoMoXoA';
	allTextareas = document.getElementsByTagName('td');
	for (var i = 0; i < allTextareas.length; i++) {
		thisTextarea = allTextareas[i];
		var zestr="ajoutez un message pour ";
		var re=new RegExp(zestr,"g");
		var arr1 = thisTextarea.textContent.match(re);
		if (arr1) {
			var u=thisTextarea.textContent.indexOf(zestr)+zestr.length;
			prenom=thisTextarea.textContent.substring(u,thisTextarea.textContent.indexOf(' ',u));
		}
	}
	li_dbg('evtATransmettre2 '+ prenom);
	zetextareas = document.getElementsByName('acceptBody');
	if(zetextareas) {
		var ziselement=zetextareas[0];
		if(ziselement) {
			ziselement.parentNode.removeChild(ziselement);
			var zatelement=document.createElement("textarea");
			zatelement.setAttribute("rows", "20");
			zatelement.setAttribute("cols", "60");
			zatelement.setAttribute("name", "acceptBody");
			/*if(Viadeo==1){
				sig=sigvfat;
			} else {
				sig=siglfat;
			}*/
			sig=sigfpv;
			zatelement.innerHTML='Bonjour '+prenom+',\n\nMerci de faire suivre cette requête.\n\nBonne journée.' + sig;
			document.forms[0].insertBefore(zatelement, document.forms[0].firstChild);
			li_dbg('evtTransmet12');
		}
	}
	li_dbg('evtATransmettre3');
	allTextareas = document.getElementsByTagName('label');
	for (var i = 0; i < allTextareas.length; i++) {
		thisTextarea = allTextareas[i];
		var zestr="which will help ";
		var re=new RegExp(zestr,"g");
		var arr1 = thisTextarea.textContent.match(re);
		if (arr1) {
			var u=thisTextarea.textContent.indexOf(zestr)+zestr.length;
			prenom=thisTextarea.textContent.substring(u,thisTextarea.textContent.indexOf(' ',u));
		}
	}
	li_dbg('evtATransmettre4 '+ prenom);
	zetextareas = document.getElementsByName('comment');
	if(zetextareas) {
		var ziselement=zetextareas[0];
		if(ziselement) {
			ziselement.parentNode.removeChild(ziselement);
			var zatelement=document.createElement("textarea");
			zatelement.setAttribute("rows", "20");
			zatelement.setAttribute("cols", "60");
			zatelement.setAttribute("name", "comment");
			/*if(Viadeo==1){
				sig=sigvfat;
			} else {
				sig=siglfat;
			}*/
			sig=sigfpv;
			zatelement.innerHTML='Bonjour '+prenom+',\n\nMerci de faire suivre cette requête.\n\nBonne journée.' + sig;
			document.forms[1].insertBefore(zatelement, document.forms[1].firstChild);
			li_dbg('evtTransmet11');
		}
	}
	li_dbg('evtATransmettre5');
}


function evtPourVous() {
	var sig;
	li_dbg('evtPourVous1');
	var prenom='AoXoMoXoA';
	allTextareas = document.getElementsByTagName('td');
	for (var i = 0; i < allTextareas.length; i++) {
		thisTextarea = allTextareas[i];
		var zestr="ajoutez un message pour ";
		var re=new RegExp(zestr,"g");
		var arr1 = thisTextarea.textContent.match(re);
		if (arr1) {
			var u=thisTextarea.textContent.indexOf(zestr)+zestr.length;
			prenom=thisTextarea.textContent.substring(u,thisTextarea.textContent.indexOf(' ',u));
		}
	}
	li_dbg('evtPourVous2 '+ prenom);
	zetextareas = document.getElementsByName('acceptBody');
	if(zetextareas) {
		var ziselement=zetextareas[0];
		if(ziselement) {
			ziselement.parentNode.removeChild(ziselement);
			var zatelement=document.createElement("textarea");
			zatelement.setAttribute("rows", "20");
			zatelement.setAttribute("cols", "60");
			zatelement.setAttribute("name", "acceptBody");
			zatelement.innerHTML='Bonjour '+prenom+',\n\nVoici une requête pour vous.\n\nBonne journée.' + sigfpv;
			document.forms[0].insertBefore(zatelement, document.forms[0].firstChild);
			li_dbg('evtTransmet12');
		}
	}
	li_dbg('evtPourVous3');
	allTextareas = document.getElementsByTagName('label');
	for (var i = 0; i < allTextareas.length; i++) {
		thisTextarea = allTextareas[i];
		var zestr="which will help ";
		var re=new RegExp(zestr,"g");
		var arr1 = thisTextarea.textContent.match(re);
		if (arr1) {
			var u=thisTextarea.textContent.indexOf(zestr)+zestr.length;
			prenom=thisTextarea.textContent.substring(u,thisTextarea.textContent.indexOf(' ',u));
		}
	}
	li_dbg('evtPourVous4 '+ prenom);
	zetextareas = document.getElementsByName('comment');
	if(zetextareas) {
		var ziselement=zetextareas[0];
		if(ziselement) {
			ziselement.parentNode.removeChild(ziselement);
			var zatelement=document.createElement("textarea");
			zatelement.setAttribute("rows", "20");
			zatelement.setAttribute("cols", "60");
			zatelement.setAttribute("name", "comment");
			zatelement.innerHTML='Bonjour '+prenom+',\n\nVoici une requête pour vous.\n\nBonne journée.' + sigfpv;
			document.forms[1].insertBefore(zatelement, document.forms[1].firstChild);
			li_dbg('evtTransmet11');
		}
	}
	li_dbg('evtPourVous5');
}


function evtForYou() {
	var sig;
	li_dbg('evtForYou1');
	var prenom='AoXoMoXoA';
	allTextareas = document.getElementsByTagName('td');
	for (var i = 0; i < allTextareas.length; i++) {
		thisTextarea = allTextareas[i];
		var zestr="ajoutez un message pour ";
		var re=new RegExp(zestr,"g");
		var arr1 = thisTextarea.textContent.match(re);
		if (arr1) {
			var u=thisTextarea.textContent.indexOf(zestr)+zestr.length;
			prenom=thisTextarea.textContent.substring(u,thisTextarea.textContent.indexOf(' ',u));
		}
	}
	li_dbg('evtForYou2 '+ prenom);
	zetextareas = document.getElementsByName('acceptBody');
	if(zetextareas) {
		var ziselement=zetextareas[0];
		if(ziselement) {
			ziselement.parentNode.removeChild(ziselement);
			var zatelement=document.createElement("textarea");
			zatelement.setAttribute("rows", "20");
			zatelement.setAttribute("cols", "60");
			zatelement.setAttribute("name", "acceptBody");
			zatelement.innerHTML='Hello '+prenom+',\n\nHere is a request for you.\n\nHave a nice day.' + sigepv;
			document.forms[0].insertBefore(zatelement, document.forms[0].firstChild);
			li_dbg('evtTransmet12');
		}
	}
	li_dbg('evtForYou3');
	allTextareas = document.getElementsByTagName('label');
	for (var i = 0; i < allTextareas.length; i++) {
		thisTextarea = allTextareas[i];
		var zestr="which will help ";
		var re=new RegExp(zestr,"g");
		var arr1 = thisTextarea.textContent.match(re);
		if (arr1) {
			var u=thisTextarea.textContent.indexOf(zestr)+zestr.length;
			prenom=thisTextarea.textContent.substring(u,thisTextarea.textContent.indexOf(' ',u));
		}
	}
	li_dbg('evtForYou4 '+ prenom);
	zetextareas = document.getElementsByName('comment');
	if(zetextareas) {
		var ziselement=zetextareas[0];
		if(ziselement) {
			ziselement.parentNode.removeChild(ziselement);
			var zatelement=document.createElement("textarea");
			zatelement.setAttribute("rows", "20");
			zatelement.setAttribute("cols", "60");
			zatelement.setAttribute("name", "comment");
			zatelement.innerHTML='Hello '+prenom+',\n\nHere is a request for you.\n\nHave a nice day.' + sigepv;
			document.forms[1].insertBefore(zatelement, document.forms[1].firstChild);
			li_dbg('evtTransmet11');
		}
	}
	li_dbg('evtForYou5');
}


function evtToTransmit() {
	var sig;
	li_dbg('evtToTransmit1');
	var prenom='AoXoMoXoA';
	allTextareas = document.getElementsByTagName('td');
	for (var i = 0; i < allTextareas.length; i++) {
		thisTextarea = allTextareas[i];
		var zestr="ajoutez un message pour ";
		var re=new RegExp(zestr,"g");
		var arr1 = thisTextarea.textContent.match(re);
		if (arr1) {
			var u=thisTextarea.textContent.indexOf(zestr)+zestr.length;
			prenom=thisTextarea.textContent.substring(u,thisTextarea.textContent.indexOf(' ',u));
		}
	}
	li_dbg('evtToTransmit2 '+ prenom);
	zetextareas = document.getElementsByName('acceptBody');
	if(zetextareas) {
		var ziselement=zetextareas[0];
		if(ziselement) {
			ziselement.parentNode.removeChild(ziselement);
			var zatelement=document.createElement("textarea");
			zatelement.setAttribute("rows", "20");
			zatelement.setAttribute("cols", "60");
			zatelement.setAttribute("name", "acceptBody");
			/*if(Viadeo==1){
				sig=sigveat;
			} else {
				sig=sigleat;
			}*/
			sig=sigepv;
			zatelement.innerHTML='Hello '+prenom+',\n\nThanks to transmit this request.\n\nHave a nice day.' + sig;
			document.forms[0].insertBefore(zatelement, document.forms[0].firstChild);
			li_dbg('evtTransmet12');
		}
	}
	li_dbg('evtToTransmit3');
	allTextareas = document.getElementsByTagName('label');
	for (var i = 0; i < allTextareas.length; i++) {
		thisTextarea = allTextareas[i];
		var zestr="which will help ";
		var re=new RegExp(zestr,"g");
		var arr1 = thisTextarea.textContent.match(re);
		if (arr1) {
			var u=thisTextarea.textContent.indexOf(zestr)+zestr.length;
			prenom=thisTextarea.textContent.substring(u,thisTextarea.textContent.indexOf(' ',u));
		}
	}
	li_dbg('evtToTransmit4 '+ prenom);
	zetextareas = document.getElementsByName('comment');
	if(zetextareas) {
		var ziselement=zetextareas[0];
		if(ziselement) {
			ziselement.parentNode.removeChild(ziselement);
			var zatelement=document.createElement("textarea");
			zatelement.setAttribute("rows", "20");
			zatelement.setAttribute("cols", "60");
			zatelement.setAttribute("name", "comment");
			/*if(Viadeo==1){
				sig=sigveat;
			} else {
				sig=sigleat;
			}*/
			sig=sigepv;
			zatelement.innerHTML='Hello '+prenom+',\n\nThanks to transmit this request.\n\nHave a nice day.' + sig;
			document.forms[1].insertBefore(zatelement, document.forms[1].firstChild);
			li_dbg('evtTransmet11');
		}
	}
	li_dbg('evtToTransmit5');
}



function Initialize() {
	li_dbg('Initialize1');
	
	var allTextareas, thisTextarea;
	allTextareas = document.getElementsByTagName('title');
	for (var i = 0; i < allTextareas.length; i++) {
		thisTextarea = allTextareas[i];
		if(thisTextarea.textContent.indexOf('iadeo')>0) {
			Viadeo=1;
			break;
		}
	}
	var ovl = document.createElement("input");
	ovl.setAttribute("id", "atransmettre");
	ovl.setAttribute("type", "button");
	ovl.setAttribute("value", "A transmettre");
	ovl.style.position = "absolute";
	ovl.style.top = "200px";
	ovl.style.right = "12px";
	ovl.style.backgroundColor = "wheat";
	ovl.addEventListener('click', evtATransmettre, true);
	document.getElementsByTagName("body")[0].appendChild(ovl);
	
	var ov2 = document.createElement("input");
	ov2.setAttribute("id", "pourvous");
	ov2.setAttribute("type", "button");
	ov2.setAttribute("value", "Pour Vous");
	ov2.style.position = "absolute";
	ov2.style.top = "230px";
	ov2.style.right = "12px";
	ov2.style.backgroundColor = "wheat";
	ov2.addEventListener('click', evtPourVous, true);
	document.getElementsByTagName("body")[0].appendChild(ov2);
	
	/*if(Viadeo==0)*/ {
		var qvl = document.createElement("input");
		qvl.setAttribute("id", "totransmit");
		qvl.setAttribute("type", "button");
		qvl.setAttribute("value", "To transmit");
		qvl.style.position = "absolute";
		qvl.style.top = "260px";
		qvl.style.right = "12px";
		qvl.style.backgroundColor = "wheat";
		qvl.addEventListener('click', evtToTransmit, true);
		document.getElementsByTagName("body")[0].appendChild(qvl);
	
		var qv2 = document.createElement("input");
		qv2.setAttribute("id", "foryou");
		qv2.setAttribute("type", "button");
		qv2.setAttribute("value", "For You");
		qv2.style.position = "absolute";
		qv2.style.top = "290px";
		qv2.style.right = "12px";
		qv2.style.backgroundColor = "wheat";
		qv2.addEventListener('click', evtForYou, true);
		document.getElementsByTagName("body")[0].appendChild(qv2);
	}
}


Initialize();

function li_dbg(str) {
	var date = new Date();
	GM_log(date+':'+str);
}

