Sub TestStatusTables()
'
' TestStatusTables Macro
' Macro recorded 3/21/2006 by Eric Mariacher
'
'
    Sheets("Caliber_Table").Select
    Charts.Add
    With ActiveChart
    .ChartType = xlColumnStacked
    .SetSourceData Source:=Sheets("Caliber_Table").Range("J2:N2,J3:N6"), PlotBy:=xlRows
    .SeriesCollection(1).Name = "=Caliber_Table!R3C9"
    .SeriesCollection(2).Name = "=Caliber_Table!R4C9"
    .SeriesCollection(3).Name = "=Caliber_Table!R5C9"
    .SeriesCollection(4).Name = "=Caliber_Table!R6C9"
    .Location Where:=xlLocationAsNewSheet
        .HasTitle = True
        .ChartTitle.Characters.Text = "Pulse tests (number of tests)"
        .Axes(xlCategory, xlPrimary).HasTitle = False
        .Axes(xlValue, xlPrimary).HasTitle = True
        .Axes(xlValue, xlPrimary).AxisTitle.Characters.Text = "number of tests"
        .Name = "PulseTestNumbers"
    End With
    Sheets("Caliber_Table").Select
    Charts.Add
    With ActiveChart
    .ChartType = xlColumnStacked
    .SetSourceData Source:=Sheets("Caliber_Table").Range("J2:N2,J7:N10"), PlotBy:=xlRows
    .SeriesCollection(1).Name = "=Caliber_Table!R3C9"
    .SeriesCollection(2).Name = "=Caliber_Table!R4C9"
    .SeriesCollection(3).Name = "=Caliber_Table!R5C9"
    .SeriesCollection(4).Name = "=Caliber_Table!R6C9"
    .Location Where:=xlLocationAsNewSheet
        .HasTitle = True
        .ChartTitle.Characters.Text = "Pulse tests (effort in days)"
        .Axes(xlCategory, xlPrimary).HasTitle = False
        .Axes(xlValue, xlPrimary).HasTitle = True
        .Axes(xlValue, xlPrimary).AxisTitle.Characters.Text = "effort in days"
        .Name = "PulseTestEffort"
    End With
    Sheets("Caliber_Table").Select
    Charts.Add
    With ActiveChart
    .ChartType = xlColumnStacked
    .SetSourceData Source:=Sheets("Caliber_Table").Range("O2:Y2,O3:Y6"), PlotBy:=xlRows
    .SeriesCollection(1).Name = "=Caliber_Table!R3C9"
    .SeriesCollection(2).Name = "=Caliber_Table!R4C9"
    .SeriesCollection(3).Name = "=Caliber_Table!R5C9"
    .SeriesCollection(4).Name = "=Caliber_Table!R6C9"
    .Location Where:=xlLocationAsNewSheet
        .HasTitle = True
        .ChartTitle.Characters.Text = "Floyd tests (number of tests)"
        .Axes(xlCategory, xlPrimary).HasTitle = False
        .Axes(xlValue, xlPrimary).HasTitle = True
        .Axes(xlValue, xlPrimary).AxisTitle.Characters.Text = "number of tests"
        .Name = "FloydTestNumbers"
    End With
    Sheets("Caliber_Table").Select
    Charts.Add
    With ActiveChart
    .ChartType = xlColumnStacked
    .SetSourceData Source:=Sheets("Caliber_Table").Range("O2:Y2,O7:Y10"), PlotBy:=xlRows
    .SeriesCollection(1).Name = "=Caliber_Table!R3C9"
    .SeriesCollection(2).Name = "=Caliber_Table!R4C9"
    .SeriesCollection(3).Name = "=Caliber_Table!R5C9"
    .SeriesCollection(4).Name = "=Caliber_Table!R6C9"
    .Location Where:=xlLocationAsNewSheet
        .HasTitle = True
        .ChartTitle.Characters.Text = "Floyd tests (effort in days)"
        .Axes(xlCategory, xlPrimary).HasTitle = False
        .Axes(xlValue, xlPrimary).HasTitle = True
        .Axes(xlValue, xlPrimary).AxisTitle.Characters.Text = "effort in days"
        .Name = "FloydTestEffort"
    End With
    Sheets("Caliber_Table").Select
    Charts.Add
    With ActiveChart
    .ChartType = xlColumnStacked
    .SetSourceData Source:=Sheets("Caliber_Table").Range("Z2:AE2,Z3:AE6"), PlotBy:=xlRows
    .SeriesCollection(1).Name = "=Caliber_Table!R3C9"
    .SeriesCollection(2).Name = "=Caliber_Table!R4C9"
    .SeriesCollection(3).Name = "=Caliber_Table!R5C9"
    .SeriesCollection(4).Name = "=Caliber_Table!R6C9"
    .Location Where:=xlLocationAsNewSheet
        .HasTitle = True
        .ChartTitle.Characters.Text = "PulseEE tests (number of tests)"
        .Axes(xlCategory, xlPrimary).HasTitle = False
        .Axes(xlValue, xlPrimary).HasTitle = True
        .Axes(xlValue, xlPrimary).AxisTitle.Characters.Text = "number of tests"
        .Name = "PulseEETestNumbers"
    End With
    Sheets("Caliber_Table").Select
    Charts.Add
    With ActiveChart
    .ChartType = xlColumnStacked
    .SetSourceData Source:=Sheets("Caliber_Table").Range("Z2:AE2,Z7:AE10"), PlotBy:=xlRows
    .SeriesCollection(1).Name = "=Caliber_Table!R3C9"
    .SeriesCollection(2).Name = "=Caliber_Table!R4C9"
    .SeriesCollection(3).Name = "=Caliber_Table!R5C9"
    .SeriesCollection(4).Name = "=Caliber_Table!R6C9"
    .Location Where:=xlLocationAsNewSheet
        .HasTitle = True
        .ChartTitle.Characters.Text = "PulseEE tests (effort in days)"
        .Axes(xlCategory, xlPrimary).HasTitle = False
        .Axes(xlValue, xlPrimary).HasTitle = True
        .Axes(xlValue, xlPrimary).AxisTitle.Characters.Text = "effort in days"
        .Name = "PulseEETestEffort"
    End With
    Sheets("Caliber_Table").Select
    Charts.Add
    With ActiveChart
    .ChartType = xlColumnStacked
    .SetSourceData Source:=Sheets("Caliber_Table").Range("AF2:AG2,AF3:AG6"), PlotBy:=xlRows
    .SeriesCollection(1).Name = "=Caliber_Table!R3C9"
    .SeriesCollection(2).Name = "=Caliber_Table!R4C9"
    .SeriesCollection(3).Name = "=Caliber_Table!R5C9"
    .SeriesCollection(4).Name = "=Caliber_Table!R6C9"
    .Location Where:=xlLocationAsNewSheet
        .HasTitle = True
        .ChartTitle.Characters.Text = "Testers (number of tests)"
        .Axes(xlCategory, xlPrimary).HasTitle = False
        .Axes(xlValue, xlPrimary).HasTitle = True
        .Axes(xlValue, xlPrimary).AxisTitle.Characters.Text = "number of tests"
        .Name = "TestersNumbers"
    End With
    Sheets("Caliber_Table").Select
    Charts.Add
    With ActiveChart
    .ChartType = xlColumnStacked
    .SetSourceData Source:=Sheets("Caliber_Table").Range("AF2:AG2,AF7:AG10"), PlotBy:=xlRows
    .SeriesCollection(1).Name = "=Caliber_Table!R3C9"
    .SeriesCollection(2).Name = "=Caliber_Table!R4C9"
    .SeriesCollection(3).Name = "=Caliber_Table!R5C9"
    .SeriesCollection(4).Name = "=Caliber_Table!R6C9"
    .Location Where:=xlLocationAsNewSheet
        .HasTitle = True
        .ChartTitle.Characters.Text = "Testers (effort in days)"
        .Axes(xlCategory, xlPrimary).HasTitle = False
        .Axes(xlValue, xlPrimary).HasTitle = True
        .Axes(xlValue, xlPrimary).AxisTitle.Characters.Text = "effort in days"
        .Name = "TestersEffort"
    End With
End Sub

