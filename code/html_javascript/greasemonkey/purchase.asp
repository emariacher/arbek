<html>
 <head>
  <title>FIFO Buffer Library Test - Shopping Cart</title>
 </head>
 <body>

 <!-- #include file="fifo.inc" -->

 <%
   dim i, total, product, price
   total=0
   fifo_initialise()
   fifo_writebuffer(request.form("thebuffer"))
 %>

   <center>

   <table border="1">
    <tr><td colspan="2" bgcolor="#cccccc"><p align="center">My Shopping Cart</p></td></tr>
    <tr><td bgcolor="#cccccc">Product Name</td><td bgcolor="#cccccc">Price</td></tr>

 <%
   for i=1 to fifo_countelements()/2
    product=fifo_pop()
    price=fifo_pop()
    Response.Write "<tr><td>"+product+"</td><td>£"+price+"</td></tr>"
    total=total+cdbl(price)
   next
 %>

  </table>
  <br>

 <%
   Response.Write "<b>Total: £"+cstr(total)+"</b>"
   fifo_finalise()
 %>

    </center>

 </body>
</html>