package annotation.processor;

import annotation.MessageEvent;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;

public class MessageEventClass {
    private TypeElement typeElement;
    private String qualifiedSuperClassName;
    private String simpleTypeName;
    private String messageType;


    public MessageEventClass(TypeElement typeElement) {
        this.typeElement = typeElement;
        MessageEvent messageEvent = typeElement.getAnnotation(MessageEvent.class);
        messageType = messageEvent.value().getMessageType();
        if (messageType == null) {
            throw new IllegalArgumentException(
                    String.format("value() in @%s for class %s is null or empty! that's not allowed",
                            MessageEvent.class.getSimpleName(), typeElement.getQualifiedName().toString()));
        }
        try {
            Class<?> clazz = messageEvent.annotationType();
            qualifiedSuperClassName = clazz.getCanonicalName();
            simpleTypeName = clazz.getSimpleName();
        } catch (MirroredTypeException mte) {
            DeclaredType declaredType = (DeclaredType) mte.getTypeMirror();
            TypeElement classTypeElement = (TypeElement) declaredType.asElement();
            qualifiedSuperClassName = classTypeElement.getQualifiedName().toString();
            simpleTypeName = classTypeElement.getSimpleName().toString();
        }
    }

    public TypeElement getTypeElement() {
        return typeElement;
    }

    public String getQualifiedSuperClassName() {
        return qualifiedSuperClassName;
    }

    public String getSimpleTypeName() {
        return simpleTypeName;
    }

    public String getMessageType() {
        return messageType;
    }
}
