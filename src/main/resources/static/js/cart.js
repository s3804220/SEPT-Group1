contextPath = "[[@{/}]]";
var shopId = "[[${shopDetail.id}]]";

$(document).ready(function() {
    total = 0.0;

    $(".itemSmallSum").each(function(index, element) {
        total = total + parseFloat(element.innerHTML);
    });
    $("#totalSum").text("$" + total);
});