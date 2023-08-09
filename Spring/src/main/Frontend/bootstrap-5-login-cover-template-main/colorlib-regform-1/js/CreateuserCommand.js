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
    return true;
}

function determineOperation() {
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
        url: 'http://localhost:8080/api/v1/users',
        type: method,
        data: createJSONPayload(),
        contentType: 'application/json; charset=utf-8',
        dataType: 'text',
        async: true,
        success: function(result) {
           alert(result);
           console.log(result.toString());
           window.location.replace("http://localhost/library-frontend/bootstrap-5-login-cover-template-main/index.html");
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
}

async function visualizeInfo(){
    let user = await fetchUser();

    $("#username").val(user.username);
    $("#password").val(user.password);
    $("#email").val(user.email);
    $("#telephone").val(user.telephoneNumber);
    $("#address").val(user.address);
    $("select").val(user.role).trigger("chosen:updated");
    let name = user.firstName + " " + user.midName + " " + user.lastName;
    $("#names").val(name);
}

async function fetchUser(name=getParam()) {
     return await $.ajax({
        url: 'http://localhost:8080/api/v1/users/' + name,
        type: 'GET',
        data: createJSONPayload(),
        contentType: 'application/json; charset=utf-8',
        dataType: 'text',
        async: true,
        error: function(result) {
            alert(result.toString());
        }
    });
}

async function findUser(){
    const searchable = $("#searchValue").val();
    let user = await fetchUser(searchable);
    if (user != null && user != undefined) { 
        window.location.replace("http://localhost/library-frontend/bootstrap-5-categories-template-main/UserInfo.html?user=" + searchable);
    } else {
        alert("Няма потребител");
    }
}

$(document).ready(async function(){
    $(".errorMsg").hide();
    let method = determineOperation();

    if (method == 'PUT') {
       await visualizeInfo();
    }

    $("button").click(async function(){
        if (validateInput()) {
            await sendChangeRequest(method);
        } 
    });

    $("#search-button").click(async function(){
        if(!await findBook()) {
            await findUser();
        }
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
        crossDomain:true,
        beforeSend: function(oJqXhr) {
            oJqXhr.setRequestHeader('Criteria', getHeader(searchable));
        },
        success: function (response){
            if(response.status == 400){
                alert("Грешен критерий за търсене");
                return false;
            }
                
            if (response.status == 200) {
                return true;
            }
            
            return false;
        }
    });
}

async function getBookResult(searchable){    
    return await getBookRequest(searchable);
}

async function findBook(){
    const searchable = $("#searchValue").val();
    if (await getBookResult(searchable)) { 
        alert("Намерена книга");
        window.location.replace("http://localhost/library-frontend/bootstrap-5-categories-template-main/BookInfo.html?book=" + searchable);
        return true;
    } else {
        alert("Книгата не е намерена");
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
    jsonObj.role = "USER";
    let names = $("#names").val().split(" ");
    jsonObj.firstName = names[0];
    jsonObj.midName = names[1];
    jsonObj.lastName = names[2];

    if ($('select').find(":selected").val() != null) {
        console.log($('select').find(":selected").val())
        jsonObj.role = $('select').find(":selected").val();
    }
    return JSON.stringify(jsonObj);
}