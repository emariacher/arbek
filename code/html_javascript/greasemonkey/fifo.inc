<%
   ' FIFO buffer   

   'Version History

   ' v1.0.0 - 26/12/2000 - Initial prototyping
   ' v1.0.1 - 27/12/2000 - Development to support element/fragment configuration
   '                     - First vbscript/database integration testing
   ' v1.1.0 - 28/12/2000 - STABLE v1.1
     
   ' Global variables
   Public var_fifo_buffer
   Public var_fifo_sepchar
   Public var_fifo_errorstatus

   ' Call to initialise system
   Sub fifo_initialise()
    var_fifo_buffer=""
    var_fifo_sepchar="~"
   End Sub

   ' Call to finalise system
   Sub fifo_finalise()
   End Sub

   ' Pass a new element string to add to the buffer
   Function fifo_push(newelement)
    dim output, verified
    output=false
    verified=fifo_verify_elementstring(newelement)
    if verified=true then 
      var_fifo_buffer=var_fifo_buffer+newelement+var_fifo_sepchar
      output=true
    else
     fifo_seterror("illegal character in element string")
    end if
    fifo_push=output
   End Function

   ' Call to remove an element string from the buffer
   Function fifo_pop()
    Dim output
    output=true
    if instr(var_fifo_buffer,var_fifo_sepchar)<1 then
     fifo_seterror("buffer is empty")
     output="error"
    else
     output=left(var_fifo_buffer,instr(var_fifo_buffer,var_fifo_sepchar))
     output=left(output,(len(output)-1))
     var_fifo_buffer=right(var_fifo_buffer,(len(var_fifo_buffer)-instr(var_fifo_buffer,var_fifo_sepchar)))
    end if
    fifo_pop=output
   End Function

   ' Read full value of the buffer
   Function fifo_readbuffer()
    fifo_readbuffer=var_fifo_buffer
   End Function

   ' Write full value of the buffer
   Sub fifo_writebuffer(newbuffer)
    var_fifo_buffer=newbuffer
   End Sub

   ' Reset the current buffer
   Sub fifo_resetbuffer()
    var_fifo_buffer=""
   End Sub

   ' Count number of elements in buffer
   Function fifo_countelements()
    Dim output
    output=0
    for ct=1 to len(var_fifo_buffer)
     if mid(var_fifo_buffer,ct,1)=var_fifo_sepchar then
      output=output+1
     end if
    next
    fifo_countelements=output
   End Function

   ' Internal function which checks for illegal characters and/or syntax in an element string
   Function fifo_verify_elementstring(element)
    dim output
    output=true
    if mid(element,var_fifo_sepchar)>0 then output=false
    fifo_verify_elementstring=output
   End Function

   ' Set the error status flag
   Sub fifo_seterror(status)
    var_fifo_errorstatus=status
   End Sub

   ' Get the error status flag
   Function fifo_geterror()
    fifo_geterror=var_fifo_errorstatus
   End Function

 %>
