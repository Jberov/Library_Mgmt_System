async function fetchBooks() {
    return books = await $.ajax({
        url: 'http://localhost:8080/api/v1/books',
        type: 'GET',
        xhrFields: {
            withCredentials: true
        },
        async: true,
        statusCode: {
            500: function (xhr) {
                $("#errorText").text(xhr.responseJSON.error);
                $("alertDiv").show();           
            },
            502: function (xhr) {
                $("#errorText").text(xhr.responseJSON.error);
                $("alertDiv").show();           
            }
        }
    });
}

async function deleteBookRequest(event) {
    let urlString = 'http://localhost:8080/api/v1/books/' + $(event.target).siblings("h5").text();
    await $.ajax({
        url: urlString,
        type: 'DELETE',
        xhrFields: {
            withCredentials: true
        },
        statusCode: {
            200: function (xhr) {
                $("#alertText").text(xhr.message);
                $("#messageDiv").show();
            },
            404: function (xhr) {
                $("#errorText").text(xhr.responseJSON.error);
                $("alertDiv").show();
            },
            409: function (xhr) {
                $("#errorText").text(xhr.responseJSON.error);
                $("alertDiv").show();            
            },
            500: function (xhr) {
                $("#errorText").text(xhr.responseJSON.error);
                $("alertDiv").show();           
            },
            502: function (xhr) {
                $("#errorText").text(xhr.responseJSON.error);
                $("alertDiv").show();           
            }
        }
    });
}

async function fetchBookIsbnRequest(event) {
    let urlString = 'http://localhost:8080/api/v1/books/' + $(event.target).siblings("h5").text();
    books = await $.ajax({
        url: urlString,
        type: 'GET',
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        xhrFields: {
            withCredentials: true
        },
        beforeSend: function (oJqXhr) {
            oJqXhr.setRequestHeader('Criteria', 'Name');
        },
        statusCode: {
            400: function (xhr) {
                $("#errorText").text(xhr.responseJSON.error);
                $("alertDiv").show();
            },
            404: function (xhr) {
                $("#errorText").text(xhr.responseJSON.error);
                $("alertDiv").show();
            },
            500: function (xhr) {
                $("#errorText").text(xhr.responseJSON.error);
                $("alertDiv").show();
            },
            502: function (xhr) {
                $("#errorText").text(xhr.responseJSON.error);
                $("alertDiv").show();
            }
        }
    });

    return books.book;
}

async function leaseBookRequest(event) {
    const book = await fetchBookIsbnRequest(event);

    const url = 'http://localhost:8080/api/v1/books/rental/' + book.isbn;

    await $.ajax({
        url: url,
        type: 'PATCH',
        async: true,
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        xhrFields: {
            withCredentials: true
        },
        statusCode: {
            200: function (xhr) {
                location.reload();
                $("#alertText").text(xhr.message);
                $("#messageDiv").show();
            },
            404: function (xhr) {
                $("#errorText").text(xhr.responseJSON.message);
                $("alertDiv").show();
            },
            500: function (xhr) {
                $("#errorText").text(xhr.responseJSON.message);
                $("alertDiv").show();
            },
            502: function (xhr) {
                $("#errorText").text(xhr.responseJSON.message);
                $("alertDiv").show();
            }
        }
    });
}

async function loadBookList() {
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

function getHeader(parameter) {
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
        crossDomain: true,
        beforeSend: function (oJqXhr) {
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

async function findUser() {
    const searchable = $("#searchValue").val();
    try {
        await fetchUser(searchable);
        window.location.replace("http://localhost/library-frontend/bootstrap-5-categories-template-main/UserInfo.html?user=" + searchable);
    } catch (error) {
        $("#errorText").text("Няма потребител или книга с такова име");
        $("alertDiv").show();
    }
}

async function findBook() {
    const searchable = $("#searchValue").val();

    try {
        await getBookRequest(searchable);
        window.location.replace("http://localhost/library-frontend/bootstrap-5-categories-template-main/BookInfo.html?book=" + searchable);
    } catch (error) {
        return false;
    }
    return true;
}

async function updateBookCount(event) {
    let bookResponse = await fetchBookIsbnRequest(event);

    const promptResp = prompt("Моля въведете с колко бройки ще се увеличи книгата", "0");

    if (promptResp !== null && promptResp !== "0") {
        bookResponse.count = parseInt(promptResp);
        await $.ajax({
            url: 'http://localhost:8080/api/v1/books',
            type: 'POST',
            data: JSON.stringify(bookResponse),
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            async: true,
            xhrFields: {
                withCredentials: true
            },
            statusCode: {
                201: function (xhr) {
                    $("#alertText").text(xhr.message);
                    $("#messageDiv").show();
                },
                400: function (xhr) {
                    $("#errorText").text(xhr.responseJSON.error);
                    $("alertDiv").show();
                },
                404: function (xhr) {
                    $("#errorText").text(xhr.responseJSON.error);
                    $("#alertDiv").show();
                },
                500: function (xhr) {
                    $("#errorText").text(xhr.responseJSON.error);
                    $("#alertDiv").show();
                },
                502: function (xhr) {
                    $("#errorText").text(xhr.responseJSON.error);
                    $("#alertDiv").show();
                }
            }
        });
    }
}

$(document).ready(async function () {
    $("#messageDiv").hide();
    $("#alertDiv").hide();
    await loadBookList();

    $.ajax({
        url: 'http://localhost:8080/login',
        type: 'POST',
        xhrFields: {
            withCredentials: true
        },
        crossDomain: true,
        success: function (result) {

            var user_role = result.Role[0].authority;
            if (user_role == "USER") {
                $('#navigation').children().each(function () {
                    if ($(this).attr('id') != "home") {
                        $(this).remove();
                    }
                });
                $('.UpdateBookBtn').hide();
                $('.DeleteBookBtn').hide();
            }
        }
    });

    $(document).on("click", ".LeaseBookBtn", async function (event) {
        await leaseBookRequest(event);
    });

    $(document).on("click", ".UpdateBookBtn", function (event) {
        updateBookCount(event);
    });

    $(document).on("click", ".DeleteBookBtn", async function (event) {
        if (confirm("Искате ли да изтриете тази книга?")) {
            await deleteBookRequest(event);
        }
    });

    $("#search-button").click(async function () {
        if (! await findBook()) {
            await findUser();
        }
    });

    $(".btn-close").click(function () {
        $("#messageDiv").hide();
        $("#errorText").hide();
    });
});