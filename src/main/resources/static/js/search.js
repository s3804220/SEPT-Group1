var shopItems = document.getElementsByClassName('product-item-card')

$("#search-input").on("input", function() {
    let searchKeyword = $(this).val()
    if(searchKeyword) {
        for (let i = 0; i < shopItems.length; i++) {
            if (shopItems[i].querySelector("#product-item-name").innerText.toLowerCase().includes(searchKeyword.toLowerCase())) {
                shopItems[i].style.display = "block"
            } else {
                shopItems[i].style.display = "none"
            }
        }
    }else {
        for (let i = 0; i < shopItems.length; i++) {
            shopItems[i].style.display = "block"
        }
    }
});