package link.tanxin.common.beanFilter;

import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;

/**
 * kafkaBean过滤器
 * 用于过滤kafka配置
 *
 * @author tan
 */
public class KafkaFilter implements TypeFilter {

    private static final String FILTER_NAME = "kafka";

    @Override
    public boolean match(MetadataReader metadataReader,
                         MetadataReaderFactory metadataReaderFactory) throws IOException {
//获取当前类注解的信息
        AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
        //获取当前正在扫描的类信息
        ClassMetadata classMetadata = metadataReader.getClassMetadata();
        //获取当前类资源（类的路径）
//        Resource resource = metadataReader.getResource();
        String className = classMetadata.getClassName();
        if (className.contains(FILTER_NAME)) {
            return false;
        }
        return true;
    }
}
