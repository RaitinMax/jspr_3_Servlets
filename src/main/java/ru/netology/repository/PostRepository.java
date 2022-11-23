package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class PostRepository {
    private final ConcurrentHashMap<Long, Post> posts = new ConcurrentHashMap<>();
    private final AtomicLong COUNTER = new AtomicLong(1);

    public List<Post> all() {
        return new ArrayList<>(posts.values());
    }

    public Optional<Post> getById(long id) throws NotFoundException {
        return Optional.ofNullable(posts.get(id));
    }

    public Post save(Post post) {

        if (post.getId() == 0) {
            long id = COUNTER.getAndIncrement();
            post.setId(id);
            posts.put(id, new Post(id, post.getContent()));
        }

        if (post.getId() != 0) {
            if (posts.containsKey(post.getId())) {
                posts.put(post.getId(), new Post(post.getId(), post.getContent()));
            } else {
                throw new NullPointerException("Такого поста не существует");
            }
        }
        return post;
    }

    public void removeById(long id) {
        posts.remove(id);
    }
}