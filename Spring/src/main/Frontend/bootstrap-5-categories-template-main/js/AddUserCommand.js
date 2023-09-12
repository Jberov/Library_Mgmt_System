async function validateInput() {
    $(".errorMsg").hide();
        let textInput, flag = false;
        $("#allInput").children(".stringInput").each(function(){
            textInput = $(this).find("input").val();
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
        let regexPattern = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{8,}$/;

        if (!regexPattern.test(textInput)) {
            $("#passwordError").text("Паролата трябва да съдържа поне една цифра, една главна и малка буква и да е с дължина от поне 8 символа");
            $("#passwordError").show();
            return false;
        }

        textInput = $("#email").val();
        regexPattern = new RegExp('@');

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

async function determineOperation() {
    const urlParams = new URLSearchParams(window.location.search);
    if ( urlParams.get("user") != null) {
        return 'PUT';
    }

    return 'POST';
}

function getParam(){
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get("user");
}

async function sendChangeRequest(method) {
    return $.ajax({
        url: 'http://localhost:8080/api/v1/users/' + getParam(),
        type: method,
        data: createJSONPayload(),
        contentType: 'application/json; charset=utf-8',
        dataType: 'text',
        async: true,
        xhrFields: {
            withCredentials: true            
        },statusCode: {
            200: function (xhr) {
                console.log(xhr);
                    $("#alertText").text(xhr);
                    $("#messageDiv").show();
                },
            201: function (xhr) {
                console.log(xhr);
                    $("#alertText").text(xhr);
                    $("#messageDiv").show();
                },
                400: function (xhr) {
                    console.log(xhr);
                    $("#errorText").text(xhr);
                    $("alertDiv").show();
                },
                403: function () {
                    console.log(xhr);

                    $("#errorText").text("Нямате необходимите привилегии");
                    $("alertDiv").show();
                },
                404: function (xhr) {
                    $("#errorText").text(xhr);
                    $("#alertDiv").show();
                },
                500: function (xhr) {
                    console.log(xhr);

                    $("#errorText").text(xhr);
                    $("#alertDiv").show();
                },
                502: function (xhr) {
                    $("#errorText").text(xhr);
                    $("#alertDiv").show();
                }
            }
    });
}

async function visualizeInfo(){
    const userResponse = await fetchUser(getParam());

    $("#username").val(userResponse.user.username);
    $("#password").val(userResponse.user.password);
    $("#email").val(userResponse.user.email);
    $("#telephone").val(userResponse.user.telephoneNumber);
    $("#address").val(userResponse.user.address);
    $("select").val(userResponse.user.role).trigger("chosen:updated");
    let name = userResponse.user.firstName + " " + userResponse.user.midName + " " + userResponse.user.lastName;
    $("#names").val(name);
}

async function fetchUser(name) {
     return await $.ajax({
        url: 'http://localhost:8080/api/v1/users/info/single/' + name,
        type: 'GET',
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        async: true,
        xhrFields: {
            withCredentials: true            
        }
    });
}

async function findUser(){
    const searchable = $("#searchValue").val();
    try {
        await fetchUser(searchable);
        window.location.replace("http://localhost/library-frontend/bootstrap-5-categories-template-main/UserInfo.html?user=" + searchable);
    } catch (error) {
        $("#errorText").text("Няма потребител или книга с такова име");
        $("#alertDiv").show();
    }
}

$(document).ready(async function(){
    $(".errorMsg").hide();
    $("#messageDiv").hide();
    $("#alertDiv").hide();
    let method = await determineOperation();

    if (method == 'PUT') {
       $("#register").text("Промени потребител");
        await visualizeInfo();
    }

    $("#register").click(async function(){

        if (validateInput()) {
            sendChangeRequest(method);
        } 
    });

    $("#search-button").click(async function(){
        if(! await findBook()) {
             findUser();
        }
    });

    $(".btn-close").click(function () {
        $("#messageDiv").hide();
        $("#alertDiv").hide();
    });
});

function getHeader(parameter){
    const regex = /([97(8|9)]{3}[-][0-9]{1,5}[-][0-9]{0,7}[-][0-9]{0,6}[-][0-9])|([0-9]{13})/;

    if (regex.test(parameter)) {
        return "Isbn";
    }

    return "Name";
}

async function getBookRequest(searchable) {
    return await $.ajax({
        url: 'http://localhost:8080/api/v1/books/' + searchable,
        type: 'GET',
        contentType: 'application/json; charset=utf-8',
        xhrFields: {
            withCredentials: true            
        },
        async: true,
        beforeSend: function(oJqXhr) {
            oJqXhr.setRequestHeader('Criteria', getHeader(searchable));
        }
    });
}

async function getBookResult(searchable){    
    try{
        await getBookRequest(searchable);
    } catch(error){
        return false;
    }

    return true;
}

async function findBook(){
    const searchable = $("#searchValue").val();
    if (await getBookResult(searchable)) { 
        window.location.replace("http://localhost/library-frontend/bootstrap-5-categories-template-main/BookInfo.html?book=" + searchable);
        return true;
    } else {
        return false;
    }
}

function createJSONPayload() {
    let jsonObj = new Object();
    jsonObj.username = $("#username").val();
    jsonObj.password = $("#password").val();
    jsonObj.email = $("#email").val();
    jsonObj.telephoneNumber = $("#telephone").val();
    jsonObj.address = $("#address").val();
    let names = $("#names").val().split(" ");
    jsonObj.firstName = names[0];
    jsonObj.midName = names[1];
    jsonObj.lastName = names[2];
    jsonObj.role = $('select').find(":selected").val();
    
    return JSON.stringify(jsonObj);
}
