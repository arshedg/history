/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function loadOrder(order){
    $("#cname").val(order.name);
    $("#confirmation").val(order.confirmed.toString());
    $("#delivery").val(order.deliverer);
    $("#pName").textContent = order.product;
    $("")
}