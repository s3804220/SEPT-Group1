function checkInput(){
    //Get the inputs entered by the admin from the HTML form elements
    let itemname = document.getElementById('item-name').value
    let nameMessage = document.getElementById('name-check')
    let description = document.getElementById('item-description').value
    let descriptionMessage = document.getElementById('description-check')
    let price = document.getElementById('item-price').value
    let priceMessage = document.getElementById('price-check')
    let fileinput = document.getElementById('item-images')
    let fileMessage = document.getElementById('files-check')
    let validated = true

    //Check all inputs and display the appropriate error message
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
    //If there is no problem with any inputs, call function to save the item into the database
    if(validated){
        saveItem()
    }
}

function saveItem() {

    //Get the item information entered by the admin from the HTML form elements
    let id = document.getElementById('item-input-id').value
    let itemname = document.getElementById('item-name').value
    let description = document.getElementById('item-description').value
    let price = document.getElementById('item-price').value
    let fileinput = document.getElementById('item-images')
    let filenames = ''

    //Join all image file names into a string separated by |
    //that string will be stored in the items database instead of the images themselves
    for (let i = 0; i < fileinput.files.length; ++i) {
        filenames += fileinput.files.item(i).name
        if(i!==fileinput.files.length-1){
            filenames+='|'
        }
    }

    //If there is no specified ID, add a new item with a POST request
    if(!id){
        fetch('http://localhost:8080/items', {
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            method: "POST",
            body: JSON.stringify({itemName: itemname,itemDescription: description, itemPrice: price, itemImage: filenames})
        }) //Get the newly added item's ID from the response
            .then(res => res.json())
            // Call function to upload all image files to a directory corresponding with that item's ID on the server
            .then(itemid => uploadImg(itemid))
            .then(() => fetch('http://localhost:8080/shopitem', {
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                method: "POST",
                body: JSON.stringify({name: itemname, price: price, description: description, image: 'img/shop/'+fileinput.files.item(0).name})
            }))
            .then(() => uploadShopImg())
            //Display the appropriate message to the admin in HTML
            .then(() => document.getElementById('item-form-div').innerHTML=`<span style="color: #1c7430">Item saved successfully!<br>You can view a list of all items in the database <a href="item-list.html" class="item-link">here</a>.</span>`)
    }else{
        //If there is a specified ID, update the item with that ID in the database with a PUT request
        //the rest of the process is similar to above
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
    //Create new FormData object
    let imgData = new FormData()
    let fileinput = document.getElementById('item-images')

    if(id !== undefined){
        //Append all image data with the specified item ID into the FormData
        for (let i = 0; i < fileinput.files.length; ++i) {
            imgData.append("img"+i+"|"+id,fileinput.files.item(i))
        }
    }

    //Upload all images to the server with a POST request
    fetch('http://localhost:8080/upload', {
        method: "POST",
        body: imgData
    })
}

function uploadShopImg(){
    //Create new FormData object
    let imgData = new FormData()
    let fileinput = document.getElementById('item-images')

    //Append image data into the FormData
    imgData.append("imgshop",fileinput.files.item(0))

    //Upload image to the Shop folder on server with a POST request
    fetch('http://localhost:8080/uploadshop', {
        method: "POST",
        body: imgData
    })
}

function getItemList(){
    let itemList = document.getElementById('item-list-rows')
    itemList.innerHTML = ''

    //Get a list of all items in the database
    fetch('http://localhost:8080/items')
        .then(res => res.json())
        .then(json => {
            for (let i = 0; i < json.length; i++) {

                //Get an array of image names by splitting the string of names in the database
                let images = json[i].itemImage.split("|")

                //For each item in the list, add a new HTML table row with the corresponding information
                //the last two elements are buttons to edit and delete that item
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
    //Redirect the admin to the form to edit the item with the specified ID
    if (id) {
        window.location = '/item-form.html?id=' + id
    }
}

function loadInfo(){
    let params = new URLSearchParams(document.location.search.substring(1))
    let itemid = params.get("id")

    //Check if there is an ID parameter in the URL
    if(itemid){
        let id = document.getElementById('item-input-id')
        let name = document.getElementById('item-name')
        let description = document.getElementById('item-description')
        let price = document.getElementById('item-price')

        //Get information for the item with the specified ID in the database
        fetch('http://localhost:8080/items/'+itemid)
            .then(res => res.json())
            .then(json => {
                //If the item exists and there is information to display, display it in the form for admin to edit
                if(json){
                    id.value = json.id
                    name.value =  json.itemName
                    description.value = json.itemDescription
                    price.value = json.itemPrice
                }else{
                    //If the item with that ID cannot be found, display a message to the admin
                    document.getElementById('item-form-div').innerHTML=`<span style="color: #cc1825">An item with that ID doesn't exist!<br>Please recheck the <a href="item-list.html" class="item-link">item list</a> or your database to find the item you want to edit.
                    <br>Or you can proceed to add a new item <a href="item-form.html" class="item-link">here</a>.</span>`
                }
            })
    }
}

function deleteItem(id){
    //Display a pop-up confirmation box to the admin
    let confirmation = confirm("Are you sure you want to delete this item?")

    //If okay is selected, send a DELETE request to delete the item with the specified ID from the database
    if(confirmation){
        fetch('http://localhost:8080/items/'+id, {
            method: "DELETE"
        }) //Also delete all image files of the specified item from the server
            .then(() => fetch('http://localhost:8080/deletefiles/'+id, {
            method: "DELETE"
        })) //Then reload the list on the page to show the updated list
            .then(() => getItemList())
    }
}