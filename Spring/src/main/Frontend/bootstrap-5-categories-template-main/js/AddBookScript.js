function validateInput(){
    let valid = true;
    $("#allInput").children(".input-group, .textarea-input").each(function() {
        let textInput = $(this).find("input, textarea").val();
        if(textInput == null || textInput == ""){
            $(this).find(".errorMsg").show();
            valid = false;
        }
    });

    if(!valid) {
        return false;
    }

    let isbn = $("#isbn").val();
    let isbnRegex = /([97(8|9)]{3}[-][0-9]{1,5}[-][0-9]{0,7}[-][0-9]{0,6}[-][0-9])|([0-9]{13})/
    
    if(!isbnRegex.test(isbn)){
        $("#isbnError").text("Невалиден формат на isbn номер!");
        $("#isbnError").show();
        valid = false;
    }

    return valid;
}

function getHeader(parameter){
    const regex = /([97(8|9)]{3}[-][0-9]{1,5}[-][0-9]{0,7}[-][0-9]{0,6}[-][0-9])|([0-9]{13})/;

    if (regex.test(parameter)) {
        return "Isbn";
    }

    return "Name";
}

async function getBookRequest(searchable) {
    console.log(getHeader(searchable));
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


function createJSONPayload() {
    let jsonObj = new Object();
    jsonObj.isbn = $("#isbn").val();
    jsonObj.count = parseInt($("#count").val());
    jsonObj.author = $("#author").val();
    jsonObj.authorDescription = $("#authorDesc").val();
    jsonObj.name = $("#bookName").val();
    jsonObj.genre = $("#Genre").val();
    jsonObj.genreDescription = $("#GenreDesc").val();
    jsonObj.description = $("#description").val();
    return JSON.stringify(jsonObj);
}

async function getBookResult(searchable){    
    return await getBookRequest(searchable);
}

async function findBook(){
    const searchable = $("#searchValue").val();
    if (await getBookResult(searchable)) { 
        window.location.replace("http://localhost/library-frontend/bootstrap-5-categories-template-main/BookInfo.html?book=" + searchable);
    } else {
        alert("Книгата не е намерена");
    }
 
}

$(document).ready(async function(){
    $(".errorMsg").hide();
    $("button").click(function(){
        $(".errorMsg").hide();
        if(validateInput()){
            $.ajax({
                url: 'http://localhost:8080/api/v1/books',
                type: 'POST',
                data: createJSONPayload(),
                contentType: 'application/json; charset=utf-8',
                dataType: 'text',
                async: true,
                xhrFields: {
                    withCredentials: true            
                },
                success: function(result) {
                    alert(result.message);
                },
                error: function(result) {
                    switch(result.status){
                        case(400):
                            alert(result);
                            return;
                        case(500):
                            alert("Грешка в сървъра");
                            return;
                        case(502):
                            alert("Грешка при вързване към БД");
                            return;
                        default:
                            alert("Грешка");
                            return;
                    }
                }
            });
        }
    });

    $("#search-button").click(async function(){
        await findBook();
    });
})