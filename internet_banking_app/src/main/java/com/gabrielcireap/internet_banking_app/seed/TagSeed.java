package com.gabrielcireap.internet_banking_app.seed;

import com.gabrielcireap.internet_banking_app.entity.Tag;
import com.gabrielcireap.internet_banking_app.repository.RepositoryFactory;
import com.gabrielcireap.internet_banking_app.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
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
