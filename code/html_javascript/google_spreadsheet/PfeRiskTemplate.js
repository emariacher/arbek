function onEdit(event){
//  var r = event.source.getActiveRange();
//  r.setComment("Last modified: " + (new Date()));
  
  var ss = SpreadsheetApp.getActiveSpreadsheet();
  var rr = ss.getSheetByName("Risk Registry");
  var as = ss.getActiveSheet();
//  var ac = as.getActiveCell();   
  rr.getRange("A1").setValue("cluck: "+GoogleClock());
}

function recalculate(clock) {
  var ss = SpreadsheetApp.getActiveSpreadsheet();
  var rr = ss.getSheetByName("Risk Registry");
  var as = ss.getActiveSheet();
//  var ac = as.getActiveCell();   
  return "clock: "+clock;
}

var flatten = function(x) {
    return x[0];
}


function update() {
  var ss = SpreadsheetApp.getActiveSpreadsheet();
  var un = ss.getSheetByName("USER NOTES");
  var rr = ss.getSheetByName("Risk Registry");
  var categoryList =  un.getRange("A36:A38").getValues().map(flatten);
  var areaList  = un.getRange("E35:E46").getValues().map(flatten);
  var statusList = un.getRange("E27:E32").getValues().map(flatten);
  var count = 0;
  for(r=3;r<16;r++) {
    var s = rr.getRange("M"+r).getValue();
    var a = rr.getRange("A"+r).getValue();
    var c = rr.getRange("G"+r).getValue();
    var ic = categoryList.indexOf(c);
  }
}

function meetCondition(area,status,category) {
  var ss = SpreadsheetApp.getActiveSpreadsheet();
  var rr = ss.getSheetByName("Risk Registry")
  var count = 0;
  for(r=3;r<16;r++) {
    var s = rr.getRange("G"+r).getValue();
    var a = rr.getRange("A"+r).getValue();
    var c = rr.getRange("M"+r).getValue();
    if((c.indexOf(category)==0)&&(a.indexOf(area)==0)&&(s.indexOf(status)==0)) {
      count++;
    }
  }
  return count;  
}

function meetCondition2(area,status,category,clock) {
  var ss = SpreadsheetApp.getActiveSpreadsheet();
  var rr = ss.getSheetByName("Risk Registry")
  var count = 0;
  for(r=3;r<16;r++) {
    var s = rr.getRange("G"+r).getValue();
    var a = rr.getRange("A"+r).getValue();
    var c = rr.getRange("M"+r).getValue();
    if((c.indexOf(category)==0)&&(a.indexOf(area)==0)&&(s.indexOf(status)==0)) {
      count++;
    }
  }
  return count;  
}

function meetConditionDebug(area,status,category,num) {
  var ss = SpreadsheetApp.getActiveSpreadsheet();
  var rr = ss.getSheetByName("Risk Registry")
  var count = 0;
  for(r=3;r<16;r++) {
    var s = rr.getRange("G"+r).getValue();
    var a = rr.getRange("A"+r).getValue();
    var c = rr.getRange("M"+r).getValue();
    if((c.indexOf(category)==0)&&(a.indexOf(area)==0)&&(s.indexOf(status)==0)) {
      count++;
    }
  }
  return num+" "+count+" "+rr.getRange("M"+num).getValue()+"=="+category+
    " "+rr.getRange("A"+num).getValue()+"=="+area+
    " "+rr.getRange("G"+num).getValue()+"=="+status;  
}

function maxRisk(area) {
  var ss = SpreadsheetApp.getActiveSpreadsheet();
  var rr = ss.getSheetByName("Risk Registry")
  var maxrisk = 0;
  for(r=3;r<16;r++) {
    var risk = rr.getRange("F"+r).getValue();
    var a = rr.getRange("A"+r).getValue();
    if(a.indexOf(area)==0) {
      maxrisk = Math.max(maxrisk, risk);
    }
  }
  return maxrisk;
}

function averageRisk(area) {
  var ss = SpreadsheetApp.getActiveSpreadsheet();
  var rr = ss.getSheetByName("Risk Registry")
  var risks = [];
  for(r=3;r<16;r++) {
    var risk = rr.getRange("F"+r).getValue();
    var a = rr.getRange("A"+r).getValue();
    if(a.indexOf(area)==0) {
      risks.push(risk);
    }
  }
  return risks.avg(risks);
}

function maxRisk2(area,clock) {
  var ss = SpreadsheetApp.getActiveSpreadsheet();
  var rr = ss.getSheetByName("Risk Registry")
  var maxrisk = 0;
  for(r=3;r<16;r++) {
    var risk = rr.getRange("F"+r).getValue();
    var a = rr.getRange("A"+r).getValue();
    if(a.indexOf(area)==0) {
      maxrisk = Math.max(maxrisk, risk);
    }
  }
  return maxrisk;
}

function averageRisk2(area,clock) {
  var ss = SpreadsheetApp.getActiveSpreadsheet();
  var rr = ss.getSheetByName("Risk Registry")
  var risks = [];
  for(r=3;r<16;r++) {
    var risk = rr.getRange("F"+r).getValue();
    var a = rr.getRange("A"+r).getValue();
    if(a.indexOf(area)==0) {
      risks.push(risk);
    }
  }
  return risks.avg(risks);
}

Array.prototype.avg = function() {
var av = 0;
var cnt = 0;
var len = this.length;
for (var i = 0; i < len; i++) {
var e = +this[i];
if(!e && this[i] !== 0 && this[i] !== '0') e--;
if (this[i] == e) {av += e; cnt++;}
}
return av/cnt;
}
