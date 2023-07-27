$(document).ready(function(){
    $(".errorMsg").hide();
    $("button").click(function(){
        $(".errorMsg").hide();
        let textInput, flag = false;
        console.log($("#allInput").children(".input-group").length);
        $("#allInput").children(".input-group").each(function(){
            textInput = $(this).find("input").val();
            console.log(textInput.toString());
            if( textInput == "" || textInput == undefined ) {
                $(this).find(".errorMsg").show();
                flag = true;
                return;
            } else if (textInput.length < 8) {
                let errorObj = $(this).find(".errorMsg").text("Полето трябва да съдържа поне 8 знака");
                $(errorObj).show();
                flag = true;
                return;
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
        regexPattern = /@[a-z]*.[a-z]{2,4}$/
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

        $.ajax({
            url: 'http://localhost:8080/api/v1/users',
            type: 'POST',
            data: createJSONPayload(),
            contentType: 'application/json; charset=utf-8',
            dataType: 'text',
            async: true,
            success: function(result) {
               alert(result);
               console.log(result.toString());
               window.location.replace("http://localhost/library-frontend/bootstrap-5-login-cover-template-main/index.html");
                // Do something with the result
            },
            error: function(result) {
                if(result.status == 400) {
                    console.log(result);
                    alert("Phone or email are already used");
                } else if (result.status == 409){
                    alert("Username is taken");
                }else {
                    alert("Server error");
                    console.log(result);
                }
            }
        });
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