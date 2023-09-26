function validateInput() {
    let valid = true;
    $("#allInput").children(".input-group, .textarea-input").each(function () {
        let textInput = $(this).find("input, textarea").val();
        if (textInput == null || textInput == "") {
            $(this).find(".errorMsg").show();
            valid = false;
        }
    });

    if (!valid) {
        return false;
    }

    let isbn = $("#isbn").val();
    let isbnRegex = /([97(8|9)]{3}[-][0-9]{1,5}[-][0-9]{0,7}[-][0-9]{0,6}[-][0-9])|([0-9]{13})/

    console.log(isbnRegex.test(isbn));
    if (!isbnRegex.test(isbn)) {
        $("#isbnError").text("Невалиден формат на isbn номер!");
        $("#isbnError").show();
        return false;
    }

    return valid;
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
        $("#alertDiv").show();
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

$(document).ready(async function () {
    $("#alertDiv").hide();
    $("#messageDiv").hide();
    $(".errorMsg").hide();

    $(".btn--radius").click(async function () {
        $(".errorMsg").hide();
        if (validateInput()) {
            await $.ajax({
                url: 'http://localhost:8080/api/v1/books',
                type: 'POST',
                data: createJSONPayload(),
                contentType: 'application/json; charset=utf-8',
                dataType: 'json',
                async: true,
                xhrFields: {
                    withCredentials: true
                },
                statusCode: {
                    201: function (xhr) {
                        $("#messageDiv").text(xhr.message);
                        $("#messageDiv").show();
                    },
                    400: function (xhr) {
                        $("#errorText").text(xhr);
                        $("#alertDiv").show();
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
    });

    $("#search-button").click(async function () {
        if (! await findBook()) {
            findUser();
        }
    });

    $(".btn-close").click(function () {
        $("#messageDiv").hide();
        $("#alertDiv").hide();
    });
})