package annotation.processor;

import annotation.MessageEvent;
import annotation.utils.Logger;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.wuxianggujun.robotcore.listener.MessageListener;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        //获取有注解的元素
        for (Element annotationElement : roundEnv.getElementsAnnotatedWith(MessageEvent.class)) {
            if (annotationElement.getKind() != ElementKind.CLASS) {
                logger.e("Only classes can be annotated with MessageEvent.class");
                return true;
            }
            analysisAnnotation(annotationElement);
        }
        return false;
    }

    private void analysisAnnotation(Element annotationElement) {
        String messageType = annotationElement.getAnnotation(MessageEvent.class).value().getMessageType();
        MethodSpec addEventListenerMethod = MethodSpec.methodBuilder("addEventListener")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .build();

        TypeSpec registerTheEventListener = TypeSpec.classBuilder("RegisterEventListener")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(addEventListenerMethod)
                .build();
        logger.i(messageType);
        JavaFile javaFile = JavaFile.builder("com.wuxianggujun.event", registerTheEventListener)
                .build();

        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

// @Override
//    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
//        if (annotations.isEmpty())
//            return false;
//        for (TypeElement typeElement : annotations) {
//            logger.i("SupportedAnnotationTypes : " + typeElement.getQualifiedName());
//
//            //判断element是view的子类或者接口
//            TypeMirror typeMirror = typeElement.asType();
//            if (typeMirror.getKind() == TypeKind.TYPEVAR){
//                TypeVariable typeVariable = (TypeVariable) typeMirror;
//                typeMirror = typeVariable.getUpperBound();
//
//            }
//            Name qualifiedName = typeElement.getQualifiedName();
//            Name simpleName = typeElement.getSimpleName();
//            logger.i(qualifiedName);
//            logger.i(simpleName);
//
//            // 生成 public static void main(String[] args) 函数
//            MethodSpec main = MethodSpec.methodBuilder("main")
//                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
//                    .returns(void.class)
//                    .addParameter(String[].class, "args")
//                    .addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet!")
//                    .build();
//
//            // 指定 public final class HelloWorld 类
//            TypeSpec registerTheEventListener = TypeSpec.classBuilder("RegisterEventListenerMap")
//                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
//                    .addMethod(main)
//                    .build();
//
//            // 正式在 "com.example.helloworld" 包名下创建 HelloWorld 类
//            JavaFile javaFile = JavaFile.builder("com.wuxianggujun.robotcore.event", registerTheEventListener)
//                    .build();
//
//            try {
//                javaFile.writeTo(filer);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return true;
//    }
//

    private boolean isView(TypeMirror type) {
        List<? extends TypeMirror> supers = typeUtils.directSupertypes(type);
        if (supers.size() == 0) {
            return false;
        }
        for (TypeMirror superType : supers) {
            logger.i(superType.toString());
            if (superType.toString().equals("com.wuxianggujun.robotcore.listener.impl.GroupMessageListener") || isView(superType)) {
                return true;
            }
        }
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
