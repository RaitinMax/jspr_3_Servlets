package ru.netology.repository;

import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class PostRepository {
    private final Map<Long, Post> posts = new ConcurrentHashMap<>();
    private final AtomicInteger size = new AtomicInteger(0);

    public List<Post> all() {
        return posts.values()
                .stream()
                .filter(x -> !x.isRemoved())
                .collect(Collectors.toList());
    }

    public Optional<Post> getById(long id) {
       var post = posts.get(id);
        return post == null || post.isRemoved() ? Optional.empty() : Optional.of(post);
    }

    public Post save(Post post) {
        if(post.getId() == 0){
            post.setId(size.addAndGet(1));
        }else {
            var found = getById(post.getId()).orElseThrow(NotFoundException::new);
            if (found.isRemoved()) throw new NotFoundException();
        }
        posts.put(post.getId(),post);
        return post;
    }

    public void removeById(long id) {
        Post post =getById(id).orElseThrow(NotFoundException::new);
        post.setRemoved(true);
    }
}
