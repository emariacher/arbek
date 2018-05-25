// PostInterceptor
// version 0.6 ALPHA
// 2006-01-11
// Copyright (c) 2005, Prakash Kailasa <pk-moz at kailasa dot net>
// Released under the GPL license
// http://www.gnu.org/copyleft/gpl.html
//
// --------------------------------------------------------------------
//
// This is a Greasemonkey user script.
//
// To install, you need Greasemonkey: http://greasemonkey.mozdev.org/
// Then restart Firefox and revisit this script.
// Under Tools, there will be a new menu item to "Install User Script".
// Accept the default configuration and install.
//
// To uninstall, go to Tools -> Manage User Scripts,
// select "PostInterceptor", and click Uninstall.
//
// Add appropriate @include URL patterns, when installing the script.
// After installation, go to Tools -> Manage User Scripts, select
// "PostInterceptor" to add/edit URL patterns.
//
// --------------------------------------------------------------------
//
// ==UserScript==
// @name          PostIntercepter
// @description   Intercept POST requests and let user modify before submit
// @namespace     http://kailasa.net/prakash/greasemonkey/
// @include       http*://example.com/*
// ==/UserScript==
//
// IMPORTANT: Be sure to change/add @include lines for the sites you
//            want the script to work on
//
// --------------------------------------------------------------------
// 
// To enable/disable POST Interceptor, click on the small '[PI] is Off'
// or '[PI] is On' button on the bottom-right corner of the page.
// 
// --------------------------------------------------------------------
//
// TODO:
// - Handle radio buttons correctly
// - Implement add_form_param
// - Code needs cleanup
//
// --------------------------------------------------------------------

const POST_INTERCEPT = 'PostIntercept';
var intercept_on;
var is_modified = true;

interceptor_setup();

function toggle_intercept(flag)
{
    intercept_on = flag;
    GM_setValue(POST_INTERCEPT, intercept_on);
    setup_pi_button();
}

function setup_pi_button()
{
    var pi = document.getElementById('__pi_toggle');
    if (!pi) {
        pi = new_node('p', '__pi_toggle');
        pi.appendChild(new_text_node(''));
        document.getElementsByTagName('body')[0].appendChild(pi);

        var pi_toggle_style = ' \
#__pi_toggle { \
  position: fixed; \
  bottom: 0; right: 0; \
  padding: 2px; margin: 0; \
  font: caption; \
  font-weight: bold; \
  cursor: crosshair; \
  opacity: .5; \
} \
#__pi_toggle:hover { \
  border-width: 2px 0 0 2px; \
  border-style: solid none none solid; \
  border-color: black; \
} \
';
        add_style("__pi_toggle_style", pi_toggle_style);
        pi.addEventListener('click',
                            function() {toggle_intercept(!intercept_on)},
                            false);
    }

    if (intercept_on) {
        pi.firstChild.data = '[PI] is On';
        pi.setAttribute('title', 'Click to turn POST Interceptor Off');
        pi.style.backgroundColor = '#ff8';
        pi.style.color = '#008';
    } else {
        pi.firstChild.data = '[PI] is Off';
        pi.setAttribute('title', 'Click to turn POST Interceptor On');
        pi.style.backgroundColor = '#ccc';
        pi.style.color = '#444';
    }
}

function interceptor_setup()
{
    // nothing to do if no forms on the page
    if (document.forms.length == 0)
        return;

    if (typeof GM_getValue != 'undefined') {
        intercept_on = GM_getValue(POST_INTERCEPT, false);
        GM_log('intercept_on = ' + intercept_on);
        setup_pi_button();
    } else {
        intercept_on = true;
    }

    // override submit handling
    unsafeWindow.HTMLFormElement.prototype.real_submit = unsafeWindow.HTMLFormElement.prototype.submit;
    unsafeWindow.HTMLFormElement.prototype.submit = interceptor;

    // trap onsubmit handler, if any, for each form.
    // we will call the real onsubmit handler ourselves later
//     var forms = document.getElementsByTagName('form');
//     for (var i = 0; i < forms.length; i++) {
//         if (forms[i].method.toLowerCase() == 'post') {
//             if (forms[i].onsubmit) {
//                 forms[i].real_onsubmit = forms[i].onsubmit;
//                 forms[i].onsubmit = function(e) {
//                     return false;
//                 };
//             }
//         }
//     }
    // define our 'submit' handler on window, to avoid defining
    // on individual forms
    window.addEventListener('submit', function(e) {
                                interceptor(e);
                                e.preventDefault();
                            }, false);
}

// interceptor: called in place of form.submit()
// or as a result of submit handler on window (arg: event)
function interceptor(e) {
    var target = e ? e.target : this;
    GM_log('interceptor: target = <' + target + '>');

    var frm;
    // target could be an INPUT element
    if (target.tagName.toLowerCase() == 'input') {
        frm = target.form;
    } else {
        frm = target;
    }
    if (!frm || !frm.tagName || frm.tagName.toLowerCase() != 'form') {
        GM_log('interceptor: found <' + frm + (frm.tagName ? '(' + frm.tagName + ')' : '') + '> instead of form; investigate further!!!');
    }

    if (!interceptor_onsubmit(frm))
        return false;

    //alert('interceptor: intercept_on = ' + intercept_on);
    if (intercept_on) {
	li_dbg(frm);
        show(frm);
        return false;
    } else {
	li_dbg(frm);
        frm.real_submit();
    }
}

// if any form defined an onsubmit handler, it was saved earlier.
// call it now
function interceptor_onsubmit(f)
{
    var rc = true;
    if (f.real_onsubmit) {
        rc = f.real_onsubmit();
    }
    return rc;
}

function show(frm)
{
    var content = build(frm);
    content.open();
}

function build(frm)
{
    add_window_style();

    var container = new_node('div', 'post_interceptor');
    container.className = '__pi_window';
    var title = new_node('h1');
    title.className = '__pi_title';
    title.appendChild(new_text_node('Intercepting POST ' + post_url(frm)));
    container.appendChild(title);

    var note = new_node('div', '__pi_note');
    note.appendChild(new_text_node('Click on any value to modify it'));
    //title.appendChild(note);
    container.appendChild(note);

    var data = build_post_data(frm);
    container.appendChild(data);

    var buttons = new_node('div', '__pi_buttons');
    var btn_send_mod = new_node('button', '__pi_btn_send_mod');
    btn_send_mod.className = '__pi_button';
    btn_send_mod.appendChild(new_text_node('Send Modified'));
    buttons.appendChild(btn_send_mod);
    btn_send_mod.addEventListener('click', function(e) { submit_modified(win) }, false);

    var btn_send_orig = new_node('button', '__pi_btn_send_orig');
    btn_send_orig.className = '__pi_button';
    btn_send_orig.appendChild(new_text_node('Send Original'));
    buttons.appendChild(btn_send_orig);
    btn_send_orig.addEventListener('click', function(e) { submit_original(win) }, false);

    var btn_cancel = new_node('button', '__pi_btn_cancel');
    btn_cancel.className = '__pi_button';
    btn_cancel.appendChild(new_text_node('Cancel'));
    buttons.appendChild(btn_cancel);
    container.appendChild(buttons);
    btn_cancel.addEventListener('click', function(e) { cancel_submit(win) }, false);

    var win = Window(container);

    container.form = frm;

    return win;
}

// POST content
function build_post_data(f)
{
    var table = new_node('table');

    // heading
    var thead = new_node('thead');
    var th_row = new_node('tr');
    var attrs = new Array('+', 'name', 'type', 'value');
    for (var a = 0; a < attrs.length; a++) {
        var th = new_node('th');
        th.appendChild(new_text_node(attrs[a].ucFirst()));
        th_row.appendChild(th);
    }
    th_row.firstChild.setAttribute('title', 'Click to add a new parameter');
    th_row.firstChild.addEventListener('click', add_form_param, false);
    thead.appendChild(th_row);
    table.appendChild(thead);

    // data
    var tbody = new_node('tbody');
    var el_count = 0;
    for (var i = 0; i < f.elements.length; i++) {
        if (!f.elements[i].name)
            continue;
        var row = new_node('tr');
        row.className = el_count++ % 2 == 0 ? '__pi_row_even' : '__pi_row_odd';
        var cell_ctrl = new_node('td', '__pi_cell_ctrl_' + i);
        cell_ctrl.className = '__pi_cell_ctrl';
        cell_ctrl.appendChild(new_text_node('X'));
        cell_ctrl.setAttribute('title', 'Click to delete');
        cell_ctrl.addEventListener('click', toggle_form_param, false);
        row.appendChild(cell_ctrl);

        //for (var a in attrs) {
        for (var a = 1; a < attrs.length; a++) {
            var cell = new_node('td', '__pi_cell_' + attrs[a] + '_' + i);
            cell.className = '__pi_cell_' + attrs[a];
            var data;
            if (attrs[a] == 'value') {
                //data = new_node('span', '__pi_cell_value_text_' + i);
                //data.appendChild(new_text_node(f.elements[i][attrs[a]]));
                data = new_node('input', '__pi_cell_value_text_' + i);
                data.value = f.elements[i][attrs[a]];
                data.readOnly = true;
                data.className = '__pi_view_field';
                data.maxLength = 1000;
                cell.addEventListener("click", show_edit, false);
            } else {
                data = new_text_node(f.elements[i][attrs[a]]);
            }
            cell.appendChild(data);
            row.appendChild(cell);
        }
        tbody.appendChild(row);
    }
    table.appendChild(tbody);
    var data = new_node('div', '__pi_post_info');
    data.className = '__pi_post_info';
    data.appendChild(table);

    return data;
}

// hide value and show edit field

function show_edit(e)
{
    var view, cell;
    if (e.target.nodeName == 'INPUT') {
        view = e.target;
        cell = view.parentNode;
    } else {
        cell = e.target;
        view = cell.firstChild;
    }
    view.__origValue = view.value;
    view.className = '__pi_edit_field';
    view.readOnly = false;
    view.addEventListener("blur", show_view, false);
}

// hide edit field and show modified value
function show_view(e)
{
    var view = e.target;
    view.className = '__pi_view_field';
    view.addEventListener("click", show_edit, false);
    /*if (view.value != view.__origValue)*/ {
        is_modified = true;
        view.parentNode.parentNode.className += ' __pi_modified';
    }
}

// build POST url
function post_url(f)
{
    // absolute URL?
    if (f.action.match(/^https?:/))
        return f.action;

    // relative URL; build complete URL
    var url = document.location.protocol + '//' + document.location.host;
    if (f.action.match(/^\//)) {
        url += f.action;
    } else {
        url += document.location.pathname + '/' + f.action;
    }
    li_dbg(url);
    return url;
}

// delete/undelete a form parameter
function toggle_form_param(e)
{
    var cell = e.target;
    var deleted = cell.parentNode.getAttribute('deleted');
    if (deleted && deleted == 'true') {
        cell.parentNode.setAttribute('deleted', 'false');
        cell.textContent = 'X';
        cell.setAttribute('title', 'Click to delete');
        cell.parentNode.className = cell.parentNode.className.replace(' __pi_deleted', '');
    } else {
        cell.parentNode.setAttribute('deleted', 'true');
        cell.textContent = '+';
        cell.setAttribute('title', 'Click to undo delete');
        cell.parentNode.className += ' __pi_deleted';
        is_modified = true;
    }
}

// add a new form parameter
function add_form_param(e)
{
    window.alert('Sorry! not implemented yet');
}

// cancel submit; just close the Interceptor window
function cancel_submit(win)
{
    win.close();
}

// ignore form modifications and submit original form
function submit_original(win)
{
    win.close();
    win.frame.form.real_submit();
}

// submit form with modified parameters
function submit_modified(win)
{
    if (is_modified) {
        update_form(win);
    }
    submit_original(win);
}

// update the form being submitted with user modifications
function update_form(win)
{
    var f = win.frame.form;
    var diff = 'submitting ' + f.name + ':\n';
    var tobeposted= '__';
    for (var i = 0; i < f.elements.length; i++) {
        if (!f.elements[i].name)
            continue;
	tobeposted += f.elements[i].name + '-' + f.elements[i].value + ' ';
        var edit = document.getElementById('__pi_cell_value_text_' + i);
        if (edit && edit.value != f.elements[i].value) {
            diff += f.elements[i].name + ': |' + f.elements[i].value + '| -> |' + edit.value + '|\n';
            // update the original form param
            f.elements[i].value = edit.value;
        }
        var deleted = document.getElementById('__pi_cell_ctrl_' + i).parentNode.getAttribute('deleted');
        if (deleted && deleted == 'true') {
            f.elements[i].disabled = true;
        }
    }
    GM_log(diff);
    li_dbg(tobeposted);
}

// helper functions
function new_node(type, id)
{
    var node = document.createElement(type);
    if (id && id.length > 0)
        node.id = id;
    return node;
}

function new_text_node(txt)
{
    return document.createTextNode(txt);
}

function add_style(style_id, style_rules)
{
    if (document.getElementById(style_id))
        return;

    var style = new_node("style", style_id);
    style.type = "text/css";
    style.innerHTML = style_rules;
    document.getElementsByTagName('head')[0].appendChild(style);
}

// style for the interceptor window
function add_window_style()
{
    var pi_style_rules = ' \
.post_interceptor { \
  margin: 0; padding: 0; \
} \
 \
.__pi_window { \
  background-color: #bfbfff; \
  border-color: #000040; \
  border-style: solid; \
  border-width: 2px; \
  /* opacity: .90; */                           \
  margin: 0px; \
  padding: 1px 2px; \
  position: absolute; \
  text-align: center; \
  visibility: hidden; \
  z-index: 1000; \
 \
  -moz-border-radius: 15px; \
} \
 \
.__pi_title { \
  background-color: #4040ff; \
  color: #ffffff; \
  margin: 1px; padding: 1px; \
  font: caption; \
  font-weight: bold; \
  text-align: center; \
  white-space: nowrap; \
  overflow: hidden; \
 \
  -moz-border-radius: 20px; \
} \
 \
#__pi_note { \
  border: solid 0px black; \
  color: #800000; \
  margin: 0; \
  font: caption; \
  font-weight: bold; \
  text-align: center; \
} \
 \
#__pi_buttons { \
  width: 99%; \
  text-align: center; \
  position: absolute; \
  bottom: 5px; \
} \
 \
.__pi_button { \
  background-color: #4040ff; \
  color: #fff; \
  margin: 0 5px; padding: 2px; \
  font: icon; \
  font-weight: bold; \
 \
  -moz-border-radius: 15px; \
} \
 \
.__pi_button:hover { \
  background-color: #ff4040; \
  cursor: pointer; \
} \
 \
.__pi_post_info { \
  max-height: 335px; \
  overflow: auto; \
  margin: 3px 2px; padding: 0; \
  border: 1px solid #008080; \
} \
 \
.__pi_post_info table { \
  width: 100%; \
  font: bold .7em "sans serif"; \
} \
 \
.__pi_post_info table thead tr { \
  background-color: black; \
  color: white; \
} \
 \
.__pi_post_info table td { \
  text-align: left; \
} \
 \
.__pi_row_odd { \
  background-color: #eee; \
} \
 \
.__pi_row_even { \
  background-color: #ccc; \
} \
 \
.__pi_cell_ctrl { \
  font: caption; \
  font-weight: bold; \
  color: white; \
  background-color: black; \
  text-align: center; \
  width: 1.5em; \
  cursor: crosshair; \
} \
 \
.__pi_view_field { \
  background-color: inherit; \
  border: 0px solid black; \
  width: 25em; \
  font: bold 1em "sans serif"; \
} \
 \
.__pi_edit_field { \
  background-color: #ffc; \
  color: blue; \
  border: 1px solid black; \
  padding: -1px; \
  width: 25em; \
  font: bold 1em "sans serif"; \
} \
 \
tr.__pi_modified td, tr.__pi_modified input { \
  color: red; \
} \
 \
tr.__pi_deleted td, tr.__pi_deleted input { \
  color: gray; \
} \
 \
';
    add_style("__pi_style", pi_style_rules);
}

//===============================================================
// Popup Window 

function Window(el)
{
    document.getElementsByTagName('body')[0].appendChild(el);

    var win = {
        frame: el,

        open: function() {
            var width = 550;
            var height = 400;
            this.frame.style.width = width + 'px';
            this.frame.style.height = height + 'px';
            this.frame.style.left = parseInt(window.scrollX + (window.innerWidth - width)/2) + 'px';
            this.frame.style.top = parseInt(window.scrollY + (window.innerHeight - height)/2) + 'px';
            this.frame.style.visibility = "visible";
        },

        close: function() {
            this.frame.style.visibility = "hidden";
        },
    };

    return win;
}

String.prototype.ucFirst = function () {
    return this.charAt(0).toUpperCase() + this.substr(1);
}

function li_dbg(str) {
	var date = new Date();
	GM_log(date+':'+str);
}

/*
 * Change Log:
 *
 * 0.6  - 2006-01-11 - Updated to work with Firefox 1.5 and Greasemonkey 0.6.4
 * 0.5  - 2005-07-26 - One minor change to make this work in GM 0.4.1 (Mark Pilgrim)
 *                   - Removed check for input type 'image', when target is determined (Umesh Shankar)
 *                   - Form parameters can be deleted, before submitting
 *                   - Skip form parameters without 'name' attribute (takes care of fieldsets)
 *                   - minor style changes
 * 0.4  - 2005-05-09 - Add a toggle "button" at the bottom-right corder
 *                   - implement edit fields using 'input' elements
 *                   - more pleasing colors (I hope) and rounded corners
 *                   - remove Menu Commands until GM_unregisterMenuCommand is implemented
 * 0.3  - 2005-04-20 - remove dependency on GM 0.3
 * 0.2  - 2005-04-18 - build full POST URL
 * 0.1  - 2005-04-17 - initial version
 */
