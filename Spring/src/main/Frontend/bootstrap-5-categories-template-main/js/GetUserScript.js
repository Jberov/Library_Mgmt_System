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
    let url = 'http://localhost:8080/api/v1/users/info/single';
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
        return false;
    }
    return true;
}

function showUserHistory(user){
    const history = user.userHistory;
    if(history != null) {
        $("table").show();
        let bookArray = history.Taken;
        bookArray.forEach(element => {
            const propArray = element.split(', ');
            $("#tableBody").append(
                "<tr>"
                + '<td class="nameCol">' + propArray[0] + "</td>"
                + "<td>" + propArray[1] + "</td>"
                + "<td>" + "Заета" + "</td>"
                + "<td>" + propArray[2] + "</td>"
                + "<td>" + propArray[3] + "</td>"
                + "<td>" + '<button type="button" class="btn btn-primary ReturnBook">Върни</button>' + "</td>"
                + "</tr>"
            )
        });
        bookArray = history.Returned;
        bookArray.forEach(element => {
            const propArray = element.split(', ');
            $("#tableBody").append(
                "<tr>"
                + '<td class="nameCol">' + propArray[0] + "</td>"
                + "<td>" + propArray[1] + "</td>"
                + "<td>" + "Върната" + "</td>"
                + "<td>" + propArray[2] + "</td>"
                + "<td>" + propArray[3] + "</td>"
                + "<td>" + '<button type="button" class="btn btn-primary LeaseBook">Заеми</button>' + "</td>"
                + "</tr>"
            )
        });
    }
}

async function fetchBookIsbnRequest(book){
    let urlString = 'http://localhost:8080/api/v1/books/' + book;
    console.log(urlString);
    const books = await $.ajax({
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

async function leaseBook(event){
        const book = $(event.target).closest('tr').find('.nameCol').text();
        console.log(book);

        const isbn = await fetchBookIsbnRequest(book);
        const url = 'http://localhost:8080/api/v1/books/rental/' + isbn;
        try {
            $.ajax({
                url: url,
                type: 'PATCH',
                async: true,
                xhrFields: {
                    withCredentials: true            
                },
                success: function(){
                    alert("Книгата е успешно върната");
                },
                error: function(error){
                    alert("Проблем при връщането на книга, поради" + error.message);
                }
            });
        } catch (Error){
            alert("Проблем при връщането на книга");
        }
}

$(document).ready(async function(){
    $("table").hide();
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
        showUserHistory(user);
    }

    $("#search-button").click(async function(){
        if(! await findBook()) {
            findUser();
        }
    });

    $(document).on("click",".LeaseBook", async function(event){
        const book = $(event.target).closest('tr').find('.nameCol').text();
        console.log(book);

        const isbn = await fetchBookIsbnRequest(book);

       
        const url = 'http://localhost:8080/api/v1/books/rental/' + isbn;

        console.log(url);
        try {
            await $.ajax({
                url: url,
                type: 'PATCH',
                async: true,
                xhrFields: {
                    withCredentials: true            
                },
                success: function(){
                    alert("Книгата е успешно заета");
                },
                error: function(error){
                    alert("Проблем при заемането на книга, поради" + error.message);
                }
            });
            location.reload();
        } catch (Error){
            alert("Проблем при заемането на книга");
        }
        
    });

    $(document).on("click",".ReturnBook",async function(event){
        const book = $(event.target).closest('tr').find('.nameCol').text();
        console.log(book);

        const isbn = await fetchBookIsbnRequest(book);

       
        const url = 'http://localhost:8080/api/v1/books/reconveyance/' + isbn;

        console.log(url);
        try {
            await $.ajax({
                url: url,
                type: 'PATCH',
                async: true,
                xhrFields: {
                    withCredentials: true            
                },
                success: function(){
                    alert("Книгата е успешно върната");
                },
                error: function(error){
                    alert("Проблем при връщането на книга, поради" + error.message);
                }
            });
            location.reload();
        } catch (Error){
            alert("Проблем при връщането на книга");
        }
        
    });
});
