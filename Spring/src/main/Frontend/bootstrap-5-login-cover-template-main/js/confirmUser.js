function register(){
    const urlParam = new URLSearchParams(window.location.search).get("token");
    $.ajax({
        url: 'http://localhost:8080/registrationConfirm?token=' + urlParam,
        type: 'POST',
        async: true,
        success: function() {
            window.location.replace("http://localhost/library-frontend/bootstrap-5-login-cover-template-main/index.html");
        },
        error: function(xhr) {
            alert(xhr.response);
        }
    });
}
$(document).ready(function(){
    $("a").click(function(){
        register();
    });
    register();
});