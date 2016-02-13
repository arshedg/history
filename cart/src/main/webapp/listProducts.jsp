<%-- 
    Document   : listProducts
    Created on : 20 Nov, 2015, 7:08:37 PM
    Author     : arsh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
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
        <script src="script/login.js"></script>
        <script src="script/cart.js?<%=System.currentTimeMillis()%>"></script>
        <style>

        </style>
    </head>
    <body>
        <script>
            $("body").ready(onBodyLoaded);
        </script>
        <div data-role="page" id="entryPage">
            <div data-role="header" id="mainHeader" >

                <img src="images/icon.png" class="ui-btn-left" />
                <h3> Fish Cart</h3>
            </div>
            <div data-role="content" >

                <a  onclick="isFish = true;
                        $('#booking').show(true);
                        loadProducts(productCache);" data-transition='flip' href="#productPage" data-role="button" data-inline="true" class="center-button ">
                    <img src="images/fish.png" class="ui-btn-bottom" />
                </a>
                <br/><br/><br/>
                <a onclick="isFish = false;
                        $('#booking').hide(true);
                        loadProducts(productCache);" data-transition='flip'  href="#productPage" data-role="button" data-inline="true" class="center-button">
                    <img src="images/meat.png" class="ui-btn-bottom" />
                </a>

            </div>
        </div>
        <div data-role="page" id="popupInfo">
            <div data-role="header" >
                <h3 id="productName">Mathy sardine</h3> 
            </div>

            <div data-role="content">
                <div id='userinfo'>
                    <label for='phone'>Contact number:</label>
                    <input type='number' id='phone'/>
                </div>
                <div data-role="fieldcontain">
                    <label for="productPrice">Price per KG:</label>
                    <input type="text" id="productPrice" id="name" value="100" readonly="true" />
                </div> 



                <label for="quantity-step"> Please enter the quantity in Kg:</label>
                <input type="range" name="quantity" id="quantity-step" value="1" min=".5" max="5" step=".5"  data-highlight="true" />

                <div id="booking">
                    <fieldset data-role="controlgroup" data-type="horizontal" data-mini="true">
                        <legend>Delivery Time:</legend>

                        <input   type="radio" name="timing" id="now" value="on" checked="checked">
                        <label id="lNow" for="now">Right now</label>
                        <input  type="radio" name="timing" id="later" value="off">
                        <label id="lLater"for="later">Tomorrow morning</label>

                    </fieldset>
                    <div id="tip" class="alert-box notice"><span>Tip:</span>Book fish for tomorrow and get 5% discount.</div>

                    <div id="dBox" class="alert-box discount"><span><b>You will have 5% discount .</b></span><input  id="dPrice" readonly="true" type="text"/></div>

                </div>
                <div data-role="fieldcontain">
                    <label  data-theme="d"  for="amountText"><b>Amount:</b></label>
                    <input data-theme="d"  type="text" name="clear" id="amountText" value="0" readonly="true">
                </div>

                <input id="orderButton" type="submit" value="PLACE ORDER" onclick="placeOrder()">
                <div id="response"></div>
            </div>

        </div>
        <div data-role="page" class="type-home" id="productPage">
            <div data-role="header" id="mainHeader" >

                <img src="images/icon.png" class="ui-btn-left" />
                <h3> Fish Cart</h3>
            </div>
            <div data-role="content" style="padding-top:0px">
                <fieldset id="filterPanel" data-role="controlgroup" data-type="horizontal" data-mini="true">


                    <input   type="radio" name="filter" id="all" value="on" checked="checked">
                    <label id="lNow" for="all">All</label>
                    <input  type="radio" name="filter" id="regular" value="off">
                    <label id="lLater"for="regular">Regular</label>
                    <input  type="radio" name="filter" id="premium" value="off">
                    <label id="lLater"for="premium">Premium</label>

                </fieldset>
                <ul data-role="listview" id="productList" data-inset="true" data-theme="c" data-dividertheme="a">
                </ul>
                <div data-role="footer" data-theme="c">
                    <ul>
                        <li>

                            For any queries please call 9605657736.
                        </li>
                        <li>
                            100% quality assurance. You can return the product if not happy with the quality.
                        </li>
                    </ul>
                </div>
            </div>

        </div>


    </div>


</body>
</html>