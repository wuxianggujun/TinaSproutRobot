package annotation.processor;

import annotation.MessageEvent;
import annotation.utils.Logger;
import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
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
        if (annotations.isEmpty()) return true;
        for (TypeElement element : annotations) {
            Set<? extends Element> annotationElements = roundEnv.getElementsAnnotatedWith(MessageEvent.class);
            TypeElement typeElement = null;
            // 检查一个类是否被 @MessageEvent 注释
            for (Element annotationElement : annotationElements) {
                if (annotationElement.getKind() != ElementKind.CLASS) {
                    logger.e(annotationElement, "Only classes can be annotated with @%s", MessageEvent.class.getSimpleName());
                    return true;//Exit processing
                }
                if (annotationElement instanceof TypeElement) {
                    typeElement = (TypeElement) annotationElement;
                }

                //获取的是包
                PackageElement packageElement = (PackageElement) typeElement.getEnclosingElement();
                logger.i(packageElement.getQualifiedName());
                MessageEventClass messageEventClass = new MessageEventClass(typeElement);
                if (!isValidClass(messageEventClass)) {
                    return true;
                }

                TypeMirror typeMirror = annotationElement.asType();

                logger.i(typeElement.getQualifiedName());


            }


        }
        return false;
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
