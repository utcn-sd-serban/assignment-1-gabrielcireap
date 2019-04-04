package com.gabrielcireap.stackOverflow.seed;

import com.gabrielcireap.stackOverflow.entity.Tag;
import com.gabrielcireap.stackOverflow.repository.RepositoryFactory;
import com.gabrielcireap.stackOverflow.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "stack_overflow.repository-type", havingValue = "MEMORY")
public class TagSeed implements CommandLineRunner {

    private final RepositoryFactory repositoryFactory;

    @Override
    public void run(String... args) throws Exception {

        System.out.println("Ran tags!");
        TagRepository tagRepository = repositoryFactory.createTagRepository();
        if(tagRepository.findAll().isEmpty()){
            tagRepository.save(new Tag(null, "tag1"));
            tagRepository.save(new Tag(null, "tag3"));
            tagRepository.save(new Tag(null, "java"));
            tagRepository.save(new Tag(null, "spring"));
            tagRepository.save(new Tag(null, "boot"));
            tagRepository.save(new Tag(null, "sd"));
            tagRepository.save(new Tag(null, "intrebare"));
            tagRepository.save(new Tag(null, "text"));
        }
    }
}
