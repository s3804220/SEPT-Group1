contextPath = "[[@{/}]]";
var shopId = "[[${shopDetail.id}]]";

$(document).ready(function() {
    total = 0.0;

    $(".itemSmallSum").each(function(index, element) {
        total = total + parseFloat(element.innerHTML);
    });
    $("#totalSum").text("$" + total);
});

var qtyValue = 1
var cartValue = $('#cart-amount').val()

$('#amount').on('change blur', function() {
    if(this.value.trim()){
        qtyValue = this.value.trim()
    }else{
        this.value = qtyValue
    }
});
$('#cart-amount').on('change blur', function() {
    if(this.value.trim()){
        cartValue = this.value.trim()
    }else{
        this.value = cartValue
    }
});