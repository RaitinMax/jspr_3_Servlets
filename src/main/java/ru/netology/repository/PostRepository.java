package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;

public class PostRepository {
    private ConcurrentHashMap<Long, Post> posts = new ConcurrentHashMap<>();

    public List<Post> all() {
        return posts.values().stream().filter(p -> !p.isBeenRemoved()).collect(Collectors.toList());
    }

    public Optional<Post> getById(long id) {
        return posts.entrySet().stream()
                .filter(e -> e.getKey() == id)
                .map(Map.Entry::getValue)
                .filter(p -> !p.isBeenRemoved())
                .findFirst();
    }

    public Post save(Post post) throws NotFoundException {
        if (posts.containsKey(post.getId()) && !posts.get(post.getId()).isBeenRemoved()) {
            posts.put(post.getId(), post);
        } else if (post.getId() == 0) {
            posts.putIfAbsent(Integer.valueOf(posts.size() + 1).longValue(), post.setId(posts.size() + 1));
        } else {
            throw new NotFoundException();
        }
        return post;
    }

    public void removeById(long id) {
        if (posts.containsKey(id)) {
            posts.get(id).setBeenRemoved(true);
        } else {
            throw new NotFoundException();
        }
    }
}
