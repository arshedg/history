<%-- 
    Document   : crm
    Created on : 21 Jan, 2016, 8:09:37 PM
    Author     : arsh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Available products</title>
        <link rel="stylesheet" href="//code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.css">
        <link rel="stylesheet" href="css/product.css">

        <script src="//code.jquery.com/jquery-1.10.2.min.js"></script>
        <script src="//code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.js"></script>
        <script src="script/crm.js"></script>
        <title>Customer Service</title>
    </head>
    <body>
        <div data-role="page" id="entryPage">
            <div data-role="header" id="mainHeader" >
                <fieldset data-role="controlgroup" data-type="horizontal" data-mini="true">

                        <input   type="radio" name="timing" id="now" value="on"  onclick="showAll=false;filter();">
                        <label id="lNow" for="now">Hide Completed</label>
                        <input  type="radio" name="timing" id="later" value="off" checked="checked" onclick="showAll=true;initCRM()();">
                        <label id="lLater"for="later">Show All</label>

                    </fieldset>
            </div>
            <div data-role="content" id="mainHeader" >
                <ul data-role="listview" data-inset="true" id="cardHolder">
                    

                </ul>
            </div>
            <li id='template' style="display:none">

                        <label data-theme="d" for="cname"><b>Name:</b></label>
                        <input data-theme="d" id="cname" type="text" name="clear" id="amountText" value="0" readonly="true"/>

                        <div data-role="fieldcontain">
                            <label for="cnumber"><b>Number:</b></label>
                            <a id="cnumber" href='tel:'></a>
                        </div>
                        <div data-role="fieldcontain">
                            <label for="items"><b>Items:</b></label>
                            <input id="items" type="text" name="clear" id="amountText" value="0" readonly="true"/>
                        </div>
                        <div data-role="fieldcontain">
                            <label for="time"><b>time:</b></label>
                            <input id="time" type="text" name="clear" id="amountText" value="0" readonly="true"/>
                        </div>
                        <label for="feedback"><b>FeedBack</b></label>
                        <textarea id="feedback" type="text" name="clear" id="amountText" value="0" >
                        </textarea>
                        <br>
                        <input id="orderButton" type="button" value="Save" onclick="save()"/>

                    </li>

        </div>    

    </body>
</html>
