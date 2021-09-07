$(document).ready(function() {
    total = 0.0;

    $(".itemSmallSum").each(function(index, element) {
        total = total + parseFloat(element.innerHTML.substring(1));
    });
    $("#totalSum").text("$" + total);
});

document.getElementById("order-button").addEventListener("click", createOrder)

function createOrder(){
    alert("Thank you for your purchase!\n\nAn email has been sent to your mailbox with your order details!")
}