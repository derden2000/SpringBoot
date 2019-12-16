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
            beforeSend: function (xhr, settings)
            {
                var CSRFToken = $("meta[name='_csrf']").attr("content");console.log(CSRFToken);
                var CSRFHeader = $("meta[name='_csrf_header']").attr("content");console.log(CSRFHeader);
                xhr.setRequestHeader(CSRFHeader, CSRFToken);
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
            beforeSend: function (xhr)
            {
                var CSRFToken = $("meta[name='_csrf']").attr("content");console.log(CSRFToken);
                var CSRFHeader = $("meta[name='_csrf_header']").attr("content");console.log(CSRFHeader);
                xhr.setRequestHeader(CSRFHeader, CSRFToken);
            },
            success: function (result) {
                console.log(result);
                $('#cartCount').text(result);
            }
        });
    }
});