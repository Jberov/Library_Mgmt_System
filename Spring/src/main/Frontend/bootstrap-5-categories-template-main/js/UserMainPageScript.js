async function fetchUsers(){
    return books = await $.ajax({
        url: 'http://localhost:8080/api/v1/users/info',
        type: 'GET',
        xhrFields: {
            withCredentials: true            
        },
        async:true
    });
}

async function deleteUserRequest(event){
    let urlString = 'http://localhost:8080/api/v1/users/' + $(event.target).siblings("h5").text();
    return books = await $.ajax({
        url: urlString,
        type: 'DELETE',
        xhrFields: {
            withCredentials: true            
        }
    });
}

async function deleteUser(event){
    let userResponse = await deleteUserRequest(event);

    switch (userResponse.status) { 
        case 404:
            alert("Няма такъв потребител");
            return false;
        case 409:
            alert(userResponse.error);
            return false;
        case 500:
            alert("Проблем в системата");
            return false;
        case 502:
            alert("Няма връзка със системата");
            return false;       
    };

    alert("Потребителят е изтрит");
    location.reload();
}

async function loadUserList(){
    let userResponse = await fetchUsers();
    if (userResponse != null) {
        let userList = userResponse;
        userList.forEach(user => {
            $("#userSection").append(
                '<div class="row bookList">' +
            '            <div class="col-md-4 mb-4">' +
            '              <div class="bg-image hover-overlay shadow-1-strong rounded ripple" data-mdb-ripple-color="light">' +
            '                <img src="./img/userAvatar.png" class="img-fluid" />' +
            '                <a href="UserInfo.html?user=' + user.username + '">' +
            '                  <div class="mask" style="background-color: rgba(251, 251, 251, 0.15);"></div>' +
            '                </a>' +
            '              </div>' +
            '            </div>' +
            '            <div class="col-md-8 mb-4 singleUser">' +
            '              <h5 class="UsernameHeading">' + user.username + '</h5>' +
            '              <p> Email: ' + user.email +
            '              </p>' +
            '              <button type="button" class="btn btn-primary ChangeUserBtn">Промени</button>' +
            '              <button type="button" class="btn btn-danger DeleteUserBtn">Премахни</button>' +
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

$(document).ready(async function(){
   await loadUserList();
    $(document).on("click",".ChangeUserBtn",function(event){
       const searchable = $(event.target).siblings("h5").text();
       window.location.replace("http://localhost/library-frontend/bootstrap-5-categories-template-main/AddUserPageAdmin.html?user=" + searchable);
    });

    $(document).on("click",".DeleteUserBtn",function(event){
        if (confirm("Искате ли да изтриете този потребител?")){
            deleteUser(event);
        }

    });

    $("#search-button").click(async function(){
        if(! await findBook()) {
            await findUser();
        }
    });
});
