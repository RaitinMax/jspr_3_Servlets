package ru.netology.controller;

import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Controller;
import ru.netology.model.Post;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

public class PostController {
  public static final String APPLICATION_JSON = "application/json";
  private final PostService service;

  public PostController(PostService service) {
    this.service = service;
  }

  public void all(HttpServletResponse response) throws IOException {
    final var data = service.all();
    sendJson(data, response);
  }

  public void getById(long id, HttpServletResponse response) throws IOException {
    final var data = service.getById(id);
    sendJson(data, response);
  }

  public void save(Reader body, HttpServletResponse response) throws IOException {
    response.setContentType(APPLICATION_JSON);


    final var gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    final var post = gson.fromJson(body, Post.class);
    final var data = service.save(post);
    response.getWriter().print(gson.toJson(data));
  }

  public void removeById(long id, HttpServletResponse response) throws IOException {
    service.removeById(id);
    response.setStatus(HttpServletResponse.SC_OK);
  }

  private void sendJson(Post data, HttpServletResponse response) throws IOException {
    final var gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    response.setContentType(APPLICATION_JSON);
    response.getWriter().write(gson.toJson(data));
  }

  private void sendJson(List<Post> data, HttpServletResponse response) throws IOException {
    final var gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    response.setContentType(APPLICATION_JSON);
    response.getWriter().write(gson.toJson(data));
  }
}
