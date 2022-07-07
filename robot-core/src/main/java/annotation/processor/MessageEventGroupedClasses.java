package annotation.processor;

import javax.annotation.processing.Filer;
import javax.lang.model.util.Elements;
import java.util.LinkedHashMap;
import java.util.Map;

public class MessageEventGroupedClasses {
    private Map<String, MessageEventClass> annotationClass = new LinkedHashMap<>();


    public void add(MessageEventClass toInsert) {
        MessageEventClass existing = annotationClass.get(toInsert.getMessageType());
        annotationClass.put(toInsert.getMessageType(), toInsert);
    }

    public void generateCode(Elements elementUtils, Filer filer) {

    }

}
