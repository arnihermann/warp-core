function fnPublishPageEvent(e, eventAnnotation) {
    YAHOO.util.Event.stopEvent(e);
    __warpForm.w_event.value = eventAnnotation;
    __warpForm.submit();
}