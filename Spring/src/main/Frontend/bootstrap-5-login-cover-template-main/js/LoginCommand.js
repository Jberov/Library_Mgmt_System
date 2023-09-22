function getBasicAuthString (sUsername, sPassword) {
    return 'Basic ' + btoa([sUsername, sPassword].join(':'));
}
$(document).ready(function(){
    $("#errorMsg").hide();
    $("button").click(function(){
        let username = $("#usernameField").val();
        let password = $("#passwordField").val();
        if ((username == "" || username == undefined) || (password == "" || password == undefined)) {
            $("#errorMsg").text("Моля въведете парола и/или потребителско име");
            $("#errorMsg").show();
            return;
        }

        $.ajax({
            url: 'http://localhost:8080/login',
            type: 'POST',
            xhrFields: {
                withCredentials: true            
            },beforeSend: function(oJqXhr) {
                oJqXhr.setRequestHeader('Authorization', getBasicAuthString(username, password));
            },
            async: true,
            success: function(result) {
                $("#errorMsg").hide();
                window.location.replace("http://localhost/library-frontend/bootstrap-5-categories-template-main/MainPage.html");

                
                // Do something with the result
            },
            error: function(oJqXhr) {
                if(oJqXhr.status != 401) {
                    console.log("Status is " + oJqXhr.status);
                    // network error or http status different than 401 Unauthorized
                    $("#errorMsg").text("Проблем при връзката");
                    $("#errorMsg").show();
                } else {
                    $("#errorMsg").text("Грешно потребителско име или парола");
                    $("#errorMsg").show();
                }
            }
        });
    });
  });
