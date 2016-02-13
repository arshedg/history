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

        <style>
            /**** Trying to style h1 and paragraph *******/
            .myHeader {
                margin-top: 0!important;
                color: blue;
            }

            .myParagraph {
                color:#333;
                overflow:show;
                text-overflow:clip;
                white-space:normal;
                margin-bottom:0px;
                height: auto;
                margin-bottom: 0;
                color: red;
            }
            #page {
                display: none;
            }
            #loading {
                display: block;
                position: absolute;
                top: 0;
                left: 0;
                z-index: 100;
                width: 100vw;
                height: 100vh;
                background-color: rgba(192, 192, 192, 0.5);
                background-image: url("http://i.stack.imgur.com/MnyxU.gif");
                background-repeat: no-repeat;
                background-position: center;
            }


            .item-link {
                padding-bottom: 5px;
            }

            .row {
                font-size: 16px;
                color: red;
            }

            .actual-price {
                font-size: 16px;
                position: relative;
            }

            .actual-price::after {
                content: " ";
                position: absolute;
                border-top: solid 1px #000;
                left: 0;
                right: 0;
                top: 50%;
            }

            .size {
                font-size: 13px;
                font-weight: 100;
            }
            .center-button{
               
    margin: 0 auto !important;
    width: 90%;
    
            }



        </style>
    </head>
    <body>

        <div data-role="page" class="type-home" id="productPage">
            <div data-role="header" id="mainHeader" >
              
                <img src="images/icon.png" class="ui-btn-left" />
                <h3> Fish Cart</h3>
            </div>
            <div data-role="content">
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
        <div data-role="popup" id="popupInfo">
            <div data-role="header" >
                <h3 id="productName">Mathy sardine</h3> 
            </div>
            <div data-role="content">

                <div data-role="fieldcontain">
                    <label for="productPrice">Price per KG:</label>
                    <input type="text" id="productPrice" id="name" value="100" readonly="true" />
                </div> 



                <label for="quantity-step"> Please enter the quantity in Kg:</label>
                <input type="range" name="quantity" id="quantity-step" value="1" min=".5" max="5" step=".5" />
                <br><br>
                <label id="total">
                    Amount to be paid:<span id="amount" >0</span>
                </label>
                <input id="orderButton" type="submit" value="PLACE ORDER" onclick="placeOrder()">
                <div id="response"></div>
            </div>
            
        </div>

    </div>


</body>
</html>