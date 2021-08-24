$(function() {
    let sortField = document.getElementById('sortField');

    if (localStorage.getItem('selectedOption')) {

        sortField.value = $("#sortField option").eq(localStorage.getItem('selectedOption')).val();
        $("#sortField").niceSelect('update');

    }
    $('#sortField').on('change', function() {
        localStorage.setItem('selectedOption', $('option:selected', this).index());
        var parentForm = $(this).closest("form");
        if (parentForm && parentForm.length > 0)
            parentForm.submit();
    });
});