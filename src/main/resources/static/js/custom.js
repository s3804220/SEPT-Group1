function saveItem() { //post

    //get values from the html form
    var itemname = document.getElementById('item-name').value
    var description = document.getElementById('item-description').value
    var price = document.getElementById('item-price').value
    var fileinput = document.getElementById('item-images')
    var filenames = ''

    uploadImg()

    for (let i = 0; i < fileinput.files.length; ++i) {
        filenames += fileinput.files.item(i).name
        if(i!==fileinput.files.length-1){
            filenames+='|'
        }
    }

    fetch('http://localhost:8080/items', {
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        method: "POST",
        body: JSON.stringify({itemName: itemname,itemDescription: description, itemPrice: price, itemImage: filenames})
    }).then(res => res.json())
        .then(itemid => uploadImg(itemid))
        .then(() => document.getElementById('item-form-div').innerHTML=`<span style="color: #1c7430">Item saved successfully!</span>`)
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
            console.log(json)
            for (let i = 0; i < json.length; i++) {

                let images = json[i].itemImage.split("|")

                //let deleteLink = `<button onclick='deleteStudent(${id})'>Delete</button>`

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
                            <td class="cart__btn"><a href="#" class="primary-btn">Edit</a></td>
                            <td class="cart__close"><span class="icon_close"></span></td></tr>`
            }
        })
}
