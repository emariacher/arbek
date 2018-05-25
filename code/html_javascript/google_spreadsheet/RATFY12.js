// The onOpen function is executed automatically every time a Spreadsheet is opened
function onOpen() {
  var ss = SpreadsheetApp.getActiveSpreadsheet();
  var menuEntries = [];
  menuEntries.push({name: "createBUsheets", functionName: "createBUsheets"});
  menuEntries.push({name: "createBUgraphs", functionName: "createBUgraphs"});
  menuEntries.push({name: "projectsWorkload", functionName: "projectsWorkload"});
  menuEntries.push({name: "projectDetail", functionName: "projectDetail"});
  menuEntries.push({name: "deleteBUsheets", functionName: "deleteBUsheets"});
  menuEntries.push({name: "deleteProjectSheets", functionName: "deleteProjectSheets"});
  ss.addMenu("MACROS", menuEntries);
}

function projectDetail() {
  var ss = SpreadsheetApp.getActiveSpreadsheet();
  var sprojects = ss.getSheetByName("Projects")
  var dateRange = sprojects.getRange("E1:AB2");
  var BUValues = sprojects.getRange("B3:B200").getValues();
  var BUU = BUValues.getUnique(); 
  var numBUU = BUU.length;
  var FMsheets = ss.getSheets().filter(isFMsheet);
  var numFMsheets = FMsheets.length;
  
  var projectRange1 = ss.getActiveSelection();
  var topRow = projectRange1.getRow();
  var leftColumn = projectRange1.getColumn();
  var numRows = projectRange1.getNumRows();
  var projectRange = SpreadsheetApp.getActiveSheet().getRange(topRow,leftColumn,parseInt(Math.min(numRows,5)),1);
//  var projectRange = SpreadsheetApp.getActiveSheet().getRange(topRow,leftColumn);
  var projectValues = projectRange.getValues()[0];
  
      
  var i, j, k, m, e, p;
  // loop through projects
  for(p = 0; p < projectValues.length; p++) {
    var project = projectValues[p];
    var projectIndex = getIndex([project]);
    var projectSheet;
    if(projectIndex>196) {
      Browser.msgBox("Project not Found:["+project+"]. Select a cell containing a project name.");
      break;
    } else {
      ss.insertSheet("-"+project, ss.getSheets().length);
      projectSheet = ss.getSheetByName("-"+project);
      projectSheet.getRange("A1").setValue(project);
      dateRange.copyTo(projectSheet.getRange("E1:AB2"));
      for(j= 4; j<=projectSheet.getMaxColumns(); j++) {
        projectSheet.setColumnWidth(j, 50);
      }
    }
    var row = 4;
    for(i = 0; sfm = FMsheets[i]; i++) {
      var rowStart, row;
      var sfmProjects = sfm.getRange("B3:B90").getValues();
      for(j = 0; j < sfmProjects.length; j++) {
        var sproject = sfmProjects[j];
        if(sproject[0].length>1) {         
          if(project.indexOf(sproject[0])==0) {
            var sprojectIndex = getIndex(sproject);
            var sbu = ss.getSheetByName(BUValues[projectIndex]);        
            if(sbu!=null) {
              sfm.getRange("A"+(j+3)+":T"+(j+3)).copyTo(projectSheet.getRange("A"+row+":T"+row));
              projectSheet.getRange("C"+row).setValue(sfm.getName());
              row++;
            } else {
            }
          }
        } else {
        }
        sprojects.getRange("A1").setValue(j+sproject+sfm.getName());
      }
    }
  }
}

function projectsWorkload() {
  var ss = SpreadsheetApp.getActiveSpreadsheet();
  var sprojects = ss.getSheetByName("Projects")
  var BUValues = sprojects.getRange("B3:B200").getValues()
  var BUU = BUValues.getUnique() 
  var numBUU = BUU.length
  var FMsheets = ss.getSheets().filter(isFMsheet);
  var numFMsheets = FMsheets.length;
  sprojects.getRange("E3:AB200").setValue(0);      
      
  var i, j, k, m, e;
  // loop through team sheets
  for(i = 0; sfm = FMsheets[i]; i++) {
    var rowStart, row;
    var sfmProjects = sfm.getRange("B3:B90").getValues();
    for(j = 0; j < sfmProjects.length; j++) {
      var sproject = sfmProjects[j];
      var unknownProject = sfm.getRange("C"+(j+3));
      if(sproject[0].length>1) {
        var projectIndex = getIndex(sproject);
        var sbu = ss.getSheetByName(BUValues[projectIndex]);        
        if(sbu!=null) {
          var already = sprojects.getRange("E"+(3+projectIndex)+":T"+(3+projectIndex)).getValues()[0];
          var toAdd = sfm.getRange("E"+(j+3)+":T"+(j+3)).getValues()[0];
          var sumed = addArrays(already,toAdd);
          sprojects.getRange("E"+(3+projectIndex)+":T"+(3+projectIndex)).setValues([sumed]);
          unknownProject.setValue("Project found");
          unknownProject.setBackgroundColor('lightgreen');
        } else {
          unknownProject.setValue("Unknown project!");
          unknownProject.setBackgroundColor('red');
        }
      } else {
        unknownProject.setValue(" ");
        unknownProject.setBackgroundColor('white');
      }
      sprojects.getRange("A1").setValue(j+sproject+sfm.getName())
    }
  }
}

function deleteBUsheets() {
  var ss = SpreadsheetApp.getActiveSpreadsheet();
  var sprojects = ss.getSheetByName("Projects")
  var BUValues = sprojects.getRange("B3:B200").getValues()
  var BUU = BUValues.getUnique() 
  var numBUU = BUU.length
  var i, j, k, m, e;
  for (i = 0; i<numBUU; i++) {
    e = BUU[i]
    var sbu = ss.getSheetByName(e);
    if(sbu != null) {
      ss.setActiveSheet(sbu);
      ss.deleteActiveSheet();
    }
  }
}

function deleteProjectSheets() {
  var ss = SpreadsheetApp.getActiveSpreadsheet();
  var Projectsheets = ss.getSheets().filter(isProjectsheet);
  var numProjects = Projectsheets.length
  var i, j, k, m, e;
  for (i = 0; i<numProjects; i++) {
    e = Projectsheets[i]
    ss.setActiveSheet(e);
    ss.deleteActiveSheet();
  }
}

function createBUsheets() {
  var teamFunctions = ["_EE", "_OP", "_CA", "_FW"];
  var ss = SpreadsheetApp.getActiveSpreadsheet();
  var sprojects = ss.getSheetByName("Projects")
  var dateRange = sprojects.getRange("E1:AB2");
  var BUValues = sprojects.getRange("B3:B200").getValues()
  var typeValues = sprojects.getRange("C3:C200").getValues() 
  typeValues.push("UnknownT");
  var projectValues = sprojects.getRange("D3:D200").getValues() 
  var FMsheets = ss.getSheets().filter(isFMsheet);
  var numFMsheets = FMsheets.length


  // Get list of unique BUs.  
  var BUU = getUnique2(BUValues) 
  BUValues.push("UnknownBU");
  var numBUU = BUU.length
      
  // Create or reset a sheet for each BU
  var i, j, k, m, e;
  for (i = 0; i<numBUU; i++) {
    e = BUU[i]
    var sbu = ss.getSheetByName(e);
    if(sbu != null) {
      sbu.getRange("A"+(7+(numFMsheets*2))+":AB205").clearContent();
    } else if(e.length>2){
      ss.insertSheet(e, ss.getSheets().length);
      sbu = ss.getSheetByName(e);
      dateRange.copyTo(sbu.getRange("E1:AB2"));
      for(j= 4; j<=sbu.getMaxColumns(); j++) {
        sbu.setColumnWidth(j, 50);
      }
      for(m=0;m<8;m++) {
        sbu.getRange(3,5+m).setFormulaR1C1("=SUM(R[2]C[0]:R["+(2+numFMsheets)+"]C[0])");
        sbu.getRange(5,5+m).setFormulaR1C1("=R[-4]C["+(m*2)+"]");
        sbu.getRange(209,5+m).setFormulaR1C1("=R[-208]C["+(m*2)+"]");
      }
      sbu.getRange(3,13).setFormulaR1C1("=MAX(R[0]C[-9]:R[0]C[-1])");
      sbu.getRange("M207").setFormulaR1C1("=MAX(R[0]C[-9]:R[0]C[-1])");
      row = 6;
      for(k = 0; sfm = FMsheets[k]; k++) {
        sbu.getRange("A"+(row+numFMsheets)).setValue(sfm.getName());
        sbu.getRange("E"+(row+numFMsheets)).setFormula("=(SUMIF($C$1:$C$200,$A"+(row+numFMsheets)+",E$1:E$200))");
        sbu.getRange("E"+(row+numFMsheets)).copyTo(sbu.getRange("F"+(row+numFMsheets)+":Z"+(row+numFMsheets)));
        sbu.getRange("D"+row).setValue(sfm.getName());
        sbu.getRange("B"+row).setValue(sfm.getName().substring(0,3));
        for(m=0;m<8;m++) {
          sbu.getRange(row,5+m).setFormulaR1C1("=SUM(R["+numFMsheets+"]C["+(m*2)+"]:R["+numFMsheets+"]C["+((m*2)+2)+"])");
        }
        row++;
      }
      for(k = 0; sfm = teamFunctions[k]; k++) {
        sbu.getRange("A"+(k+210)).setValue(sfm);
        sbu.getRange("E"+(k+210)).setFormula("=(SUMIF($B$1:$B$20,\""+sfm+"\",E$1:E$20))");
        sbu.getRange("E"+(k+210)).copyTo(sbu.getRange("F"+(k+210)+":N"+(k+210)));
        sbu.getRange("D"+(k+210)).setValue(sfm);
        for(m=0;m<8;m++) {
          sbu.getRange(207,5+m).setFormulaR1C1("=SUM(R[3]C[0]:R[15]C[0])");
        }
      }
    } else {
      continue;
    }
    sbu.getRange("A1").setValue(8+(numFMsheets*2))
  };
  
  // loop through team sheets
  for(i = 0; sfm = FMsheets[i]; i++) {
    var rowStart, row;
    var sfmProjects = sfm.getRange("B3:B90").getValues();
    sprojects.getRange("A1").setValue(sfm.getName());
    for(j = 0; j < sfmProjects.length; j++) {
      var sproject = sfmProjects[j];
      var unknownProject = sfm.getRange("C"+(j+3));
      if(sproject[0].length>1) {
        var projectIndex = getIndex(sproject);
        var sbu = ss.getSheetByName(BUValues[projectIndex]);        
        if(sbu!=null) {
          row = sbu.getRange("A1").getValue();
          sfm.getRange("A"+(j+3)+":AB"+(j+3)).copyTo(sbu.getRange("A"+row+":AB"+row), {contentsOnly:true});
          sbu.getRange("A"+row).setFormula("=SUM(R[0]C[4]:R[0]C[18])");
          sbu.getRange("D"+row).setValue(typeValues[projectIndex]+" "+j);
          sbu.getRange("C"+row).setValue(sfm.getName());
          //        sbu.getRange("O"+row).setValue((j+3) + " " + getIndex(sproject)+ " " + sbu.getName()+ " " + sproject + " " + typeValues[getIndex(sproject)]);
          sbu.getRange("A1").setValue(row+1);
          unknownProject.setValue("Project found");
          unknownProject.setBackgroundColor('lightgreen');
        } else {
          unknownProject.setValue("Unknown project!");
          unknownProject.setBackgroundColor('red');
        }
      } else {
        unknownProject.setValue(" ");
        unknownProject.setBackgroundColor('white');
      }
//      sprojects.getRange("A1").setValue(j+sproject+sfm.getName())
    }
  }
  sprojects.getRange("A1").setValue("finished!");
  createBUgraphs();
  sprojects.getRange("A1").setValue("graphs generated!");
}

function createBUgraphs() {
  var ss = SpreadsheetApp.getActiveSpreadsheet();
  var sprojects = ss.getSheetByName("Projects")
  var BUValues = sprojects.getRange("B3:B200").getValues()
  var FMsheets = ss.getSheets().filter(isFMsheet);
  var numFMsheets = FMsheets.length

  // Get list of unique BUs.  
  var BUU = getUnique2(BUValues); 
  var numBUU = BUU.length;
  
  var i;
  for (i = 0; i<numBUU; i++) {
    var e = BUU[i]
    var sbu = ss.getSheetByName(e);
    if(sbu != null) {
      sbu.getRange("B1").setValue(sbu.getName());
      drawBarChart(sbu, sbu.getRange("D5:H"+(5+numFMsheets)), sbu.getRange("M3").getValue());
      drawBarChart2(sbu, sbu.getRange("D209:H213"), sbu.getRange("M207").getValue());
    }
  }
}

function drawBarChart(sheet,range,maxval) {
  var i, j, k;
  var table = range.getValues();
  var labels = table[0].slice(1).join("|");
  var lblseries1 = new Array();
  var series1 = new Array();
  for(i=1;i<table.length;i++) {
    lblseries1.push(table[i][0]);
    series1.push(table[i].slice(1).join(","));
  }
  var lblseries = lblseries1.join("|");
  var series = series1.join("|"); 
  
  var chartApi = "https://chart.googleapis.com/chart?cht=bvs&";
  var url = chartApi + "chs=400x300&chco=FFCC99,CCFFFF,CCCCFF,99CC00,FFFFCC,FFCC00,969696,FF8080,9999FF,FF80FF,CCCCCC&chtt=Resources+for+"+
      sheet.getName()+"+projects+detailed&chd=t:"+series+"&chds=0,"+maxval+"&chdl="+lblseries+
      "&chxt=x&chxl=0:|"+labels+
      "|&chbh=30,15,30&chm=N,FF0000,-1,,12|N,000000,0,,12,,c|N,000000,1,,12,,c|N,000000,2,,12,,c|N,000000,3,,12,,c|N,000000,4,,12,,c|N,000000,5,,12,,c|N,FFFFFF,6,,12,,c|N,000000,7,,12,,c|N,000000,8,,12,,c|N,000000,9,,12,,c|N,000000,10,,12,,c";

//  sheet.getRange("A4").setValue(url);
  sheet.insertImage(url, 5, 2);
//  sheet.getRange("C1").setValue(sheet.getName());
  return url;
}

function drawBarChart2(sheet,range,maxval) {
  var i, j, k;
  var table = range.getValues();
  var labels = table[0].slice(1).join("|");
  var lblseries1 = new Array();
  var series1 = new Array();
  for(i=1;i<table.length;i++) {
    lblseries1.push(table[i][0]);
    series1.push(table[i].slice(1).join(","));
  }
  var lblseries = lblseries1.join("|");
  var series = series1.join("|"); 
  
  var chartApi = "https://chart.googleapis.com/chart?cht=bvs&";
  var url = chartApi + "chs=400x300&chco=FFFFCC,CCFFFF,CCCCFF,99CC00,FFCC99,FFCC00,969696,FF8080,9999FF&chtt=Resources+for+"+
      sheet.getName()+"+projects&chd=t:"+series+"&chds=0,"+maxval+"&chdl="+lblseries+
      "&chxt=x&chxl=0:|"+labels+
      "|&chbh=30,15,30&chm=N,FF0000,-1,,12|N,000000,0,,12,,c|N,000000,1,,12,,c|N,000000,2,,12,,c|N,000000,3,,12,,c|N,000000,4,,12,,c|N,000000,5,,12,,c|N,000000,6,,12,,c|N,000000,7,,12,,c|N,000000,8,,12,,c";

  sheet.insertImage(url, 1, 2);
  return url;
}

function debugChart() {
  var ss = SpreadsheetApp.getActiveSpreadsheet();
  var sbu = ss.getSheetByName("CDBU");
  var url = drawBarChart(sbu,sbu.getRange("D5:F10"), sbu.getRange("M3").getValue());
  ss.msgBox(url);
}

Array.prototype.getUnique = function () {
  var o = new Object();
  var i, e;
  for (i = 0; e = this[i]; i++) {o[e] = 1};
  var a = new Array();
  for (e in o) {a.push (e)};
  return a;
}
  
function getUnique2(array) {
  var o = new Object();
  var i, e;
  for (i = 0; e = array[i]; i++) {o[e] = 1};
  var a = new Array();
  for (e in o) {a.push (e)};
  return a;
}
  
function isFMsheet(element, index, array) {
  return (element.getName().indexOf("_") == 0);
}

function isProjectsheet(element, index, array) {
  return (element.getName().indexOf("-") == 0);
}

function getIndex(project) {
  var ss = SpreadsheetApp.getActiveSpreadsheet();
  var sprojects = ss.getSheetByName("Projects");
  var projectValues = sprojects.getRange("D3:D200").getValues();
  var w = project[0]
  var i=0;
  for(e in projectValues) {
    var v = projectValues[e][0];
    if(v.length<2) {
      i=200;
      break;
    } 
    if(v==w) {
      break;
    }
    i++;
  }   
  return i;
}

var ParseInt = function(x) {
  var i = parseInt(x)
  if(isNaN(i)) {
    return 0;
  } else {
    return i;
  }
}


function debugAddArray() {
  var arr1 = ["1","2","3"];
  var arr2 = ["2","3","4"];
  var i_out = addArrays(arr1, arr2);
  document.writeln(i_out);
}

function addArrays(s_arr1, s_arr2) {
  var i_arr1 = s_arr1.map(ParseInt);
  var i_arr2 = s_arr2.map(ParseInt);
  var i_out = [];
  for(i=0; i < i_arr1.length;i++) {
    i_out.push(i_arr1[i]+i_arr2[i]);
  }
  return i_out;
}
