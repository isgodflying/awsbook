package com.world.silvergoni.springboot.web.service;

import com.world.silvergoni.springboot.web.domain.posts.Posts;
import com.world.silvergoni.springboot.web.domain.posts.PostsRepository;
import com.world.silvergoni.springboot.web.dto.PostsResponseDto;
import com.world.silvergoni.springboot.web.dto.PostsSaveRequestDto;
import com.world.silvergoni.springboot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {
	private final PostsRepository postsRepository;

	@Transactional
	public Long save(PostsSaveRequestDto requestDto) {
		return postsRepository.save((requestDto.toEntity())).getId();
	}

	@Transactional	//이거 조심
	public Long update(long id, PostsUpdateRequestDto requestDto) {
		Posts posts = postsRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
		posts.update(requestDto.getTitle(), requestDto.getContent());

		return id;
	}

	public PostsResponseDto findById(Long id) {
		Posts entity = postsRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
		return new PostsResponseDto(entity);
	}

	@Transactional(readOnly = true)
	public List<PostsResponseDto> findAlldesc() {
		return postsRepository.findAllDesc().stream()
			.map(PostsResponseDto::new)
			.collect(Collectors.toList());
	}

	@Transactional
	public void delete(Long id) {
		Posts posts = postsRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
		postsRepository.delete(posts);
	}
}
