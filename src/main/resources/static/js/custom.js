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

