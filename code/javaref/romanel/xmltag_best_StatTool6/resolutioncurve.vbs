Sub Macro1()
'
' Macro1 Macro
' Macro recorded 02/27/2009 by Eric Mariacher
'

'
    Range("A1:E18").Select
    Charts.Add
    ActiveChart.ApplyCustomType ChartType:=xlBuiltIn, TypeName:= _
        "Line - Column on 2 Axes"
    ActiveChart.SetSourceData Source:=Sheets("_A_eQuadDJPlatformD").Range( _
        "A1:E18"), PlotBy:=xlColumns
    ActiveChart.Location Where:=xlLocationAsObject, Name:="_A_eQuadDJPlatformD"
    With ActiveChart
        .HasTitle = True
        .ChartTitle.Characters.Text = "Bug resolution trend"
        .Axes(xlCategory, xlPrimary).HasTitle = False
        .Axes(xlValue, xlPrimary).HasTitle = True
        .Axes(xlValue, xlPrimary).AxisTitle.Characters.Text = "Number of Bugs"
        .Axes(xlCategory, xlSecondary).HasTitle = False
        .Axes(xlValue, xlSecondary).HasTitle = True
        .Axes(xlValue, xlSecondary).AxisTitle.Characters.Text = _
        "Bug Resolution Duration (days)"
    End With
    ActiveChart.Legend.Select
    ActiveChart.SeriesCollection(3).Select
    With Selection.Border
        .ColorIndex = 43
        .Weight = xlMedium
        .LineStyle = xlContinuous
    End With
    With Selection
        .MarkerBackgroundColorIndex = xlAutomatic
        .MarkerForegroundColorIndex = xlAutomatic
        .MarkerStyle = xlNone
        .Smooth = False
        .MarkerSize = 7
        .Shadow = False
    End With
    With Selection.Border
        .ColorIndex = 43
        .Weight = xlMedium
        .LineStyle = xlContinuous
    End With
    With Selection
        .MarkerBackgroundColorIndex = xlNone
        .MarkerForegroundColorIndex = xlNone
        .MarkerStyle = xlNone
        .Smooth = False
        .MarkerSize = 7
        .Shadow = False
    End With
    ActiveChart.SeriesCollection(4).Select
    With Selection.Border
        .ColorIndex = 3
        .Weight = xlMedium
        .LineStyle = xlContinuous
    End With
    With Selection
        .MarkerBackgroundColorIndex = xlNone
        .MarkerForegroundColorIndex = xlAutomatic
        .MarkerStyle = xlNone
        .Smooth = False
        .MarkerSize = 5
        .Shadow = False
    End With
    ActiveChart.PlotArea.Select
    ActiveChart.ChartArea.Select
    ActiveChart.PlotArea.Select
    ActiveChart.SeriesCollection(3).Select
    With Selection.Border
        .ColorIndex = 4
        .Weight = xlMedium
        .LineStyle = xlContinuous
    End With
    With Selection
        .MarkerBackgroundColorIndex = xlNone
        .MarkerForegroundColorIndex = xlNone
        .MarkerStyle = xlNone
        .Smooth = False
        .MarkerSize = 7
        .Shadow = False
    End With
    ActiveChart.SeriesCollection(2).Select
    With Selection.Border
        .ColorIndex = 43
        .Weight = xlThin
        .LineStyle = xlContinuous
    End With
    Selection.Shadow = False
    Selection.InvertIfNegative = False
    Selection.Interior.ColorIndex = xlAutomatic
    With Selection.Border
        .Weight = xlThin
        .LineStyle = xlNone
    End With
    Selection.Shadow = False
    Selection.InvertIfNegative = False
    With Selection.Interior
        .ColorIndex = 46
        .Pattern = xlSolid
    End With
    ActiveChart.SeriesCollection(1).Select
    With Selection.Border
        .Weight = xlThin
        .LineStyle = xlAutomatic
    End With
    Selection.Shadow = False
    Selection.InvertIfNegative = False
    With Selection.Interior
        .ColorIndex = 43
        .Pattern = xlSolid
    End With
    With Selection.Border
        .Weight = xlThin
        .LineStyle = xlNone
    End With
    Selection.Shadow = False
    Selection.InvertIfNegative = False
    With Selection.Interior
        .ColorIndex = 10
        .Pattern = xlSolid
    End With
    ActiveWindow.Visible = False
    ActiveSheet.ChartObjects("Chart 1").Activate
    Windows("TTf7A8D.tmp41.xml").Activate
    ActiveWindow.Visible = False
    ActiveSheet.ChartObjects("Chart 1").Activate
    Windows("TTf7A8D.tmp41.xml").Activate
    ActiveChart.SeriesCollection(3).Select
    With Selection.Border
        .ColorIndex = 10
        .Weight = xlMedium
        .LineStyle = xlContinuous
    End With
    With Selection
        .MarkerBackgroundColorIndex = xlNone
        .MarkerForegroundColorIndex = xlNone
        .MarkerStyle = xlNone
        .Smooth = False
        .MarkerSize = 7
        .Shadow = False
    End With
    ActiveChart.SeriesCollection(1).Select
    With Selection.Border
        .Weight = xlHairline
        .LineStyle = xlNone
    End With
    Selection.Shadow = False
    Selection.InvertIfNegative = False
    With Selection.Interior
        .ColorIndex = 43
        .Pattern = xlSolid
    End With
    With Selection.Border
        .Weight = xlHairline
        .LineStyle = xlNone
    End With
    Selection.Shadow = False
    Selection.InvertIfNegative = False
    With Selection.Interior
        .ColorIndex = 4
        .Pattern = xlSolid
    End With
    ActiveChart.PlotArea.Select
    ActiveChart.SeriesCollection(1).Select
    With Selection.Border
        .Weight = xlHairline
        .LineStyle = xlNone
    End With
    Selection.Shadow = False
    Selection.InvertIfNegative = False
    With Selection.Interior
        .ColorIndex = 43
        .Pattern = xlSolid
    End With
    ActiveChart.SeriesCollection(2).Select
    With Selection.Border
        .Weight = xlHairline
        .LineStyle = xlNone
    End With
    Selection.Shadow = False
    Selection.InvertIfNegative = False
    With Selection.Interior
        .ColorIndex = 45
        .Pattern = xlSolid
    End With
    ActiveChart.ChartArea.Select
    ActiveChart.PlotArea.Select
    With Selection.Border
        .ColorIndex = 2
        .Weight = xlThin
        .LineStyle = xlContinuous
    End With
    With Selection.Interior
        .ColorIndex = 15
        .PatternColorIndex = 1
        .Pattern = xlSolid
    End With
    With Selection.Border
        .ColorIndex = 48
        .Weight = xlThin
        .LineStyle = xlContinuous
    End With
    With Selection.Interior
        .ColorIndex = 2
        .PatternColorIndex = 1
        .Pattern = xlSolid
    End With
    ActiveChart.ChartArea.Select
End Sub
Sub Macro2()
'
' Macro2 Macro
' Macro recorded 02/27/2009 by Eric Mariacher
'

'
    Range("A1:E18").Select
    Charts.Add
    ActiveChart.ApplyCustomType ChartType:=xlBuiltIn, TypeName:= _
        "Line - Column on 2 Axes"
    ActiveChart.SetSourceData Source:=Sheets("_A_eQuadDJPlatformD").Range( _
        "A1:E18"), PlotBy:=xlColumns
    ActiveChart.Location Where:=xlLocationAsObject, Name:="_A_eQuadDJPlatformD"
    With ActiveChart
        .HasTitle = True
        .ChartTitle.Characters.Text = "Bug Resolution Trend"
        .Axes(xlCategory, xlPrimary).HasTitle = False
        .Axes(xlValue, xlPrimary).HasTitle = True
        .Axes(xlValue, xlPrimary).AxisTitle.Characters.Text = "Number of Bugs"
        .Axes(xlCategory, xlSecondary).HasTitle = False
        .Axes(xlValue, xlSecondary).HasTitle = True
        .Axes(xlValue, xlSecondary).AxisTitle.Characters.Text = _
        "Bug resolution Duration (days)"
    End With
    ActiveSheet.Shapes("Chart 2").IncrementLeft 179.25
    ActiveSheet.Shapes("Chart 2").IncrementTop 135.75
    ActiveChart.PlotArea.Select
    With Selection.Border
        .ColorIndex = 16
        .Weight = xlThin
        .LineStyle = xlContinuous
    End With
    With Selection.Interior
        .ColorIndex = 2
        .PatternColorIndex = 1
        .Pattern = xlSolid
    End With
    ActiveChart.SeriesCollection(3).Select
    With Selection.Border
        .ColorIndex = 10
        .Weight = xlMedium
        .LineStyle = xlContinuous
    End With
    With Selection
        .MarkerBackgroundColorIndex = xlAutomatic
        .MarkerForegroundColorIndex = xlAutomatic
        .MarkerStyle = xlNone
        .Smooth = False
        .MarkerSize = 7
        .Shadow = False
    End With
    ActiveChart.SeriesCollection(4).Select
    With Selection.Border
        .ColorIndex = 3
        .Weight = xlMedium
        .LineStyle = xlContinuous
    End With
    With Selection
        .MarkerBackgroundColorIndex = xlNone
        .MarkerForegroundColorIndex = xlAutomatic
        .MarkerStyle = xlNone
        .Smooth = False
        .MarkerSize = 5
        .Shadow = False
    End With
    ActiveChart.SeriesCollection(2).Select
    With Selection.Border
        .Weight = xlThin
        .LineStyle = xlNone
    End With
    Selection.Shadow = False
    Selection.InvertIfNegative = False
    With Selection.Interior
        .ColorIndex = 45
        .Pattern = xlSolid
    End With
    ActiveChart.SeriesCollection(1).Select
    With Selection.Border
        .Weight = xlThin
        .LineStyle = xlNone
    End With
    Selection.Shadow = False
    Selection.InvertIfNegative = False
    With Selection.Interior
        .ColorIndex = 43
        .Pattern = xlSolid
    End With
    ActiveChart.ChartArea.Select
    ActiveSheet.Shapes("Chart 2").ScaleWidth 1.32, msoFalse, _
        msoScaleFromBottomRight
    ActiveSheet.Shapes("Chart 2").ScaleHeight 1.42, msoFalse, _
        msoScaleFromBottomRight
End Sub

