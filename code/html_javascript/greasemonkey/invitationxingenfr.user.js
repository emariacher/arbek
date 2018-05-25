// ==UserScript==
// @name	  invitationxing
// @namespace	  http://z.z
// @description	  Transmet une requete Viaduc et/ou LinkedIn
// @include	  https://www.xing.com/app/contact*
// @version   	  0.1
// ==/UserScript==


function evtSuisseSimEn() {
	li_dbg('evtSuisseSimEn1');
	zetextareas = document.getElementsByName('reason');
	if(zetextareas) {
		var ziselement=zetextareas[0];
		if(ziselement) {
			ziselement.parentNode.removeChild(ziselement);
			var zatelement=document.createElement("textarea");
			zatelement.setAttribute("rows", "4");
			zatelement.setAttribute("cols", "45");
			zatelement.setAttribute("name", "reason");
			zatelement.innerHTML='We are both working in Switzerland in similar domains. I guess we could help each other.\nMy goal is to grow my network to maximize chances for both of us to discover or transmit new opportunities.';
			document.forms[1].insertBefore(zatelement, document.forms[1].firstChild);
			li_dbg('evtTransmetEnss12');
		}
	}
	li_dbg('evtSuisseSimEn3');
}


function evtSuisseGenEn() {
	li_dbg('evtSuisseGenEn1');
	zetextareas = document.getElementsByName('reason');
	if(zetextareas) {
		var ziselement=zetextareas[0];
		if(ziselement) {
			ziselement.parentNode.removeChild(ziselement);
			var zatelement=document.createElement("textarea");
			zatelement.setAttribute("rows", "4");
			zatelement.setAttribute("cols", "45");
			zatelement.setAttribute("name", "reason");
			zatelement.innerHTML='We are both working in Switzerland. I guess we could help each other.\nMy goal is to grow my network to maximize chances for both of us to discover or transmit new opportunities.';
			document.forms[1].insertBefore(zatelement, document.forms[1].firstChild);
			li_dbg('evtTransmetEnsg12');
		}
	}
	li_dbg('evtSuisseGenEn3');
}
//********************-------------------********************

function evtSuisseSim() {
	li_dbg('evtSuisseSim1');
	zetextareas = document.getElementsByName('reason');
	if(zetextareas) {
		var ziselement=zetextareas[0];
		if(ziselement) {
			ziselement.parentNode.removeChild(ziselement);
			var zatelement=document.createElement("textarea");
			zatelement.setAttribute("rows", "4");
			zatelement.setAttribute("cols", "45");
			zatelement.setAttribute("name", "reason");
			zatelement.innerHTML='Nous travaillons dans des domaines similaires en Suisse. Je pense donc que nous pourrions nous aider mutuellement.\nMon but est d étendre mon réseau pour maximiser les chances de faire vous découvrir ou transmettre de nouvelles opportunités.';
			document.forms[1].insertBefore(zatelement, document.forms[1].firstChild);
			li_dbg('evtTransmet12');
		}
	}
	li_dbg('evtSuisseSim3');
}


function evtSuisseGen() {
	li_dbg('evtSuisseGen1');
	zetextareas = document.getElementsByName('reason');
	if(zetextareas) {
		var ziselement=zetextareas[0];
		if(ziselement) {
			ziselement.parentNode.removeChild(ziselement);
			var zatelement=document.createElement("textarea");
			zatelement.setAttribute("rows", "4");
			zatelement.setAttribute("cols", "45");
			zatelement.setAttribute("name", "reason");
			zatelement.innerHTML='Nous travaillons tous les deux en Suisse. Je pense donc que nous pourrions nous aider mutuellement.\nMon but est d étendre mon réseau pour maximiser les chances de faire vous découvrir ou transmettre de nouvelles opportunités.';
			document.forms[1].insertBefore(zatelement, document.forms[1].firstChild);
			li_dbg('evtTransmet12');
		}
	}
	li_dbg('evtSuisseGen3');
}
//********************-------------------********************

function evtRomandieSim() {
	li_dbg('evtRomandieSim1');
	zetextareas = document.getElementsByName('reason');
	if(zetextareas) {
		var ziselement=zetextareas[0];
		if(ziselement) {
			ziselement.parentNode.removeChild(ziselement);
			var zatelement=document.createElement("textarea");
			zatelement.setAttribute("rows", "4");
			zatelement.setAttribute("cols", "45");
			zatelement.setAttribute("name", "reason");
			zatelement.innerHTML='Nous travaillons dans des domaines similaires en Suisse romande. Je pense donc que nous pourrions nous aider mutuellement.\nMon but est d étendre mon réseau pour maximiser les chances de faire vous découvrir ou transmettre de nouvelles opportunités.';
			document.forms[1].insertBefore(zatelement, document.forms[1].firstChild);
			li_dbg('evtTransmet12');
		}
	}
	li_dbg('evtRomandieSim3');
}


function evtRomandieGen() {
	li_dbg('evtRomandieGen1');
	zetextareas = document.getElementsByName('reason');
	if(zetextareas) {
		var ziselement=zetextareas[0];
		if(ziselement) {
			ziselement.parentNode.removeChild(ziselement);
			var zatelement=document.createElement("textarea");
			zatelement.setAttribute("rows", "4");
			zatelement.setAttribute("cols", "45");
			zatelement.setAttribute("name", "reason");
			zatelement.innerHTML='Nous travaillons tous les deux en Suisse romande. Je pense donc que nous pourrions nous aider mutuellement.\nMon but est d étendre mon réseau pour maximiser les chances de faire vous découvrir ou transmettre de nouvelles opportunités.';
			document.forms[1].insertBefore(zatelement, document.forms[1].firstChild);
			li_dbg('evtTransmet12');
		}
	}
	li_dbg('evtRomandieGen3');
}
//************************************************************

function Initialize() {
	li_dbg('Initialize1');
	var ovl = document.createElement("input");
	ovl.setAttribute("id", "SuisseSim");
	ovl.setAttribute("type", "button");
	ovl.setAttribute("value", "SuisseSim");
	ovl.style.position = "absolute";
	ovl.style.top = "200px";
	ovl.style.right = "12px";
	ovl.style.backgroundColor = "wheat";
	ovl.addEventListener('click', evtSuisseSim, true);
	document.getElementsByTagName("body")[0].appendChild(ovl);
	
	var ov2 = document.createElement("input");
	ov2.setAttribute("id", "SuisseGen");
	ov2.setAttribute("type", "button");
	ov2.setAttribute("value", "SuisseGen");
	ov2.style.position = "absolute";
	ov2.style.top = "230px";
	ov2.style.right = "12px";
	ov2.style.backgroundColor = "wheat";
	ov2.addEventListener('click', evtSuisseGen, true);
	document.getElementsByTagName("body")[0].appendChild(ov2);
	
	var qvl = document.createElement("input");
	qvl.setAttribute("id", "RomandieSim");
	qvl.setAttribute("type", "button");
	qvl.setAttribute("value", "RomandieSim");
	qvl.style.position = "absolute";
	qvl.style.top = "260px";
	qvl.style.right = "12px";
	qvl.style.backgroundColor = "wheat";
	qvl.addEventListener('click', evtRomandieSim, true);
	document.getElementsByTagName("body")[0].appendChild(qvl);
	
	var qv2 = document.createElement("input");
	qv2.setAttribute("id", "RomandieGen");
	qv2.setAttribute("type", "button");
	qv2.setAttribute("value", "RomandieGen");
	qv2.style.position = "absolute";
	qv2.style.top = "290px";
	qv2.style.right = "12px";
	qv2.style.backgroundColor = "wheat";
	qv2.addEventListener('click', evtRomandieGen, true);
	document.getElementsByTagName("body")[0].appendChild(qv2);
	
	var svl = document.createElement("input");
	svl.setAttribute("id", "SwissSim");
	svl.setAttribute("type", "button");
	svl.setAttribute("value", "SwissSim");
	svl.style.position = "absolute";
	svl.style.top = "320px";
	svl.style.right = "12px";
	svl.style.backgroundColor = "wheat";
	svl.addEventListener('click', evtSuisseSimEn, true);
	document.getElementsByTagName("body")[0].appendChild(svl);
	
	var sv2 = document.createElement("input");
	sv2.setAttribute("id", "SwissGen");
	sv2.setAttribute("type", "button");
	sv2.setAttribute("value", "SwissGen");
	sv2.style.position = "absolute";
	sv2.style.top = "350px";
	sv2.style.right = "12px";
	sv2.style.backgroundColor = "wheat";
	sv2.addEventListener('click', evtSuisseGenEn, true);
	document.getElementsByTagName("body")[0].appendChild(sv2);
	
}


Initialize();

function li_dbg(str) {
	var date = new Date();
	GM_log(date+':'+str);
}

