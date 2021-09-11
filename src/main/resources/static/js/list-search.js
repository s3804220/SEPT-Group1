var shopItems = document.getElementsByClassName('item-list-row')
var itemListHead = document.getElementById('itemlist-tablehead')

$("#list-search-input").on("input", function() {
    let searchKeyword = $(this).val()
    if(searchKeyword) {
        for (let i = 0; i < shopItems.length; i++) {
            if (shopItems[i].querySelector(".itemlist-itemname").innerText.toLowerCase().includes(searchKeyword.toLowerCase())) {
                shopItems[i].style.display = "block"
            } else {
                shopItems[i].style.display = "none"
            }
        }
        itemListHead.style.display = "none"
    }else {
        for (let i = 0; i < shopItems.length; i++) {
            shopItems[i].style.display = "block"
        }
    }
});