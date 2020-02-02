$(document).ready(function () {
    $('body').on('submit', '#myform', function (e) {
    var phone = $('input[name="phone"]').val();
    var formVar = this;
        e.preventDefault();
        if (phone == '') {
        alert('Вы не указали телефон. Введите номер телефона')
        }
        $.ajax({
            type: "POST",
            url: "/app/check_login",
            data: {
                phone: phone,
            },
            beforeSend: function (xhr, settings)
            {
                var CSRFToken = $("meta[name='_csrf']").attr("content");console.log(CSRFToken);
                var CSRFHeader = $("meta[name='_csrf_header']").attr("content");console.log(CSRFHeader);
                xhr.setRequestHeader(CSRFHeader, CSRFToken);
            },
            success: function (result) {
                if (result == true) {
                formVar.submit();
                } else {
                alert("Пользователя с таким телефоном не существует. Проверьте Ваш телефон")
                }
            },
            error: function (msg) {
                console.log(msg);
                alert("Process failed: " + msg);
            }
        });
    });
});