function validateInput(){

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

$(document).ready(function(){
    $(".errorMsg").hide();
    $("button").click(function(){
        $.ajax({
            url: 'http://localhost:8080/api/v1/books',
            type: 'POST',
            data: createJSONPayload(),
            contentType: 'application/json; charset=utf-8',
            dataType: 'text',
            async: true,
            success: function(result) {
               alert(result.message);
            },
            error: function() {
                alert("Грешка при операцията")
            }
        });
    });
})