window.onload = function() {

    /* call warp's normal loadup, so other services continue to work */
    window.warpOnload();

    document.getElementById('nameInput').onkeyup=function() {
        var nBox = dwr.util.getValue("nameInput");

        AjaxDemo.remoteMethod(nBox,
                function(name) { dwr.util.setValue("reply", name); }
        );
    };
};