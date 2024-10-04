package com.questApplication.questApplication.business.concretes;

import com.questApplication.questApplication.business.abstracts.PostService;
import com.questApplication.questApplication.core.utilities.result.*;
import com.questApplication.questApplication.entity.Post;
import com.questApplication.questApplication.entity.dto.PostDTO;
import com.questApplication.questApplication.mapper.PostMapper;
import com.questApplication.questApplication.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostManager implements PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    @Autowired
    public PostManager(PostRepository postRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
    }

    @Override
    public DataResult<Page<PostDTO>> getAllPosts(Pageable pageable) {
        try {
            Page<Post> posts = postRepository.findAllByStatusNot("D", pageable);
            Page<PostDTO> postDTOs = posts.map(postMapper::toDTO);
            return new SuccessDataResult<>(postDTOs, "Gönderiler başarıyla getirildi");
        } catch (Exception e) {
            return new ErrorDataResult<>(null, "Gönderiler getirilirken bir hata oluştu");
        }
    }

    @Override
    public DataResult<PostDTO> getPostById(Long id) {
        try {
            Post post = postRepository.findByIdAndStatusNot(id, "D").orElse(null);
            if (post != null) {
                PostDTO postDTO = postMapper.toDTO(post);
                return new SuccessDataResult<>(postDTO, "Gönderi başarıyla getirildi");
            } else {
                return new ErrorDataResult<>(null, "Gönderi bulunamadı");
            }
        } catch (Exception e) {
            return new ErrorDataResult<>(null, "Gönderi getirilirken bir hata oluştu");
        }
    }

    @Override
    public DataResult<Page<PostDTO>> getPostsByUserId(Long userId, Pageable pageable) {
        try {
            Page<Post> posts = postRepository.findByUserIdAndStatusNot(userId, "D", pageable);
            Page<PostDTO> postDTOs = posts.map(postMapper::toDTO);
            return new SuccessDataResult<>(postDTOs, "Kullanıcı gönderileri başarıyla getirildi");
        } catch (Exception e) {
            return new ErrorDataResult<>(null, "Kullanıcı gönderileri getirilirken bir hata oluştu");
        }
    }

    @Override
    @Transactional
    public DataResult<PostDTO> createPost(PostDTO postDTO) {
        try {
            Post post = postMapper.toEntity(postDTO);
            post.setStatus("A"); // Active
            Post savedPost = postRepository.save(post);
            PostDTO savedPostDTO = postMapper.toDTO(savedPost);
            return new SuccessDataResult<>(savedPostDTO, "Gönderi başarıyla oluşturuldu");
        } catch (Exception e) {
            return new ErrorDataResult<>(null, "Gönderi oluşturulurken bir hata oluştu");
        }
    }

    @Override
    @Transactional
    public DataResult<PostDTO> updatePost(Long id, PostDTO postDTO) {
        try {
            Post existingPost = postRepository.findByIdAndStatusNot(id, "D").orElse(null);
            if (existingPost != null) {
                Post updatedPost = postMapper.toEntity(postDTO);
                updatedPost.setId(id);
                updatedPost.setStatus("U"); // Updated
                Post savedPost = postRepository.save(updatedPost);
                PostDTO savedPostDTO = postMapper.toDTO(savedPost);
                return new SuccessDataResult<>(savedPostDTO, "Gönderi başarıyla güncellendi");
            } else {
                return new ErrorDataResult<>(null, "Güncellenecek gönderi bulunamadı");
            }
        } catch (Exception e) {
            return new ErrorDataResult<>(null, "Gönderi güncellenirken bir hata oluştu");
        }
    }

    @Override
    @Transactional
    public Result deletePost(Long id) {
        try {
            Post post = postRepository.findByIdAndStatusNot(id, "D").orElse(null);
            if (post != null) {
                post.setStatus("D"); // Deleted
                postRepository.save(post);
                return new SuccessResult("Gönderi başarıyla silindi");
            } else {
                return new ErrorResult("Silinecek gönderi bulunamadı");
            }
        } catch (Exception e) {
            return new ErrorResult("Gönderi silinirken bir hata oluştu");
        }
    }

    @Override
    @Transactional
    public DataResult<PostDTO> activatePost(Long id) {
        try {
            Post post = postRepository.findById(id).orElse(null);
            if (post != null) {
                if (!post.getStatus().equals("A")) {
                    post.setStatus("A"); // Active
                    Post savedPost = postRepository.save(post);
                    PostDTO activatedPostDTO = postMapper.toDTO(savedPost);
                    return new SuccessDataResult<>(activatedPostDTO, "Gönderi başarıyla aktifleştirildi");
                } else {
                    return new ErrorDataResult<>(null, "Gönderi zaten aktif");
                }
            } else {
                return new ErrorDataResult<>(null, "Aktifleştirilecek gönderi bulunamadı");
            }
        } catch (Exception e) {
            return new ErrorDataResult<>(null, "Gönderi aktifleştirilirken bir hata oluştu");
        }
    }
}
