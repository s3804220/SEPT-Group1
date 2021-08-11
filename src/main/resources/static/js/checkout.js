$(document).ready(function() {
    total = 0.0;

    $(".itemSmallSum").each(function(index, element) {
        total = total + parseFloat(element.innerHTML.substring(1));
    });
    $("#totalSum").text("$" + total);
});

document.getElementById("order-button").addEventListener("click", createOrder)

function createOrder(){
    alert("Your order has been created!\nThank you for your purchase!")
}