function getURLParam(){
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get("book");
}

function getHeader(){
    let parameter = getURLParam(), regex = /([97(8|9)]{3}[-][0-9]{1,5}[-][0-9]{0,7}[-][0-9]{0,6}[-][0-9])|([0-9]{13})/;

    if (regex.test(parameter)) {
        return "Isbn";
    }

    return "Name";
}

async function sendFetchRequest(){
    let response = await $.ajax({
        url: 'http://localhost:8080/api/v1/books/' + getURLParam(),
        type: 'GET',
        async: true,
        contentType: 'application/json; charset=utf-8',
        xhrFields: {
            withCredentials: true            
        },
        crossDomain:true,
        beforeSend: function(oJqXhr) {
            oJqXhr.setRequestHeader('Criteria', getHeader());
        }
    });

    if(response.status == 400){
        alert("Грешен критерий за търсене");
        return null;
    }
    
    return response.book;
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

    let book = await sendFetchRequest();

    if (book != null) {
        console.log("Reporting here for HTML init");
        $("#bookName").text(book.name.toString());
        $("#isbn").text(book.isbn.toString());
        $("#bookAuthor").text(book.author.toString());
        $("#authorDesc").text(book.authorDescription.toString());
        $("#bookDesc").text(book.description.toString());
        $("#bookGenre").text(book.genre.toString());
        $("#genreDesc").text(book.genreDescription.toString());
        $("#count").text(book.count.toString());
    }
});