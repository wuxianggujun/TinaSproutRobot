package annotation.processor;

import annotation.MessageEvent;
import annotation.enums.MessageEventType;
import annotation.utils.Logger;
import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
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
            // Check if a class has been annotated with @MessageEvent
            for (Element annotationElement : annotationElements) {
                if (annotationElement.getKind() != ElementKind.CLASS) {
                    logger.e(annotationElement, "Only classes can be annotated with @%s", MessageEvent.class.getSimpleName());
                    return true;//Exit processing
                }
                MessageEvent messageEvent = annotationElement.getAnnotation(MessageEvent.class);
                if (messageEvent != null) {
                    if (messageEvent.value().equals(MessageEventType.GROUP) || messageEvent.value().equals(MessageEventType.PRIVATE)) {
                        LinkedHashSet<Element> groupList = annotationClass.get(messageEvent.value().getMessageType());
                        if (groupList == null) {
                            groupList = new LinkedHashSet<Element>();
                        }
                        groupList.add(annotationElement);
                        logger.i(Arrays.toString(groupList.toArray()));
                        annotationClass.put(messageEvent.value().getMessageType(), groupList);
                    }
                }
                logger.i(annotationElement.getSimpleName());
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
