$(document).ready(function () {
    $(".btn-add").click(function (e) {
    var number = $(this).data('num');
    var price = $(this).data('price');
        e.preventDefault();
        $.ajax({
            type: "POST",
            url: "/app/cart",
            data: {
                id: number,
                price : price
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
                $('#cartCountNav').text(result);
            }
        });
    }
});