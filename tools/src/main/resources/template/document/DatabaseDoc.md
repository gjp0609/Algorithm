word 宏 使所有表格自动调整
```
Sub SelectAllTables()

    Dim tempTable As Table
    
    Application.ScreenUpdating = False
    
    If ActiveDocument.ProtectionType = wdAllowOnlyFormFields Then
        MsgBox "Error"
        Exit Sub
    End If

    ActiveDocument.DeleteAllEditableRanges wdEditorEveryone

    For Each tempTable In ActiveDocument.Tables
        tempTable.AutoFitBehavior (wdAutoFitWindow)
    Next

    ActiveDocument.SelectAllEditableRanges wdEditorEveryone

    ActiveDocument.DeleteAllEditableRanges wdEditorEveryone
    
    Application.ScreenUpdating = True

End Sub
```