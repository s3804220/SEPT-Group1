//$(document).ready(loadInfo())
deleteBtn = document.getElementsByClassName("delete-button")

for (let i = 0; i < deleteBtn.length; i++) {
    deleteBtn[i].addEventListener("click", function (){
      deleteItem($(this).data("itemid"))
    })
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
            })) //Then reload the page to show the updated list
            .then(() => window.location = '/item-list')
    }
}