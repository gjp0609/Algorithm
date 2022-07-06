package fun.onysakura.algorithm.spring.es.mapper;

import fun.onysakura.algorithm.spring.es.entity.Files;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface FilesRepository extends ElasticsearchRepository<Files,String> {
}
