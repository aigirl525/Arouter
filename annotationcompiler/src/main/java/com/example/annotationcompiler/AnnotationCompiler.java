package com.example.annotationcompiler;



import com.example.annotation.Path;
import com.google.auto.service.AutoService;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

@AutoService(Processor.class)
public class AnnotationCompiler extends AbstractProcessor {

    //生成文件对象
    Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
    }
    //声明返回要处理哪个注解
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new HashSet<>();
        types.add(Path.class.getCanonicalName());
        return types;

    }
    //支持java版本
    @Override
    public SourceVersion getSupportedSourceVersion() {
      return processingEnv.getSourceVersion();
    }

    //注解处理器的核心
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        //拿到该模块所有path注解的节点
        Set<? extends Element> elementsAnnotatedWith = roundEnvironment.getElementsAnnotatedWith(Path.class);
        //结构化数据
        Map<String,String> map = new HashMap<>();
        for (Element element : elementsAnnotatedWith){
            //实际就是类节点
            TypeElement typeElement = (TypeElement) element;
            Path annotation = typeElement.getAnnotation(Path.class);
            //读取到key
            String key = annotation.value();
            //包名 + 类名
            String activityName = typeElement.getQualifiedName().toString();
            map.put(key,activityName);
        }
        if (map.size() > 0){
            //开始写文件
            Writer writer = null;
            String utilsName = "ActivityUtils";
            try{
                JavaFileObject javaFileObject = filer.createSourceFile("com.example.utils." + utilsName);
                writer = javaFileObject.openWriter();
                writer.write("package com.example.utils;\n" +
                        "\n"
                        + "import com.example.arouter.ARouter;\n"
                        + "import com.example.arouter.IRoute;\n"
                        + "\n"
                        + "public class " + utilsName + " implements IRoute {\n"
                        + "\n" +
                        " @Override\n" +
                        " public void putActivity() {"
                        + "\n");
                Iterator<String> iterator = map.keySet().iterator();
                while (iterator.hasNext()){
                    String path = iterator.next();
                    String value = map.get(path);
                    writer.write("ARouter.getInstance().putActivity(\"" + path + "\","
                            + value + ".class);\n");
                }
                writer.write("}\n}");
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if (writer != null){
                    try {
                        writer.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }
}
