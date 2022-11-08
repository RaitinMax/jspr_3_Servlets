package ru.netology.service;

import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;
import ru.netology.repository.PostRepository;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class PostService {
    private final PostRepository repository;

    public PostService(PostRepository repository) {
        this.repository = repository;
    }

    public List<Post> all() {
        return repository.all()
                .stream()
                .filter(x -> !x.isRemoved())
                .collect(Collectors.toList());
    }

    public Post getById(long id) {
        var post = repository.getById(id).orElseThrow(NotFoundException::new);
        if (post.isRemoved()) throw new NotFoundException();
        return post;
    }

    public Post save(Post post) {
        if (post.getId() != 0) {
            var found = repository.getById(post.getId()).orElseThrow(NotFoundException::new);
            if (found.isRemoved()) throw new NotFoundException();
        }
        return repository.save(post);
    }

    public void removeById(long id) {
        var post = repository.getById(id).orElseThrow(NotFoundException::new);
        if (post.isRemoved()) throw new NotFoundException();
        repository.removeById(id);
    }
}

