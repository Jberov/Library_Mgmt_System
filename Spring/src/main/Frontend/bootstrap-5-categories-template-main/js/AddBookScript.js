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
})