  // FIFO buffer   

  //Version History

  // v1.0.0 - 26/12/2000 - Initial prototyping
  // v1.0.1 - 27/12/2000 - Development to support element/fragment configuration
  //                     - First vbscript/database integration testing
  // v1.1.0 - 28/12/2000 - STABLE v1.1

  // Global variables
  var var_fifo_buffer
  var var_fifo_temp
  var var_fifo_sepchar
  var var_fifo_errorstatus

  // Call to initialise system
  function fifo_initialise(){
   var_fifo_buffer=""
   var_fifo_temp=""
   var_fifo_sepchar="~"
  }

  // Call to finalise system
  function fifo_finalise(){
  }

  // Push a new element into the buffer
  function fifo_push(newelement){
   output=true
   var_fifo_temp=newelement
   if (var_fifo_temp.length==0){
    fifo_seterror("there is nothing to push!")
    output=false
    } else {
     if (var_fifo_temp.indexOf(var_fifo_sepchar)>0){
      fifo_geterror("illegal character");
      } else {
      var_fifo_buffer=var_fifo_buffer+var_fifo_temp+var_fifo_sepchar
     }
    }
   return(output)
  }

  // Pop an element from the buffer
  function fifo_pop(){
   newlen=0
   var_fotjeff_temp=""
   if (var_fifo_buffer.length==0){
    fifo_geterror("there is nothing to pop!")
    } else {
     if (var_fifo_buffer.indexOf(var_fifo_sepchar)==-1){
      fifo_geterror("the "+var_fifo_sepchar+" character is illegal in the buffer")
      } else {
      var_fifo_temp=var_fifo_buffer.substr(0,(var_fifo_buffer.indexOf(var_fifo_sepchar)))
      newlen=var_fifo_buffer.length-(var_fifo_buffer.indexOf(var_fifo_sepchar))
      var_fifo_buffer=var_fifo_buffer.substr((var_fifo_buffer.indexOf(var_fifo_sepchar)+1),newlen)
     }
    }
    return(var_fifo_temp)
  }

  // Read the current buffer
  function fifo_readbuffer(){
   return(var_fifo_buffer)
  }

  // Set the current buffer
  function fifo_writebuffer(newvalue){
   var_fifo_buffer=newvalue
  }

  // Reset the current buffer
  function fifo_reset(){
   var_fifo_buffer=""
  }

  // Count the number of elements in the buffer
  function fifo_countelements(){
   count=0
   for (i=0; i<var_fifo_buffer.length; i++){
    if (var_fifo_buffer.substr(i,1)==var_fifo_sepchar){
    count=count+1
    }
   }
   return(count)
  }

  // Set the error status flag
  function fifo_seterror(message){
   var_fifo_errorstatus=message
  }

  // Get the error status flag
  function fifo_geterror(message){
   return(var_fifo_errorstatus)
  } 

