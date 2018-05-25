// The onOpen function is executed automatically every time a Spreadsseet is opened
function onOpen() {
  var ss = SpreadsheetApp.getActiveSpreadsheet();
  var menuEntries = [];
  menuEntries.push({name: "updateStatus", functionName: "updateStatus"});
  menuEntries.push({name: "writeStatuses", functionName: "writeStatuses"});
  ss.addMenu("MACROS", menuEntries);
}

function onEdit(event) {
  var ss = event.source.getActivesheet();
  var ac = event.source.getActiveCell();
  var lastColumn = ss.getLastColumn();
  update(ss,ac,lastColumn);
}

function updateStatus() {
  var ss = SpreadsheetApp.getActiveSheet();
  var ac = ss.getActiveCell();
  var lastColumn = ss.getLastColumn();
  update(ss,ac,lastColumn);
}

function update(ss,ac,lastColumn) {
  var ar = ac.getRow();
  var cell = ss.getRange(ar,8);
  var tbd = ss.getRange(ar,4).getValue().toUpperCase();
  if(tbd.length<2) {
    return;
  }
  var statusValues = ss.getRange(ar,11,1,lastColumn-9).getValues()[0];
  var buildNumDirection = getbuildNumDirection(ss);
  var k, status;
  if(buildNumDirection.indexOf("left2right")==0) {
    for(k = statusValues.length-1; k>-1; k--) {
      status = statusValues[k];
      if(status.length>2) {
        cell.setValue(status);
        break;
      }
    }
  } else {
    for(k = 0; k<statusValues.length; k++) {
      status = statusValues[k];
      if(status.length>2) {
        cell.setValue(status);
        break;
      }
    }
  }

  var row = ss.getRange(ar,1,1,lastColumn);
  var statusu = status.toUpperCase();
  if(tbd.indexOf("NOT USED")==0) {
    row.setBackgroundColor("#C0C0C0");
    row.setFontColor("#808080");
  } else if(statusu.indexOf("VERIFIED")==0) {
    row.setBackgroundColor("#CCFFCC");
  } else if(statusu.indexOf("NEED_DECISION")==0) {
    row.setBackgroundColor("#FF9900");
  } else if(status.length==0) {
    row.setBackgroundColor("white");
  } else {
    row.setBackgroundColor("red");
  }
}

function getbuildNumDirection(ss) {
  var lastColumn = ss.getLastColumn();
  var buildNumDirection = ss.getRange("J1").getValue();
  if(buildNumDirection.indexOf("left2right")==0) {
    return buildNumDirection;
  } else if(buildNumDirection.indexOf("right2left")==0) {
    return buildNumDirection;
  } else {
    var buildValues = ss.getRange(1,11,1,lastColumn-9).getValues()[0];
    var status11 = parseInt(buildValues[0].substr(1,4));
    var statusLast = parseInt(buildValues[buildValues.length-2].substr(1,4));
    if(statusLast-status11>0) {
      buildNumDirection = "left2right";
    } else if(statusLast-status11<0) {
      buildNumDirection = "right2left";
    } else {
      buildNumDirection = "unknown";
    }
    ss.getRange("J1").setValue(buildNumDirection);
    return buildNumDirection;
  }
}
function writeStatuses() {
  var sheets = SpreadsheetApp.getActiveSpreadsheet().getSheets();
  var i, row, tbd, lastRow, lastColumn;
  for(i = 0; ss = sheets[i]; i++) {
    lastRow = ss.getLastRow();
    lastColumn = ss.getLastColumn();
    for(row=2;row<=lastRow;row++) {
      tbd = ss.getRange(row,4).getValue();
      if(tbd.length<2) {
        break;
      }
      update(ss,ss.setActiveCell("A"+row),lastColumn);
    }
  }
}
