// ==UserScript==
// @name	  efface
// @namespace	  http://z.z
// @description	  Efface le carnet d addresse Viadeo
// @include	  http://www.viadeo.com/contacts/carnetadresses/*
// @version   	  0.1
// ==/UserScript==


function Initialize() {
	//li_dbg('Initialize1');
	
	/*var allButtons, thisButton;
	allButtons = document.evaluate(
	'//input[@onclick]',
	document,
	null,
	XPathResult.UNORDERED_NODE_SNAPSHOT_TYPE,
	null);
	for (var i = 0; i < allButtons.snapshotLength; i++) {
		thisButton = allButtons.snapshotItem(i);
		// do something with thisButton
		thisButton.parentNode.removeChild(thisButton);
	}*/

	
	var allInputareas, thisInputarea;
	allInputareas = document.getElementsByTagName('input');
	for (var i = 0; i < allInputareas.length; i++) {
		thisInputarea = allInputareas[i];
		thisInputarea.setAttribute("checked", "true");
	}
	//efface();
	/*var newdeleteButton=document.createElement("input");
	with (newdeleteButton){
		setAttribute("value","Efface_les_contacts...");
		setAttribute("name","DeleteBtn");
		setAttribute("type","submit");
		style.position = "absolute";
		style.top = "500px";
		style.right = "300px";
		style.backgroundColor = "wheat";
		addEventListener('click', efface, true);
	}
	document.getElementsByTagName("body")[0].appendChild(newdeleteButton);
	
	li_dbg('Initialize9');*/
}

function efface() {
	deleteContacts2();
	document.deleteForm.submit();
	li_dbg('efface9');
	return false;
}

function deleteContacts2() {
		li_dbg('deleteContacts1');

var chkBoxes = document.getElementsByTagName('INPUT');

leformResultat = document.getElementsByName('deleteForm');
var formResultat=leformResultat[0];

var idContact = "";
var nbSelected = 0;
var newHiddenId;
var j = 0;
for(i = 0; i < chkBoxes.length; i++)
{
		//li_dbg('deleteContacts2 '+ i + ' ' + chkBoxes[i].type + ' ' + chkBoxes[i].checked);
	if (chkBoxes[i].type == 'checkbox' && chkBoxes[i].checked)
	{	
		li_dbg('deleteContacts3 '+ i + ' ' + nbSelected);
		idContact = chkBoxes[i].getAttribute("Id");
		idContact = idContact.substring(3);
		newHiddenId = document.createElement('INPUT')
		with (newHiddenId){
			setAttribute("type", "hidden");
			setAttribute("name", "contactId"+ j++);
			setAttribute("value", idContact);
		}	
		formResultat.appendChild(newHiddenId);			
		nbSelected++;									
	}  	
}

/*if (0 == nbSelected) 
	alert(lang["functions.error.selectContact"]);
else if (!confirmAction("functions.confirm.delete")) 
	return false;*/

li_dbg('deleteContacts9 ' + nbSelected);
return nbSelected > 0;

}



Initialize();

function li_dbg(str) {
	var date = new Date();
	GM_log(date+':'+str);
}

