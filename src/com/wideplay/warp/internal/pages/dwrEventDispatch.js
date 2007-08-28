document.getElementById("%s").onclick=function(){
    var tmpParamMap = {};
    var tmpViewport = document.getElementById("%s");
    tmpParamMap["%s"] = tmpViewport.targetUri; /* put target uri */
    tmpParamMap["%s"] = "%s";   /* put event name*/
    tmpParamMap["%s"] = "%d";   /* put event topic*/

    /*for each viewport, get the bindings and build a map to pass to the remote function*/
    for (var bindingIndex in tmpViewport.bindings) {
        var binding = tmpViewport.bindings[bindingIndex];
        tmpParamMap[document.getElementById(binding).name] = dwr.util.getValue(binding);
    }

    RemoteEventProxy.dispatchEvent(tmpParamMap,
            function(reply) { dwr.util.setValue("%s", reply, { escapeHtml:false }); }
        );
};