package org.book.springboot.service.posts;

import lombok.RequiredArgsConstructor;
import org.book.springboot.domain.posts.Posts;
import org.book.springboot.domain.posts.PostsRepository;
import org.book.springboot.web.dto.PostsResponseDto;
import org.book.springboot.web.dto.PostsSaveRequestDto;
import org.book.springboot.web.dto.PostsUpdateRequestDto;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    public PostsResponseDto findById(Long id) {
        Posts entity = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id="+id));
        return new PostsResponseDto(entity);
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts entity = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id="+id));
        entity.update(requestDto.getTitle(), requestDto.getContent());
        return id;
    }
}
