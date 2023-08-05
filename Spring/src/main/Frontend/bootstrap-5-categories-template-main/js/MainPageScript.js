async function fetchBooks(){
    return books = await $.ajax({
        url: 'http://localhost:8080/api/v1/books',
        type: 'GET',
        xhrFields: {
            withCredentials: true            
        },
        async:true
    });
}

async function deleteBookRequest(event){
    let urlString = 'http://localhost:8080/api/v1/books/' + $(event.target).siblings("h5").text();
    console.log(urlString);
    return books = await $.ajax({
        url: urlString,
        type: 'DELETE',
        xhrFields: {
            withCredentials: true            
        }
    });
}

async function fetchBookIsbnRequest(event){
    console.log($(event.target).siblings("h5").text());
    console.log('http://localhost:8080/api/v1/books/' + $(event.target).siblings("h5").text());
    let urlString = 'http://localhost:8080/api/v1/books/' + $(event.target).siblings("h5").text();
    books = await $.ajax({
        url: urlString,
        type: 'GET',
        xhrFields: {
            withCredentials: true            
        },
        beforeSend: function(oJqXhr) {
            oJqXhr.setRequestHeader('Criteria', 'Name');
        }
    });

    return books.book.isbn;
}

async function leaseBookRequest(event) {
    let isbn = await fetchBookIsbnRequest(event);

    let url = 'http://localhost:8080/api/v1/books/rental/' + isbn;

    let lease = $.ajax({
        url: url,
        type: 'PATCH',
        xhrFields: {
            withCredentials: true            
        },
    });

    return lease;
}

async function leaseBook(event){
    let lease = await leaseBookRequest(event);

    switch (lease.status) { 
        case 404:
            alert("No such book");
            return;
        case 400:
            alert("Bad request");
            return;
        case 500:
            alert("Server error");
            return; 
        case 502:
            alert("Gateway error");
            return;        
    };

    alert("Книгата " + lease.response.name + " успешно заета");
}

async function deleteBook(event){
    let bookResponse = await deleteBookRequest(event);

    switch (bookResponse.status) { 
        case 404:
            alert("No such book");
            break;
        case 400:
            alert("Bad request");
            break;
        case 500:
            alert("Server error");
            break; 
        case 502:
            alert("Gateway error");
            break;        
    };
    location.reload();
    alert("Книгата е изтрита");
}

async function loadBookList(){
    let bookResponse = await fetchBooks();
    if (bookResponse != null) {
        let bookList = bookResponse.books;
        bookList.forEach(element => {
            console.log("Book name " + element.name);
            $("#bookSection").append(
                '<div class="row bookList">' +
            '            <div class="col-md-4 mb-4">' +
            '              <div class="bg-image hover-overlay shadow-1-strong rounded ripple" data-mdb-ripple-color="light">' +
            '                <img src="./img/Open-book-bg.png" class="img-fluid" />' +
            '                <a href="BookInfo.html?book=' + element.name + '">' +
            '                  <div class="mask" style="background-color: rgba(251, 251, 251, 0.15);"></div>' +
            '                </a>' +
            '              </div>' +
            '            </div>' +
            '            <div class="col-md-8 mb-4 singleBook">' +
            '              <h5 class="BookNameHeading">' + element.name + '</h5>' +
            '              <p>' + element.description +
            '              </p>' +
            '              <button type="button" class="btn btn-primary LeaseBookBtn">Заеми</button>' +
            '              <button type="button" class="btn btn-success UpdateBookBtn">Промени</button>' +
            '              <button type="button" class="btn btn-danger DeleteBookBtn">Премахни</button>' +
            '            </div>' +
            '          </div>'
            )
        });
    }
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
        
            console.log("Status for search is " + response.status);
        
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
        window.location.replace("http://localhost/library-frontend/bootstrap-5-categories-template-main/BookInfo.html?book=" + searchable);
    } else {
        alert("Книгата не е намерена");
    }
 
}

$(document).ready(async function(){
   await loadBookList();

    $.ajax({
        url: 'http://localhost:8080/login',
        type: 'POST',
        xhrFields: {
            withCredentials: true            
        },
        crossDomain:true,
        success: function(result) {

        var user_role = result.Role[0].authority;
            if (user_role == "USER") {
                $('#navigation').children().each(function () {
                    if($(this).attr('id') != "home"){
                        $(this).remove();
                    }   
                });

            $('.UpdateBookBtn').hide();
            $('.DeleteBookBtn').hide();
        }
        }
    });

    $(document).on("click",".LeaseBookBtn",function(event){
       leaseBook(event);
    });

    $(document).on("click",".DeleteBookBtn",function(event){
        deleteBook(event);
    });

    $("#search-button").click(async function(){
        await findBook();
    });
});