

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
        }
    });
}

function createString(statData, base) {
    let i = 1;
    console.log(base);

    $.each(statData, function(key, value){

        base += '\n' + i + ". " + key + ", прочетен " + value + " път/и;" + '\n';
        i++;
    });

    return base;
}

async function fillOutStatistics(date=null){
    let statData, payload;

    if (date === null) {
        payload = "";
    } else{
        payload = date;
    }
    
    statData = await getBookReadCount(payload);

    $("#countOfReadings").text("Брой прочитания: " + statData.countOfReadings);

    statData = await getMostReadByKey(date, 'genre');

    console.log(statData);

    let compexString = "Най-четени жанрове са:\n";

    $("#mostReadGenre").text(createString(statData, compexString));

    statData = await getMostReadByKey(date, 'author');

    compexString = "Най-четени автори са:\n";

    $("#mostReadAuthor").text(createString(statData, compexString));

    statData = await getMostReadByKey(date);

    compexString = "Най-четени книги са:\n";

    $("#mostReadBooks").text(createString(statData, compexString));
}

$(document).ready(async function(){
    $(".errorMsg").hide();
    fillOutStatistics();

    $("#dateSearchBtn").click(function(){
        const date = $("#date").text();
        if (validateDate(date)) {
            fillOutStatistics(date);
            return;
        }
        alert("Невалидна дата");
        location.reload();

    });

    $("#search-button").click(async function(){
        if(! await findBook()) {
            findUser();
        }
    });
});