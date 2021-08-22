// jQuery(function()
// {
//
//
//     jQuery(#sortField).change(function() {
//         console.log("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@Hello");
//         this.form.submit();
//     });
//
//     // window.location = elm.value+".php";
//     // window.location = 'shop/page=1' + '?sortField=itemName&sortDir=' + ${reverseSortDir}}
// });
$(document).ready(function() {
    $('#sortField').on('change', function() {
        console.log("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@Hello");
        var parentForm = $(this).closest("form");
        if (parentForm && parentForm.length > 0)
            parentForm.submit();
    });
});