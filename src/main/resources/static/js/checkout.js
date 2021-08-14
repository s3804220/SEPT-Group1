$(document).ready(function() {
    total = 0.0;

    $(".itemSmallSum").each(function(index, element) {
        total = total + parseFloat(element.innerHTML.substring(1));
    });
    $("#totalSum").text("$" + total);
});

document.getElementById("order-button").addEventListener("click", createOrder)

function createOrder(){
    alert("Thank you for your purchase!\nYour order has been created!")
    /*document.getElementById('checkout-form-div').innerHTML=`<div class="checkout__order"><span style="color: #1c7430">Thank you for your purchase!<br>Your order has been successfully created!<br>Please wait while an Admin confirms your order. You can review your order here.</span></div>`*/
}