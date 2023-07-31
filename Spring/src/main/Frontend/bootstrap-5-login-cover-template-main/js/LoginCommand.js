function getBasicAuthString (sUsername, sPassword) {
    return 'Basic ' + btoa([sUsername, sPassword].join(':'));
}
$(document).ready(function(){
    $("#errorMsg").hide();
    $("button").click(function(){
        let username = $("#usernameField").val();
        let password = $("#passwordField").val();
        if ((username == "" || username == undefined) || (password == "" || password == undefined)) {
            console.log("Everything hidden");
            $("#errorMsg").text("Please enter text into marked fields");
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
                window.location.replace("http://localhost/library-frontend/bootstrap-5-categories-template-main/categories-template.html");

                
                // Do something with the result
            },
            error: function(oJqXhr) {
                if(oJqXhr.status != 401) {
                    console.log("Status is " + oJqXhr.status)
                    // network error or http status different than 401 Unauthorized
                    $("#errorMsg").text("Connection error");
                    $("#errorMsg").show();
                } else {
                    $("#errorMsg").text("Wrong username or password");
                    $("#errorMsg").show();
                }
            }
        });

    });
  });