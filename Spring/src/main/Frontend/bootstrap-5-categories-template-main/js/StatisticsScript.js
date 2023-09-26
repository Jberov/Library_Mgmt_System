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
        },
        statusCode: {
            500: function () {
                $("#errorText").text("Системата е временно недостъпна");
                $("#alertDiv").show();
            }
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
       },
       statusCode: {
           500: function () {
               $("#errorText").text("Системата е временно недостъпна");
               $("#alertDiv").show();
           }
       }
   });
}

async function findUser(){
   const searchable = $("#searchValue").val();
   try {
       await fetchUser(searchable);
       window.location.replace("http://localhost/library-frontend/bootstrap-5-categories-template-main/UserInfo.html?user=" + searchable);
   } catch (error) {
        $("#errorText").text("Няма потребител или книга с такова име");
        $("#alertDiv").show();
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

async function getBookReadCount(payload) {
    return await $.ajax({
        url: 'http://localhost:8080/api/v1/statistics/countOfBooks',
        type: 'GET',
        data: payload,
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        async: true,
        xhrFields: {
            withCredentials: true            
        },
        statusCode: {
            500: function () {
                $("#errorText").text("Системата е временно недостъпна");
                $("#alertDiv").show();
            }
        }
    });
}

async function getMostReadByKey(payload, header="") {
    return await $.ajax({
        url: 'http://localhost:8080/api/v1/statistics/mostPopularBooks',
        type: 'GET',
        data: payload,
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        async: true,
        xhrFields: {
            withCredentials: true            
        },
        beforeSend: function(oJqXhr) {
            oJqXhr.setRequestHeader('Criteria', header);
        },
        statusCode: {
            500: function () {
                $("#errorText").text("Системата е временно недостъпна");
                $("#alertDiv").show();
            }
        }
    });
}

function createString(statData, base) {
    let i = 1;
    $.each(statData, function(key, value){

        base += '\n' + i + ". " + key + ", прочетен " + value + " път/и;" + '\n';
        i++;
    });
    return base;
}

async function fillOutStatistics(date){
    let statData, payload;

    if (date == null) {
        payload = "";
    } else{
        payload = date;
    }
    
    statData = await getBookReadCount(payload);

    $("#countOfReadings").text("Брой прочитания: " + statData.countOfReadings);

    statData = await getMostReadByKey(payload, 'genre');

    let compexString = "Най-четени жанрове са:\n";

    $("#mostReadGenre").text(createString(statData, compexString));

    statData = await getMostReadByKey(payload, 'author');

    compexString = "Най-четени автори са:\n";

    $("#mostReadAuthor").text(createString(statData, compexString));

    statData = await getMostReadByKey(payload);

    compexString = "Най-четени книги са:\n";

    $("#mostReadBooks").text(createString(statData, compexString));
}

function validateDate(date){
    const regex = /^[0-9]{4}-[0-9]{2}-[0-9]{2}$/;
    return regex.test(date);
}

$(document).ready(async function(){
    $("#messageDiv").hide();
    $("#alertDiv").hide();
    $(".errorMsg").hide();
    fillOutStatistics();

    $("#dateSearchBtn").click(function(){
        const date = $("#date").val();
        console.log(date);
        if (validateDate(date)) {
            fillOutStatistics(date);
            return;
        }

        location.reload();
        $("#errorText").text("Невалидна дата");
        $("#alertDiv").show();
    });

    $("#search-button").click(async function(){
        if(! await findBook()) {
            findUser();
        }
    });

    $(".btn-close").click(function () {
        $("#messageDiv").hide();
        $("#alertDiv").hide();
    });
});