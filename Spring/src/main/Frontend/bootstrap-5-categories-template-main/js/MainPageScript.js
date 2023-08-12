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
    return books = await $.ajax({
        url: urlString,
        type: 'DELETE',
        xhrFields: {
            withCredentials: true            
        }
    });
}

async function fetchBookIsbnRequest(event){
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

    return books.book;
}

async function leaseBookRequest(event) {
    const book = await fetchBookIsbnRequest(event);

    const url = 'http://localhost:8080/api/v1/books/rental/' + book.isbn;

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
    const lease = await leaseBookRequest(event);

    switch (lease.status) { 
        case 404:
            alert("Книгата не е налична");
            return;
        case 500:
            alert("Недостъпна система");
            return; 
        case 502:
            alert("Проблем със системата");
            return;        
    };

    alert("Книгата " + lease.response.name + " успешно заета");
}

async function deleteBook(event){
    let bookResponse = await deleteBookRequest(event);

    switch (bookResponse.status) { 
        case 404:
            alert("Не всички са върнали тази книга. Триенето е преустановено");
            return;
        case 400:
            alert("Проблем със заявката");
            return;
        case 500:
            alert("Недостъпна система");
            return; 
        case 502:
            alert("Проблем със системата");
            return;        
    };
    location.reload();
    alert("Книгата е изтрита");
}

async function loadBookList(){
    let bookResponse = await fetchBooks();
    if (bookResponse != null) {
        let bookList = bookResponse.books;
        bookList.forEach(element => {
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
            '              <button type="button" class="btn btn-primary UpdateBookBtn">Промени</button>' +
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
        }
    });
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
       alert("Няма потребител или книга с такова име");
   }
}

async function findBook(){
    const searchable = $("#searchValue").val();

    try{ 
        await getBookRequest(searchable);
        window.location.replace("http://localhost/library-frontend/bootstrap-5-categories-template-main/BookInfo.html?book=" + searchable);
    } catch (error) {
        console.log("No book found");
        return false;
    }
    return true;
}

async function updateBookCount(event){
    let bookResponse = await fetchBookIsbnRequest(event);

    const promptResp = prompt("Моля въведете с колко бройки ще се увеличи книгата", "0");

    if (promptResp !== null && promptResp !== "0") {
        bookResponse.count = parseInt(promptResp);
            await $.ajax({
                url: 'http://localhost:8080/api/v1/books',
                type: 'POST',
                data: JSON.stringify(bookResponse),
                contentType: 'application/json; charset=utf-8',
                dataType: 'text',
                async: true,
                xhrFields: {
                    withCredentials: true            
                },
                error: function(result) {
                    switch(result.status){
                        case(400):
                            alert(result.message);
                            return;
                        case(500):
                            alert("Грешка");
                            return;
                        case(502):
                            alert(result.message);
                            return;
                        default:
                            alert("Грешка");
                            return;
                    }
                }
            });    
        alert("Книгата е актуализирана");
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

    $(document).on("click",".UpdateBookBtn",function(event){
       updateBookCount(event);
    });

    $(document).on("click",".DeleteBookBtn",function(event){
        if (confirm("Искате ли да изтриете тази книга?")){
            deleteBook(event);
        }
    });

    $("#search-button").click(async function(){
        if(! await findBook()) {
            await findUser();
        }
    });
});