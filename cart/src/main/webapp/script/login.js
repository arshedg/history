function registerUser() {
    var user = document.getElementById("user").value;
    var number = document.getElementById("number").value;
    var xhttp;
    xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (xhttp.readyState == 4 && xhttp.status == 200) {
            registerResponse(xhttp);
        }
    };
    var appender = new Date().getTime();
    //Being extra safe in over-riding caching
    var url="api/register?name="+user+"&no="+number+"&appender="+appender;
    document.getElementById("register").value="PLEASE WAIT";
     document.getElementById("register").disabled=true;
    xhttp.open("GET", url, true);
    xhttp.send();
}
function registerResponse(response){
    if(response.responseText=="SUCCESS"){  
        saveNumber();
        redirect('listProducts.jsp');;
    }else
    {
        document.getElementById("register").value="REGISTER";
        document.getElementById("register").disabled=false;
        showMessage(response.responseText);
    }
}
function redirect(uri) {
  if(navigator.userAgent.match(/Android/i)) 
    document.location=uri;      
  else
    window.location.replace(uri);
}
function saveNumber(){
    try{
        Android.saveNumber(document.getElementById("number").value);
    }catch(error){
        NUMBER = document.getElementById("number").value;
        //may be not an android device
    }
}

function showMessage(toast) {
    try{
        Android.showToast(toast);
    }catch(error){
        alert(toast)
    }
    
}
function getNumber() {
    try {
        return Android.getNumber();
    } catch (error) {
        return "";
    }
}