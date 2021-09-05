$(function() {
    let params = new URLSearchParams(document.location.search.substring(1));

    let filterField = document.getElementById('filterField');
    let searchField = document.getElementById('search-input');
    let sortField = document.getElementById('sortField');

// Filter
    let filterValue = params.get('filterField');
    if (filterValue == null) filterValue = "All";
    let validFilter = $("#filterField").data("categories");
    console.log("@@@@@@@@@@@@validFilter: " + validFilter);
    console.log("@@@@@@@@@@@@filterValue: " + filterValue);

    if (filterValue && validFilter.includes(filterValue)) {
        filterField.value = filterValue;
        $("#filterField").niceSelect('update');
    } else if (filterValue && !validFilter.includes(filterValue)) {
        filterField.value = "All";
        $("#filterField").niceSelect('update');
    }

// Search
    let searchValue = params.get('search-input');
    console.log("@@@@@@@@@@@@ 1st search value: " + searchField.value);

    if (searchValue) {
        searchField.value = searchValue;
    }
    console.log("@@@@@@@@@@@@ 1st search value: " + searchField.value);
    


// Sorting

    //Get the sort value from the URL parameter
    let sortValue = params.get("sortField");
    let validSort = ["id","name","priceHTL","priceLTH"];

    if (sortValue && validSort.includes(sortValue)) {
        //If there exists a sort value in the URL parameter, set the option value and update JQuery nice-select
        sortField.value = sortValue;
        $("#sortField").niceSelect('update');
    } else if (sortValue && !validSort.includes(sortValue)){
        sortField.value = "id";
        $("#sortField").niceSelect('update');
    }
    $('#sortField').on('change', function() {
        let parentForm = $(this).closest("form");
        if (parentForm && parentForm.length > 0)
            parentForm.submit();
    });

    function searchBoard() {
        let parentForm = $(this).closest("form");
        if (parentForm && parentForm.length > 0)
            parentForm.submit();

    }

});