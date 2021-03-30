package com.hostar.education.springboot.service.posts;

import com.hostar.education.springboot.domain.posts.Posts;
import com.hostar.education.springboot.domain.posts.PostsRepository;
import com.hostar.education.springboot.web.dto.PostsListResponseDto;
import com.hostar.education.springboot.web.dto.PostsResponseDto;
import com.hostar.education.springboot.web.dto.PostsSaveRequestDto;
import com.hostar.education.springboot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;

    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {

        Posts entity = postsRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 게시글이 없습니다. id=" + id ));

        entity.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    public PostsResponseDto findById(Long id) {
        Posts entity = postsRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        return new PostsResponseDto(entity);
    }

    @Transactional
    public void delete(Long id) {
        Posts posts = postsRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 게시글이 없습니다. id=" + id ));

        postsRepository.delete(posts);
    }

    @Transactional(readOnly = true) // readOnly ==> 트랙잭션 범위는 유지하되, 조회 기능만 남겨두어 조회 속도가 개선 :: 등록, 수정, 삭제 기능이 전혀없는 서비스 메소드에서 사용하는걸 추천
    public List<PostsListResponseDto> findAllDesc() {
        return postsRepository.findAllDesc().stream()
                .map(PostsListResponseDto::new) // :: 참조 메소드
//                .map(p -> new PostsListResponseDto(p)) // 위와 동일한듯
                .collect(Collectors.toList());
    }
}
