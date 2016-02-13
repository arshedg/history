/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


$("body").ready(onBodyLoaded);
var showAll=true;
function onBodyLoaded() {
//    var order= new Object();
//    order.name="arsh";
//    order.number=123;
//    order.orders = [];
//    order.orders[0]="fish";
//    order.orders[1]="meat";
//    var manyOrders =[] ;
//    manyOrders[0]=order;
  //  loadCrm(manyOrders)
    initCRM();
}
function initCRM(){
    $.mobile.loading('show');
    var url="api/ordersbycustomer/";
    $.ajax({
        beforeSend: function () {
                $.mobile.loading('show');
        }, //Show spinner
        complete: function () {
            $.mobile.loading('hide');

        },
        url: url,
        type: "post",
        dataType: 'json',
        error: function () {
           alert("looks like internet issue. Try refreshing")
        }
        ,
        success: function (response) {
            cachedResponse = response;
            loadCrm();
        },
    });
}
var cachedResponse;
function filter(){
    if(showAll){
        initCRM();
    }else{
        var children = $("#cardHolder").children();
        for(var i=0;i<children.length;i++){
            var feed = $(children[i]).find("#feedback").val();
            if(feed!=null&&feed.length>2){
               $("#cardHolder")[0].removeChild(children[i]); 
            }
        }
        $("#cardHolder").listview("refresh");
    }
}
function clearAll(){
    var children = $("#cardHolder").children();
        for(var i=1;i<children.length;i++){

               $("#cardHolder")[0].removeChild(children[i]); 
            
        }
}
function loadCrm(){
    clearAll();
    var response = cachedResponse;
    var cardHolder = $("#cardHolder");
    for(var i=0;i<response.length;i++){
        var node = createTile(response[i]);
        if(node!=null){
            cardHolder.append(node);
        }
    }
    cardHolder.listview("refresh");
    hack();
}
function createTile(orderDetail){
    if(showAll){
        var card = $("#template").clone(true);
        card.attr("style","");
        card.id="changed";
        card.find("#cname").val(orderDetail.name);
        card.find("#cnumber").text(orderDetail.number);
        card.find("#cnumber").attr("href","tel:"+orderDetail.number);
        card.find("#items").val(orderDetail.product);
        card.find("#time").val(orderDetail.time);
        card.find("#feedback").val(orderDetail.feedback);
        
        card.find("#orderButton").attr("onclick","save(event)");
        return card;
    }
    return null;
}
function save(ev){
   var gFather = $(ev.target.parentNode.parentNode);
   var number = gFather.find("#cnumber").text();
   var feedback = gFather.find("#feedback").val();
   var url="api/updatefeedback?number="+number+"&feedback="+feedback;
    $.ajax({
        beforeSend: function () {
                $.mobile.loading('show');
        }, //Show spinner
        complete: function () {
            $.mobile.loading('hide');

        },
        url: url,
        type: "post",
        error: function () {
           alert("looks like internet issue. Try refreshing")
        }
        ,
        success: function (response) {
            alert("registered");
        },
    });
}
function hack(){
    try{
    var holder=document.getElementById("cardHolder");
    var fc= holder.firstElementChild;
    holder.removeChild(fc);
    }catch(error){
        
    }
}