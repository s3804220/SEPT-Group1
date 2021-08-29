$(function() {
    let params = new URLSearchParams(document.location.search.substring(1));

    let filterField = document.getElementById('filterField');
    let sortField = document.getElementById('sortField');

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

    function searchBoard() {

        let parentForm1 = $(this).closest("form");
        if (parentForm1 && parentForm1.length > 0)
            parentForm1.submit();

    }

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
    })


    // if (localStorage.getItem('selectedOption')) {
    //
    //     sortField.value = $("#sortField option").eq(localStorage.getItem('selectedOption')).val();
    //     $("#sortField").niceSelect('update');
    //
    // }
    // $('#sortField').on('change', function() {
    //     localStorage.setItem('selectedOption', $('option:selected', this).index());
    //     var parentForm = $(this).closest("form");
    //     if (parentForm && parentForm.length > 0)
    //         parentForm.submit();
    // });
});