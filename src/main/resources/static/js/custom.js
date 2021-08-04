function checkInput(){
    let itemname = document.getElementById('item-name').value
    let nameMessage = document.getElementById('name-check')
    let description = document.getElementById('item-description').value
    let descriptionMessage = document.getElementById('description-check')
    let price = document.getElementById('item-price').value
    let priceMessage = document.getElementById('price-check')
    let fileinput = document.getElementById('item-images')
    let fileMessage = document.getElementById('files-check')
    let validated = true

    if(!itemname){
        nameMessage.style.display = "block"
        nameMessage.style.color = "red"
        nameMessage.innerHTML = 'Please enter an item name!'
        validated = false
    }else {
        nameMessage.style.display = "none"
    }
    if(!description){
        descriptionMessage.style.display = "block"
        descriptionMessage.style.color = "red"
        descriptionMessage.innerHTML = 'Please enter a description for the item!'
        validated = false
    }else {
        descriptionMessage.style.display = "none"
    }
    if(!price || isNaN(price)){
        priceMessage.style.display = "block"
        priceMessage.style.color = "red"
        priceMessage.innerHTML = 'Please enter a valid price!'
        validated = false
    }else if (price<0){
        priceMessage.style.display = "block"
        priceMessage.style.color = "red"
        priceMessage.innerHTML = 'Please do not enter negative prices!'
        validated = false
    }else {
        priceMessage.style.display = "none"
    }
    if(fileinput.files.length===0){
        fileMessage.style.display = "block"
        fileMessage.style.color = "red"
        fileMessage.innerHTML = 'Please select at least 1 file!'
        validated = false
    }else {
        fileMessage.style.display = "none"
    }
    if(validated){
        saveItem()
    }
}

function saveItem() {

    //get values from the html form
    let id = document.getElementById('item-input-id').value
    let itemname = document.getElementById('item-name').value
    let description = document.getElementById('item-description').value
    let price = document.getElementById('item-price').value
    let fileinput = document.getElementById('item-images')
    let filenames = ''

    for (let i = 0; i < fileinput.files.length; ++i) {
        filenames += fileinput.files.item(i).name
        if(i!==fileinput.files.length-1){
            filenames+='|'
        }
    }

    if(!id){
        fetch('http://localhost:8080/items', {
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            method: "POST",
            body: JSON.stringify({itemName: itemname,itemDescription: description, itemPrice: price, itemImage: filenames})
        }).then(res => res.json())
            .then(itemid => uploadImg(itemid))
            .then(() => document.getElementById('item-form-div').innerHTML=`<span style="color: #1c7430">Item saved successfully!<br>You can view a list of all items in the database <a href="item-list.html" class="item-link">here</a>.</span>`)
    }else{
        fetch('http://localhost:8080/items', {
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            method: "PUT",
            body: JSON.stringify({id:id, itemName: itemname,itemDescription: description, itemPrice: price, itemImage: filenames})
        }).then(res => res.json())
            .then(itemid => uploadImg(itemid))
            .then(() => document.getElementById('item-form-div').innerHTML=`<span style="color: #1c7430">Item saved successfully!<br>You can view a list of all items in the database <a href="item-list.html" class="item-link">here</a>.</span>`)
    }

}

function uploadImg(id){
    let imgData = new FormData()
    let fileinput = document.getElementById('item-images')

    if(id !== undefined){
        for (let i = 0; i < fileinput.files.length; ++i) {
            imgData.append("img"+i+"|"+id,fileinput.files.item(i))
        }
    }

    fetch('http://localhost:8080/upload', {
        method: "POST",
        body: imgData
    })
}

function getItemList(){
    let itemList = document.getElementById('item-list-rows')
    itemList.innerHTML = ''

    fetch('http://localhost:8080/items')
        .then(res => res.json())
        .then(json => {
            for (let i = 0; i < json.length; i++) {

                let images = json[i].itemImage.split("|")

                itemList.innerHTML += `<tr>
                            <td class="product__cart__item">
                                <div class="product__cart__item__pic">
                                    <img src="img/upload/item${json[i].id}/${images[0]}" alt="" style="width: 100px;height: 100px">
                                </div>
                                <div class="product__cart__item__text">
                                    <h6>${json[i].itemName}</h6>
                                </div>
                                </td>
                            <td class="cart__price">$ ${json[i].itemPrice}</td>
                            <td class="cart__stock">Available</td>
                            <td class="cart__btn"><button class="primary-btn" onclick="redirectEdit(${json[i].id})">Edit</button></td>
                            <td class="cart__close"><i class="bi bi-x-circle-fill" style="color: #e60000; font-size: 2em; margin-left: -50px; cursor: pointer;" onclick="deleteItem(${json[i].id})"></i></td></tr>`
            }
        })
}

function redirectEdit(id){
    if (id) {
        window.location = '/item-form.html?id=' + id
    }
}

function loadInfo(){
    let params = new URLSearchParams(document.location.search.substring(1))
    let itemid = params.get("id")
    if(itemid){
        let id = document.getElementById('item-input-id')
        let name = document.getElementById('item-name')
        let description = document.getElementById('item-description')
        let price = document.getElementById('item-price')


        fetch('http://localhost:8080/items/'+itemid)
            .then(res => res.json())
            .then(json => {
                if(json){
                    id.value = json.id
                    name.value =  json.itemName
                    description.value = json.itemDescription
                    price.value = json.itemPrice
                }else{
                    document.getElementById('item-form-div').innerHTML=`<span style="color: #cc1825">An item with that ID doesn't exist!<br>Please recheck the <a href="item-list.html" class="item-link">item list</a> or your database to find the item you want to edit.
                    <br>Or you can proceed to add a new item <a href="item-form.html" class="item-link">here</a>.</span>`
                }
            })
    }
}

function deleteItem(id){
    let confirmation = confirm("Are you sure you want to delete this item?")
    if(confirmation){
        fetch('http://localhost:8080/items/'+id, {
            method: "DELETE"
        }).then(() => fetch('http://localhost:8080/deletefiles/'+id, {
            method: "DELETE"
        })).then(() => getItemList())
    }
}