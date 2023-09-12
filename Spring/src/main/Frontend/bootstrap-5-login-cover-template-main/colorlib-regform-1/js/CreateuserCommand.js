function validateInput() {
    $(".errorMsg").hide();
        let textInput, flag = false;
        console.log($("#allInput").children(".input-group").length);
        $("#allInput").children(".input-group").each(function(){
            textInput = $(this).find("input").val();
            console.log(textInput.toString());
            if( textInput == "" || textInput == undefined ) {
                $(this).find(".errorMsg").show();
                flag = true;
            } else if (textInput.length < 8) {
                let errorObj = $(this).find(".errorMsg").text("Полето трябва да съдържа поне 8 знака");
                $(errorObj).show();
                flag = true;
            }
        });

        if(flag) {
            return false;
        }

        textInput = $("#password").val();
        let regexPattern = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{8,}$/

        if (!regexPattern.test(textInput)) {
            $("#passwordError").text("Паролата трябва да съдържа поне една цифра, една главна и малка буква и да е с дължина от поне 8 символа");
            $("#passwordError").show();
            return false;
        }

        textInput = $("#email").val();
        regexPattern = /@/;
        if (!regexPattern.test(textInput)) {
            $("#emailError").text("Невалиден имейл");
            $("#emailError").show();
            return false;
        }

        textInput = $("#telephone").val();
        regexPattern = /^[0-9]{10}$/
        if (!regexPattern.test(textInput)) {
            $("#telError").text("Невалиден телефон");
            $("#telError").show();
            return false;
        }

        textInput = $("#names").val();
        regexPattern = /^[а-яА-Я]* [а-яА-Я]* [а-яА-Я]*$/
        if (!regexPattern.test(textInput)) {
            $("#namesMsg").text("Моля попълнете своите имена само с една шпация разстояние помежду им");
            $("#namesMsg").show();
            return false;
        }
    return true;
}

function sendCreateRequest() {
    return $.ajax({
        url: 'http://localhost:8080/api/v1/users',
        type: "POST",
        data: createJSONPayload(),
        contentType: 'application/json; charset=utf-8',
        dataType: 'text',
        statusCode: {
            200: function() {
                window.location.replace("http://localhost/library-frontend/bootstrap-5-login-cover-template-main/index.html");
            },
            400: function(xhr) {
                $("#errorText").text(xhr);
                $("alertDiv").show();
            },
            500: function(xhr) {
                $("#errorText").text(xhr);
                $("alertDiv").show();
            },
            502: function(xhr) {
                $("#errorText").text(xhr);
                $("alertDiv").show();
            }
        }
    });
}

$(document).ready(function(){
    $(".errorMsg").hide();
    $("#messageDiv").hide();
    $("#alertDiv").hide();

    $(".btn--radius").click(function(){
        if (validateInput()) {
            sendCreateRequest();
        } 
    });

    $(".btn-close").click(function () {
        $("#messageDiv").hide();
        $("#alertDiv").hide();
    });
});

function createJSONPayload() {
    let jsonObj = new Object();
    jsonObj.username = $("#username").val();
    jsonObj.password = $("#password").val();
    jsonObj.email = $("#email").val();
    jsonObj.telephoneNumber = $("#telephone").val();
    jsonObj.address = $("#address").val();
    jsonObj.role = "USER";
    let names = $("#names").val().split(" ");
    jsonObj.firstName = names[0];
    jsonObj.midName = names[1];
    jsonObj.lastName = names[2];
    return JSON.stringify(jsonObj);
}