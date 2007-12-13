document.getElementById("%s").onclick=function(){ __warpForm.w_event.value= "%s"

        if (0 != topicId) {
            onFrameLoadWriter.append("\"; __warpForm.w_event_topic.value= \"");
            onFrameLoadWriter.append(topicId);
        }
        onFrameLoadWriter.append("\"; __warpForm.submit(); return false;}; ");