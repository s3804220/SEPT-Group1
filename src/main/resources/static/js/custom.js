document.getElementById("save-button").addEventListener("click", checkInput)
document.addEventListener('DOMContentLoaded',loadInfo)

var replaceImg = false
//Check whether to replace item images by checking what radio button is selected in the form
$('input:radio[name="image-upload"]').on('click change', function(e) {
    if(e.currentTarget.value==="no"){
        document.getElementById('image-upload-btn').style.display = "block"
        replaceImg = true
    }else {
        document.getElementById('image-upload-btn').style.display = "none"
        replaceImg = false
    }
});

function checkInput(){
    //Get the inputs entered by the admin from the HTML form elements
    let id = document.getElementById('item-input-id').value
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
    //Check if item name is blank
    if(!itemname){
        nameMessage.style.display = "block"
        nameMessage.style.color = "red"
        nameMessage.innerHTML = 'Please enter an item name!'
        validated = false
    }else {
        nameMessage.style.display = "none"
    }
    //Check if item description is blank
    if(!description){
        descriptionMessage.style.display = "block"
        descriptionMessage.style.color = "red"
        descriptionMessage.innerHTML = 'Please enter a description for the item!'
        validated = false
    }else {
        descriptionMessage.style.display = "none"
    }
    //Check if price is not blank and is a valid number
    if(!price || isNaN(price)){
        priceMessage.style.display = "block"
        priceMessage.style.color = "red"
        priceMessage.innerHTML = 'Please enter a valid price!'
        validated = false
    }
    //Check if price is negative
    else if (price<=0){
        priceMessage.style.display = "block"
        priceMessage.style.color = "red"
        priceMessage.innerHTML = 'Please do not enter negative or 0 prices!'
        validated = false
    }else {
        priceMessage.style.display = "none"
    }
    //Check if the user wants to replace the images
    if(!id || id && replaceImg){
        //Check if the user has not selected any files
        if(fileinput.files.length===0){
            fileMessage.style.display = "block"
            fileMessage.style.color = "red"
            fileMessage.innerHTML = 'Please select at least 1 file!'
            validated = false
        }
        //Check if there are more than 5 files
        else if(fileinput.files.length>5){
            fileMessage.style.display = "block"
            fileMessage.style.color = "red"
            fileMessage.innerHTML = 'Please select a maximum of 5 images!'
            validated = false
        } else {
            for (let i = 0; i < fileinput.files.length; ++i) {
                //For each file in the selected files array, check if the extension is a valid image format
                let filename = fileinput.files.item(i).name
                let array = filename.split('.')
                const validExtensions = ["jpg", "jpeg", "bmp", "gif", "png"]
                if(array.length===1 || ( array[0] === "" && array.length === 2 )){
                    fileMessage.style.display = "block"
                    fileMessage.style.color = "red"
                    fileMessage.innerHTML = 'Please select a file with a valid name and extension!'
                    validated = false
                    break
                }else if(!validExtensions.includes(array.pop().toLowerCase())){
                    fileMessage.style.display = "block"
                    fileMessage.style.color = "red"
                    fileMessage.innerHTML = 'Please select image files only!'
                    validated = false
                    break
                }else if(fileinput.files.item(i).size>5242880){
                    //Check if the image file size is larger than 5MB
                    fileMessage.style.display = "block"
                    fileMessage.style.color = "red"
                    fileMessage.innerHTML = 'The max size per file is 5MB only!'
                    validated = false
                    break
                }else{
                    fileMessage.style.display = "none"
                }
            }
        }
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
    let category = document.getElementById('item-category').value
    let availability = document.querySelector('input[name="item-availability"]:checked').value
    let fileinput = document.getElementById('item-images')
    let filenames = ''

    //Join all image file names into a string separated by |
    //that string will be stored in the items database
    for (let i = 0; i < fileinput.files.length; ++i) {
        filenames += fileinput.files.item(i).name
        if(i!==fileinput.files.length-1){
            filenames+='|'
        }
    }

    //If there is no specified ID, add a new item with a POST request
    if(!id){
        fetch('/items', {
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            method: "POST",
            body: JSON.stringify({itemName: itemname,itemDescription: description,itemPrice: price,category: category,availability:availability,itemImage: filenames})
        }) //Get the newly added item's ID from the response
            .then(res => res.json())
            // Call function to upload all image files
            .then(itemid => uploadImg(itemid))
            //Display the appropriate message to the admin in HTML
            .then(() => document.getElementById('item-form-div').innerHTML=`<span style="color: #1c7430">Item saved successfully!<br>You can view a list of all items in the database <a href="/item-list" class="item-link">here</a>.</span>`)
    }else{
        //If there is a specified ID, update the item with that ID in the database with a PUT request
        //the rest of the process is similar to above
        fetch('/items/images/'+id)
            .then(res => res.text())
            .then(images => {
                if(!replaceImg){
                    //If the Admin wants to keep old images, fetch and store the old image names so they will not get replaced with new ones
                    filenames = images
                }else{
                    //If the Admin wants to upload new images, then delete all old images
                    fetch('/item/'+id+'/image', {
                        method: "DELETE"
                    })
                }
            }).then(() => {
            fetch('/items', {
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                method: "PUT",
                body: JSON.stringify({id:id,itemName: itemname,itemDescription: description,itemPrice: price,category: category,availability:availability,itemImage: filenames})
            }).then(res => res.json())
                .then(itemid => uploadImg(itemid))
                .then(() => document.getElementById('item-form-div').innerHTML=`<span style="color: #1c7430">Item saved successfully!<br>You can view a list of all items in the database <a href="/item-list" class="item-link">here</a>.</span>`)
        })
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
    fetch('/item/'+id+'/image', {
        method: "POST",
        body: imgData
    })
}

function loadInfo(){
    let params = new URLSearchParams(document.location.search.substring(1))
    let itemid = params.get("id")

    //Check if there is an ID parameter in the URL
    if(itemid){
        document.getElementById('upload-choice').style.display = "block"
        document.getElementById('image-upload-btn').style.display = "none"

        //If the ID is a valid number
        if(!isNaN(Number(itemid))){
            let id = document.getElementById('item-input-id')
            let name = document.getElementById('item-name')
            let description = document.getElementById('item-description')
            let price = document.getElementById('item-price')
            let category = document.getElementById('item-category')

            //Get information for the item with the specified ID in the database
            fetch('/items/'+itemid)
                .then(res => res.json())
                .then(json => {
                    //If the item exists and there is information to display, display it in the form for admin to edit
                    if(json){
                        id.value = json.id
                        name.value =  json.itemName
                        description.value = json.itemDescription
                        price.value = json.itemPrice
                        category.value = json.category
                        $("#item-category").niceSelect('update')
                        if(json.availability){
                            document.getElementById('available-yes').checked = true
                        }else{
                            document.getElementById('available-yes').checked = false
                            document.getElementById('available-no').checked = true
                        }
                    }else{
                        //If the item with that ID cannot be found, display a message to the admin
                        document.getElementById('item-form-div').innerHTML=`<span style="color: #cc1825">An item with that ID doesn't exist!<br>Please recheck the <a href="/item-list" class="item-link">item list</a> or your database to find the item you want to edit.
                    <br>Or you can proceed to add a new item <a href="/item-form" class="item-link">here</a>.</span>`
                    }
                })
        }else{
            //If the ID is invalid, display a message to the admin
            document.getElementById('item-form-div').innerHTML=`<span style="color: #cc1825">An item with that ID doesn't exist!<br>Please recheck the <a href="/item-list" class="item-link">item list</a> or your database to find the item you want to edit.
                    <br>Or you can proceed to add a new item <a href="/item-form" class="item-link">here</a>.</span>`
        }

    }
}
