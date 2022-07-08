package annotation.processor;

import annotation.MessageEvent;
import annotation.enums.MessageEventType;
import annotation.utils.Logger;
import com.google.auto.service.AutoService;
import com.wuxianggujun.robotcore.listener.impl.GroupMessageListener;
import com.wuxianggujun.robotcore.listener.impl.PrivateMessageListener;

import javax.annotation.Nullable;
import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 使用APT技术处理MessageEvent注解，减少反射使用提高框架效率
 */

@SupportedAnnotationTypes({"com.wuxianggujun.annotation.MessageEvent"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class MessageEventProcessor extends AbstractProcessor {

    private Filer filer;
    private Logger logger;

    private Types typeUtils;

    private Elements elementUtils;

    private Map<String, LinkedHashSet<Element>> annotationClass = new LinkedHashMap<>();


    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        logger = new Logger(processingEnv.getMessager());
        filer = processingEnv.getFiler();
        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        logger.i("Init ......");
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement typeElement : annotations) {
            for (Element element : roundEnv.getElementsAnnotatedWith(MessageEvent.class)) {
                TypeElement targetClass = null;
                if (element.getKind() != ElementKind.CLASS)
                    return true;
                if (element instanceof TypeElement) {
                    targetClass = (TypeElement) element;
                    //获取接口
                    List<? extends TypeMirror> list = targetClass.getInterfaces();
                    for (TypeMirror typeMirror : list) {
                        MessageEvent messageEvent = targetClass.getAnnotation(MessageEvent.class);
                        if (messageEvent != null) {
                            String messageType = messageEvent.value().getMessageType();
                            String typeMirrorClassName = typeMirror.toString();
                            //获取注解的值先判断值属不属于类型里面的
                            if (!MessageEventType.contains(messageType)) {
                                logger.e("注解的值无效:" + messageType);
                            }
                            if (typeMirrorClassName.equals(PrivateMessageListener.class.getCanonicalName()) && messageType.equals(MessageEventType.PRIVATE.getMessageType())) {
                                LinkedHashSet<Element> elements = annotationClass.get(messageType);
                                if (elements == null) {
                                    elements = new LinkedHashSet<>();
                                }
                                elements.add(targetClass);
                                annotationClass.put(messageType, elements);
                                logger.i("Private:" + typeMirror.toString());
                            } else if (typeMirrorClassName.equals(GroupMessageListener.class.getCanonicalName()) && messageType.equals(MessageEventType.GROUP.getMessageType())) {
                                LinkedHashSet<Element> elements = annotationClass.get(messageType);
                                if (elements == null) {
                                    elements = new LinkedHashSet<>();
                                }
                                elements.add(targetClass);
                                annotationClass.put(messageType, elements);
                                logger.i("Group：" + typeMirror.toString());
                            }
                        }
                    }
                }

            }
            generateClass();
        }
        return false;
    }

    private void generateClass() {

        JavaFileObject f = null;
        try {
            f = filer.createSourceFile("com.wuxianggujun.robot.event.RegisterEventListener");
            try (Writer w = f.openWriter()) {
                PrintWriter pw = new PrintWriter(w);
                pw.println("package com.wuxianggujun.robot.event;");
                pw.println("import com.wuxianggujun.robotcore.listener.MessageEventContext;");

                // 使用For-Each迭代entries，通过Map.entrySet遍历key和value
                for (Map.Entry<String, LinkedHashSet<Element>> entry : annotationClass.entrySet()) {
                    LinkedHashSet<Element> elements = entry.getValue();
                    for (Element element : elements) {
                        TypeElement targetClass = (TypeElement) element;
                        String packageName = elementUtils.getPackageOf(targetClass).getQualifiedName().toString();
                        String typeClassName = targetClass.getSimpleName().toString();
                        pw.printf("import %s;\n", packageName + "." + typeClassName);
                    }
                    logger.i("Generatingkey = " + entry.getKey() + ", value = " + entry.getValue());
                }
                pw.println();
                pw.println("/**");
                pw.println(" * 这个是使用apt生成的类，");
                pw.println(" * 在编译时找到全部的被注解的类);");
                pw.println(" * 然后添加到register方法里面");
                pw.println(" * @author 无相孤君 2022-7-8 10:00");
                pw.println(" * 还不如反射好写，他奶奶滴");
                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss");
                LocalDateTime localDateTime = LocalDateTime.now();
                String date = df.format(localDateTime);
                pw.printf(" * @date %s\n", date);
                pw.println(" */");
                pw.println();
                pw.printf("public final class %s{\n", "RegisterEventListener");
                pw.println("    public static void register() {");
                // 使用For-Each迭代entries，通过Map.entrySet遍历key和value
                for (Map.Entry<String, LinkedHashSet<Element>> entry : annotationClass.entrySet()) {
                    LinkedHashSet<Element> elements = entry.getValue();
                    for (Element element : elements) {
                        TypeElement targetClass = (TypeElement) element;
                        String typeClassName = targetClass.getSimpleName().toString();
                        pw.printf("        MessageEventContext.getInstance().addEventListener(\"%s\", new %s());\n", entry.getKey(), typeClassName);
                    }
                }
                pw.println("    }");
                pw.println("}");
                pw.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private @Nullable TypeElement getSuperClass(TypeElement typeElement) {
        TypeMirror typeMirror = typeElement.getSuperclass();
        if (typeMirror.getKind() == TypeKind.NONE) {
            return null;
        }
        return (TypeElement) ((DeclaredType) typeMirror).asElement();
    }


    private boolean isValidClass(MessageEventClass item) {
        TypeElement classTypeElement = item.getTypeElement();
        // // Cast to TypeElement, has more type specific methods
        if (!classTypeElement.getModifiers().contains(Modifier.PUBLIC)) {
            logger.e(classTypeElement, "The class %s is not public.", classTypeElement.getQualifiedName().toString());
            return false;
        }
        // Check if it's an abstract class
        if (classTypeElement.getModifiers().contains(Modifier.ABSTRACT)) {
            logger.e(classTypeElement, "The class %s is abstract. You can't annotate abstract classes with @%",
                    classTypeElement.getQualifiedName().toString(), MessageEvent.class.getSimpleName());
            return false;
        }
        TypeElement superClassElement = elementUtils.getTypeElement(item.getQualifiedSuperClassName());
        if (superClassElement.getKind() == ElementKind.INTERFACE) {
            //检测有没有实现接口
            if (!classTypeElement.getInterfaces().contains(superClassElement.asType())) {
                logger.e(classTypeElement, "The class %s annotated with @%s must implement the interface %s",
                        classTypeElement.getQualifiedName().toString(), MessageEvent.class.getSimpleName(),
                        item.getQualifiedSuperClassName());
                return false;
            }
        }
//     else {
//            // Check subclassing
//            TypeElement currentClass = classTypeElement;
//            while (true) {
//                TypeMirror superClassType = currentClass.getSuperclass();
//
//                if (superClassType.getKind() == TypeKind.NONE) {
//                    // Basis class (java.lang.Object) reached, so exit
//                    logger.e(classTypeElement, "The class %s annotated with @%s must inherit from %s",
//                            classTypeElement.getQualifiedName().toString(), MessageEvent.class.getSimpleName(),
//                            item.getQualifiedSuperClassName());
//                    return false;
//                }
//
//                if (superClassType.toString().equals(item.getQualifiedSuperClassName())) {
//                    // Required super class found
//                    break;
//                }
//
//                // Moving up in inheritance tree
//                currentClass = (TypeElement) typeUtils.asElement(superClassType);
//            }
//        }
        // 检查是否给出了一个空的公共构造函数
        for (Element enclosed : classTypeElement.getEnclosedElements()) {
            if (enclosed.getKind() == ElementKind.CONSTRUCTOR) {
                ExecutableElement constructorElement = (ExecutableElement) enclosed;
                if (constructorElement.getParameters().size() == 0 && constructorElement.getModifiers()
                        .contains(Modifier.PUBLIC)) {
                    // Found an empty constructor
                    return true;
                }
            }
        }
        // No empty constructor found
        logger.e(classTypeElement, "The class %s must provide an public empty default constructor",
                classTypeElement.getQualifiedName().toString());
        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        // 在此处声明该processor支持的注解类型
        // 和注解@SupportedAnnotationTypes功能相同
        Set<String> set = new HashSet<>();
        set.add(MessageEvent.class.getCanonicalName());
        return set;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
