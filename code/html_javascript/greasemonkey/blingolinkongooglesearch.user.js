/*
Blingoogle
version 2.0
Simple script that puts a link at the top of Google search
pages to do the same search on Blingo.  Basically it's just
a reminder to go back to the site to try and win stuff.

If you haven't joined up yet, here's my invite link:
http://www.blingo.com/friends?ref=Y8PCYdrYUZkXWMuR4JQ1%2F9idgpo

*/
// ==UserScript==
// @name          Blingo link on Google Search
// @namespace     http://userscripts.org/scripts/show/1088
// @description	  Adds a Blingo link to Google's Search page
// @include       http://google.com/search*
// @include       http://www.google.com/search*
// ==/UserScript==

if (document.forms.length > 0)
{
	var res = document.location.search.match(/(\?|&)q=([^&]+)/);
	if (res.length == 3)
	{
		var bl = document.createElement('a');
		bl.href = 'http://www.blingo.com/search?q=' + res[2];
		bl.innerHTML = 'Blingo';

		var space = document.createElement('span');
		space.innerHTML = '&nbsp;&nbsp;&nbsp;&nbsp;';

		var x = document.evaluate("//a[@class='q']", document, null, XPathResult.UNORDERED_NODE_SNAPSHOT_TYPE, null);
		var t = x.snapshotItem(0);
		t.parentNode.insertBefore(bl, t);
		t.parentNode.insertBefore(space, t);
	}
}






