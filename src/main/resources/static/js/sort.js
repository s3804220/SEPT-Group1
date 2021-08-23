$(function() {
    if (localStorage.getItem('selectedOption')) {
        $("#sortField option").eq(localStorage.getItem('selectedOption')).prop('selected', true);
    }
    $('#sortField').on('change', function() {
        localStorage.setItem('selectedOption', $('option:selected', this).index());
        var parentForm = $(this).closest("form");
        if (parentForm && parentForm.length > 0)
            parentForm.submit();
    });
});