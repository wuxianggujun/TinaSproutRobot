package annotation.processor;

import annotation.MessageEvent;
import annotation.utils.Logger;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
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
            TypeSpec.Builder typeBuilder = TypeSpec.classBuilder("RegisterEventListener")
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL);
            for (Element element : roundEnv.getElementsAnnotatedWith(MessageEvent.class)) {
                TypeElement targetClass = null;
                if (element.getKind() != ElementKind.CLASS)
                    return true;
                if (element instanceof TypeElement) {
                    targetClass = (TypeElement) element;
                }
                String packageName = elementUtils.getPackageOf(targetClass).getQualifiedName().toString();
                logger.i("包名：" + packageName);
                String typeClassName = targetClass.getSimpleName().toString();
                logger.i(typeClassName);
                ClassName className = ClassName.get(packageName, typeClassName);
                logger.i(className.canonicalName());
                MessageEvent messageEvent = targetClass.getAnnotation(MessageEvent.class);
                JavaFileObject f = null;
                try {
                    f = filer.
                            createSourceFile("com.wuxianggujun.robot.event.RegisterEventListener");
                    try (Writer w = f.openWriter()) {
                        PrintWriter pw = new PrintWriter(w);
                        pw.println("package com.wuxianggujun.robot.event;");
                        pw.println("import com.wuxianggujun.robotcore.listener.MessageEventContext;");
                        pw.printf("import %s;\n", className);
                        pw.println("import org.springframework.stereotype.Repository;");
                        pw.printf("public final class %s{\n", "RegisterEventListener");
                        pw.println("public static void register(){");
                        pw.printf("MessageEventContext.getInstance().addEventListener(\"%s\", new %s());\n", messageEvent.value().getMessageType(), typeClassName);
                        pw.println("    }");
                        pw.println("}");
                        pw.flush();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


//                //实现接口的类名
//                ClassName className = ClassName.get(targetClass);
//                logger.i(className.canonicalName());
//                List<? extends Element> enclosedElements = targetClass.getEnclosedElements();
//                for (Element enclosedElement : enclosedElements) {
//                    logger.i(enclosedElement.getSimpleName());
//                }


                //获取接口
                List<? extends TypeMirror> list = targetClass.getInterfaces();
                for (TypeMirror typeMirror : list) {
                    ClassName typeName = (ClassName) ClassName.get(typeMirror);
                    logger.i(PrivateMessageListener.class.getCanonicalName());
                }

            }


        }
        return false;
    }

    private void generateClass(Element element) throws Exception {
        TypeElement targetClass = null;
        if (element.getKind() != ElementKind.CLASS)
            return;
        if (element instanceof TypeElement) {
            targetClass = (TypeElement) element;
        }
        String packageName = elementUtils.getPackageOf(targetClass).getQualifiedName().toString();
        logger.i("包名：" + packageName);
        String typeClassName = targetClass.getSimpleName().toString();
        logger.i(typeClassName);
        ClassName className = ClassName.get(packageName, typeClassName);

        MethodSpec.Builder addMethod = MethodSpec.methodBuilder("addEventListener")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addStatement("com.wuxianggujun.robotcore.listener.MessageEventContext.getInstance().addEventListener($S, new $T())", "group", className)
                .returns(void.class);

        TypeSpec.Builder classBuilder = TypeSpec.classBuilder("RegisterEventListener")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)

                .addMethod(addMethod.build());

        try {
            JavaFile.builder("com.wuxianggujun.robot.event",
                            classBuilder.build())
                    .build()
                    .writeTo(filer);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
//        if (annotations.isEmpty()) return true;
//        for (TypeElement element : annotations) {
//            Set<? extends Element> annotationElements = roundEnv.getElementsAnnotatedWith(MessageEvent.class);
//            TypeElement typeElement = null;
//            // 检查一个类是否被 @MessageEvent 注释
//            for (Element annotationElement : annotationElements) {
//                if (annotationElement.getKind() != ElementKind.CLASS) {
//                    logger.e(annotationElement, "Only classes can be annotated with @%s", MessageEvent.class.getSimpleName());
//                    return true;//Exit processing
//                }
//                if (annotationElement instanceof TypeElement) {
//                    typeElement = (TypeElement) annotationElement;
//                }
//
//                //获取的是包
//                PackageElement packageElement = (PackageElement) typeElement.getEnclosingElement();
//                logger.i(packageElement.getQualifiedName());
//                MessageEventClass messageEventClass = new MessageEventClass(typeElement);
//                if (!isValidClass(messageEventClass)) {
//                    return true;
//                }
//
//                TypeMirror typeMirror = annotationElement.asType();
//
//                logger.i(typeElement.getQualifiedName());
//
//
//            }
//
//
//        }

//    public void generateCode() {
//        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("create" + key.simpleName())
//                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
//                .addParameter(String.class, "type")
//                .beginControlFlow("switch(type)");
//        for (ElementInfo elementInfo : input.get(key)) {
//            methodBuilder
//                    .addStatement("case $S: return new $T()", elementInfo.tag, elementInfo.className);
//        }
//
//        methodBuilder
//                .endControlFlow()
//                .addStatement("throw new RuntimeException(\"not support type\")")
//                .returns(void.class);
//        MethodSpec methodSpec = methodBuilder.build();
//
//
//        TypeSpec helloWorld = TypeSpec.classBuilder(key.simpleName() + "Factory")
//                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
//                .addMethod(methodSpec)
//                .build();
//        JavaFile javaFile = JavaFile.builder("com.wuxianggujun,robot.event", helloWorld)
//                .build();
//
//        javaFile.writeTo(filer);
//    }

    private @Nullable TypeElement getSuperClass(TypeElement typeElement) {
        TypeMirror typeMirror = typeElement.getSuperclass();
        if (typeMirror.getKind() == TypeKind.NONE) {
            return null;
        }
        return (TypeElement) ((DeclaredType) typeMirror).asElement();
    }

    private void writeBuilderFile(
            String className, Map<String, String> setterMap)
            throws IOException {

        String packageName = null;
        int lastDot = className.lastIndexOf('.');
        if (lastDot > 0) {
            packageName = className.substring(0, lastDot);
        }

        String simpleClassName = className.substring(lastDot + 1);
        String builderClassName = className + "Builder";
        String builderSimpleClassName = builderClassName
                .substring(lastDot + 1);

        JavaFileObject builderFile = processingEnv.getFiler()
                .createSourceFile(builderClassName);

        try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {

            if (packageName != null) {
                out.print("package ");
                out.print(packageName);
                out.println(";");
                out.println();
            }

            out.print("public class ");
            out.print(builderSimpleClassName);
            out.println(" {");
            out.println();

            out.print("    private ");
            out.print(simpleClassName);
            out.print(" object = new ");
            out.print(simpleClassName);
            out.println("();");
            out.println();

            out.print("    public ");
            out.print(simpleClassName);
            out.println(" build() {");
            out.println("        return object;");
            out.println("    }");
            out.println();

            setterMap.entrySet().forEach(setter -> {
                String methodName = setter.getKey();
                String argumentType = setter.getValue();

                out.print("    public ");
                out.print(builderSimpleClassName);
                out.print(" ");
                out.print(methodName);

                out.print("(");

                out.print(argumentType);
                out.println(" value) {");
                out.print("        object.");
                out.print(methodName);
                out.println("(value);");
                out.println("        return this;");
                out.println("    }");
                out.println();
            });

            out.println("}");
        }
    }

    private void verifyNotAnAnnotation(TypeElement typeElement) {
        if (typeElement.getKind() == ElementKind.ANNOTATION_TYPE) {

        }
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
