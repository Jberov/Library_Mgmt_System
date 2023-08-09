function getURLParam(){
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get("user");
}

function getHeader(){
    let parameter = getURLParam(), regex = /([97(8|9)]{3}[-][0-9]{1,5}[-][0-9]{0,7}[-][0-9]{0,6}[-][0-9])|([0-9]{13})/;

    if (regex.test(parameter)) {
        return "Isbn";
    }

    return "Name";
}

async function getUser(parameter){
    let url = 'http://localhost:8080/api/v1/users/info';
    if( getURLParam() != null) {
        url += '/';
        url += getURLParam();
    } else if( parameter != null) {
        url += '/';
        url += parameter;
    }

    let response = await $.ajax({
        url: url,
        type: 'GET',
        async: true,
        contentType: 'application/json; charset=utf-8',
        xhrFields: {
            withCredentials: true            
        },
        crossDomain:true
    });

    if(response.status == 400){
        alert("Грешен критерий за търсене");
        return false;
    }

    if(response.status == 403){
        alert("Липса на права");
        return false;
    }
    
    return response.user;
}

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

async function findUser(){
    const searchable = $("#searchValue").val();
    if (await getUser(searchable)) { 
        alert("Намерен потребител");
        window.location.replace("http://localhost/library-frontend/bootstrap-5-categories-template-main/UserInfo.html?user=" + searchable);
    } else {
        alert("Няма потребител");
    }
}

$(document).ready(async function(){
    $("#timestamp p").text(new Date().toLocaleDateString());

    $.ajax({
        url: 'http://localhost:8080/login',
        type: 'POST',
        xhrFields: {
            withCredentials: true            
        },
        crossDomain:true,
        success: function(result) {

        let user_role = result.Role[0].authority;
            if (user_role == "USER") {
                $('#navigation').children().each(function () {
                    if($(this).attr('id') != "home"){
                        $(this).remove();
                    }   
                });
            }
        }
    });

    let user = await getUser();

    if (user != null) {
        $("#email").text(user.email.toString());
        $("#username").text(user.username.toString());
        $("#telephone").text(user.telephoneNumber.toString());
        $("#role").text(user.role.toString());
        $("#address").text(user.address.toString());
        $("#firstName").text(user.firstName.toString());
        $("#midName").text(user.midName.toString());
        $("#lastName").text(user.lastName.toString());
    }

    $("#search-button").click(async function(){
        if(!await findBook()) {
            await findUser();
        }
    });
});