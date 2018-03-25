$( document ).ready(function() {
    var logo = $(".user-logo");
    var footer = $("#footer");

    function isLogoImg(logodiv){
        var img = new Image();
        img = logodiv.find(".bg");
        img.error(function(){
            logodiv.remove();
        });
        img.load(function(){
            logodiv.find(".title-logo").remove();
        });
    }
    function isFooterContent(footerdiv){
        var el = footerdiv.html();
        var notFoundPage = footerdiv.find("title").html();
        if ((el == "") || (notFoundPage == "404 Not Found")) {
            footerdiv.remove();
        }
    }
    isLogoImg(logo);
    isFooterContent(footer);
});

