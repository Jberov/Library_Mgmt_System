async function register(){
    const urlParam = new URLSearchParams(window.location.search).get("token");
    console.log(urlParam);

    console.log('http://localhost:8080/registrationConfirm?token=' + urlParam);

    await $.ajax({
        url: 'http://localhost:8080/registrationConfirm?token=' + urlParam,
        type: 'POST',
        async: true,
        success: function() {
            window.location.replace("http://localhost/library-frontend/bootstrap-5-login-cover-template-main/index.html");
        },
        error: function(xhr) {
            alert(xhr);
        }
    });
}

$(document).ready(function(){
    $("a").click(async function(){
        await register();
    });
});