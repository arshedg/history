<%-- 
    Document   : deliverymanager
    Created on : 4 Jan, 2016, 3:47:13 PM
    Author     : arsh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Delivery Manager</title>
        <link rel="stylesheet" href="//code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.css">
        <script src="//code.jquery.com/jquery-1.10.2.min.js"></script>
        <script src="//code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.js"></script>
        <script src="script/login.js"></script>
        <script src="script/cart.js?<%=System.currentTimeMillis()%>"></script>  
        <style>
            

            #red {
              border-left: 5px solid #f00;
              border-bottom: 5px solid black;
            }

            #green {
              border-left: 5px solid #0f0;
              border-bottom: 5px solid black;

            }
        </style>
    </head>
    <body>
        <div data-role="page" id="entryPage">
            <div data-role="header" id="mainHeader" >

                <img src="images/icon.png" class="ui-btn-left" />
                <h3>Delivery Manager</h3>
            </div>
            <div data-role="content">
                <ul data-role="listview" id="productList" data-inset="true" data-theme="c" data-dividertheme="a">
                    <li id='red'>
                        <h1>Customer:<span id="cname">Arshed</span></h1>
                        <div data-role="fieldcontain">
                        <label style="display: inline; vertical-align: 1.2em;" for="flip-1">Done confirmation call?</label>
                        <select name="flip-1" id="flip-1" data-role="slider">
                            <option value="off">no</option>
                            <option value="on">yes</option>
                        </select> 
                        </div>
                        <label for="delivery">Delivery Person :</label>
                        <select id="delivery">
                            <option>Not decieded</option>
                            <option>Vaishakh</option>
                            <option>Siraj</option>
                            <option>Achy</option>
                        </select>
                        <div>Orders</div>
                        <ul>
                            <l1>
                                <div>Prawns :<span id="cname">1</span>KG</div>
                            </l1>
                            <l1>
                                <div>Broiler Chicken :<span id="cname">2</span>KG</div>
                            </l1>
                        </ul>
                        <label for="cAddress">Address :</label>

                        <textarea id="cAddress"cols="40" rows="8"  class="ui-input-text ui-body-c ui-corner-all ui-shadow-inset"></textarea>
                        <h1>Total Price :120Rs</h1>
                        <button onclick="confirm('Do you really delivered?')" href="#" data-role="button">Order delivered</button>

                    </li>
                       <li id='green'>
                        <h1>Customer:<span id="cname">Arshed</span></h1>
                        <label for="flip-1">Confirmation call:</label>
                        <select name="flip-1" id="flip-1" data-role="slider">
                            <option value="off">no</option>
                            <option value="on">yes</option>
                        </select> 
                        <label for="delivery">Delivery Person :</label>
                        <select id="delivery">
                            <option>Not decieded</option>
                            <option>Vaishakh</option>
                            <option>Siraj</option>
                            <option>Achy</option>
                        </select>
                        <div>Orders</div>
                        <ul>
                            <l1>
                                <div>Prawns :<span id="cname">1</span>KG</div>
                            </l1>
                            <l1>
                                <div>Broiler Chicken :<span id="cname">2</span>KG</div>
                            </l1>
                        </ul>
                        <label for="cAddress">Address :</label>

                        <textarea id="cAddress"cols="40" rows="8"  class="ui-input-text ui-body-c ui-corner-all ui-shadow-inset"></textarea>
                        <h1>Total Price :120Rs</h1>
                        <button onclick="confirm('Do you really delivered?')" href="#" data-role="button">Order delivered</button>

                    </li>
                </ul>
            </div>
        </div>
        <div id="template">
               <h1>Customer:<span id="cname">Arshed</span></h1>
                        <div data-role="fieldcontain">
                        <label style="display: inline; vertical-align: 1.2em;" for="confirmation">Done confirmation call?</label>
                        <select name="confirmation" id="confirmation" data-role="slider">
                            <option value="off">no</option>
                            <option value="on">yes</option>
                        </select> 
                        </div>
                        <label for="delivery">Delivery Person :</label>
                        <select id="delivery">
                            <option>Not decieded</option>
                            <option>Vaishakh</option>
                            <option>Siraj</option>
                            <option>Achy</option>
                        </select>
                        <div>Orders</div>
                        <ul>
                            <l1>
                                <div><span id="pName">Prawns</span> :<span id="pQuantity">1</span>KG</div>
                            </l1>
                        </ul>
                        <label for="cAddress">Address :</label>

                        <textarea id="cAddress"cols="40" rows="8"  class="ui-input-text ui-body-c ui-corner-all ui-shadow-inset"></textarea>
                        <h1>Total Price :<span id="total">120</span>Rs</h1>
                        <button onclick="confirm('Do you really delivered?')" href="#" data-role="button">Order delivered</button>

        </div>>
    </body>
</html>
