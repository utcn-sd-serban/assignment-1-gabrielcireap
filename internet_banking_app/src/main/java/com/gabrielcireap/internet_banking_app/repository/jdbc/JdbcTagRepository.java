package com.gabrielcireap.internet_banking_app.repository.jdbc;

import com.gabrielcireap.internet_banking_app.entity.Tag;
import com.gabrielcireap.internet_banking_app.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.swing.text.html.Option;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class JdbcTagRepository implements TagRepository {

    private final JdbcTemplate template;

    @Override
    public List<Tag> findAll() {
        return template.query("SELECT * FROM tag", ((resultSet, i) -> new Tag(resultSet.getInt("id"),
                                                                                    resultSet.getString("name"))));
    }

    @Override
    public Tag save(Tag tag) {
        if (tag.getId() == null) {
            tag.setId(insert(tag));
        }
        return tag;
    }

    @Override
    public void remove(Tag tag) {
        template.update("DELETE FROM tag WHERE name = ?", tag);
    }

    @Override
    public Optional<Tag> findById(int id) {
        List<Tag> tags = template.query("SELECT * FROM tag WHERE id = ?", (resultSet, i) -> new Tag(resultSet.getInt("id"), resultSet.getString("name")),id);
        return tags.isEmpty() ? Optional.empty() : Optional.ofNullable(tags.get(0));
    }

    private int insert(Tag tag){
        SimpleJdbcInsert insert = new SimpleJdbcInsert(template);
        insert.setTableName("tag");
        insert.usingGeneratedKeyColumns("id");
        Map<String, Object> map = new HashMap<>();
        map.put("name", tag.getName());
        return insert.executeAndReturnKey(map).intValue();
    }
}
