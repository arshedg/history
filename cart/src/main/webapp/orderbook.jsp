<%-- 
    Document   : orderbook
    Created on : 24 Nov, 2015, 4:23:15 PM
    Author     : arsh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="//code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.css">
        <script src="//code.jquery.com/jquery-1.10.2.min.js"></script>
        <script src="//code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.js"></script>
        <title>Order Book</title>
        <script>
            $(document).ready(function () {
                $.ajax({
                    url: 'api/orders/',
                    type: "post",
                    dataType: "json",
                    data: {
                        json: JSON.stringify([
                            {
                                id: 1,
                                firstName: "Peter",
                                lastName: "Jhons"},
                            {
                                id: 2,
                                firstName: "David",
                                lastName: "Bowie"}
                        ]),
                        delay: 3
                    },
                    success: function (data, textStatus, jqXHR) {
                        // since we are using jQuery, you don't need to parse response
                        drawTable(data);
                    }
                });

                function drawTable(data) {
                    for (var i = 0; i < data.length; i++) {
                        drawRow(data[i]);
                    }
                }

                function drawRow(rowData) {
                    var row = $("<tr />")
                    $("#personDataTable").append(row); //this will append tr element to table... keep its reference for a while since we will add cels into it
                    row.append($("<td onclick='detectRow(event)'><a href='#edit'>" + rowData.name + "</a></td>"));
                    row.append($("<td>" + rowData.product + "</td>"));
                    row.append($("<td>" + rowData.quantity + "</td>"));
                    row.append($("<td><a href='tel:" + rowData.number+"'>"+rowData.number + "</a></td>"));
                    row.append($("<td>" + rowData.address + "</td>"));
                    row.append($("<td>" + (rowData.immediate==false?"NO":"YES") + "</td>"));
                    row.append($("<td>" + rowData.time + "</td>"));
                }
               



            });
             var _uname,_uaddress,_unumber;
             function detectRow(e){
                    _uname=e.target.text;
                    _uaddress=e.target.parentElement.nextSibling.nextSibling.nextSibling.nextSibling.textContent;
                    _unumber=e.target.parentElement.nextSibling.nextSibling.nextSibling.textContent;
                    $("#name").val(_uname);
                    $("#address").val(_uaddress);
                     $("#number").val(_unumber);
                }
        </script>

    </head>
    <body>
        <div data-role="page" class="ui-content">
        <table  id="personDataTable"  data-mode="reflow" class="table-stroke">
            <tr>
                <th>name</th>
                <th>product</th>
                <th>quantity</th>
                <th>number</th>
                <th>address</th>
                <th>immediate</th>
                <th>time</th>
            </tr>

        </table>
        </div>
        <div data-role="page" id="edit">
            <div data-role="header" id="mainHeader" >
                
                <img src="images/icon.png" class="ui-btn-left" />
                <h3> Fish Cart</h3>
            </div>
            <div data-role="content">
             <form method="get" action="api/updateuser">
                    <label for='name'>Name:</label>
                    <input type='text' name="name" id='name'/>
                    <label for='address'>Address</label>
                    <input type='text' name="address" id='address'/>
                    <input type="number" style="display:none" name="number" id="number"/>  
                    <input type="submit" value="save">
             </form>
            </div>
        </div>
    </body>
</html>
