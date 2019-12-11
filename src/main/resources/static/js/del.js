$(document).ready(function () {
    $(".btn-del").click(function (e) {
    var number = $(this).data('num');
        e.preventDefault();
        $.ajax({
            type: "POST",
            url: "/app/cart-del",
            data: {
                id: number
            },
            success: function (result) {
                alert("Прибыли данные: " + result);
                updateCounter();
            },
            error: function (msg) {
                alert("Oops");
            }
        });
    });

    function updateCounter() {
        $.ajax({
            type: "POST",
            url: "/app/cart_count_request",
            success: function (result) {
                console.log(result);
                $('#cartCount').text(result);
            }
        });
    }
});