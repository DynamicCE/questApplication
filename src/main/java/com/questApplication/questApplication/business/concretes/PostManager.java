package com.questApplication.questApplication.business.concretes;

import com.questApplication.questApplication.business.abstracts.PostService;
import com.questApplication.questApplication.core.utilities.results.*;
import com.questApplication.questApplication.entity.Post;
import com.questApplication.questApplication.entity.dto.PostDTO;

import com.questApplication.questApplication.mapper.PostMapper;
import com.questApplication.questApplication.repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostManager implements PostService {
    private static final Logger logger = LoggerFactory.getLogger(PostManager.class);
    private final PostRepository postRepository;
    private final PostMapper postMapper;

    @Autowired
    public PostManager(PostRepository postRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
    }

    @Override
    public DataResult<List<PostDTO>> getAllPosts() {
        logger.info("Tüm gönderiler getiriliyor");
        try {
            List<Post> posts = postRepository.findAll();
            List<PostDTO> postDTOs = posts.stream()
                    .map(postMapper::toDTO)
                    .collect(Collectors.toList());
            logger.info("Toplam {} gönderi başarıyla getirildi", postDTOs.size());
            return new SuccessDataResult<>(postDTOs, "Gönderiler başarıyla getirildi");
        } catch (Exception e) {
            logger.error("Gönderiler getirilirken bir hata oluştu", e);
            return new ErrorDataResult<>(null, "Gönderiler getirilirken bir hata oluştu");
        }
    }

    @Override
    public DataResult<PostDTO> getPostById(Long id) {
        logger.info("{} ID'li gönderi getiriliyor", id);
        try {
            Post post = postRepository.findById(id).orElse(null);
            if (post != null) {
                PostDTO postDTO = postMapper.toDTO(post);
                logger.info("{} ID'li gönderi başarıyla getirildi", id);
                return new SuccessDataResult<>(postDTO, "Gönderi başarıyla getirildi");
            } else {
                logger.warn("{} ID'li gönderi bulunamadı", id);
                return new ErrorDataResult<>(null, "Gönderi bulunamadı");
            }
        } catch (Exception e) {
            logger.error("{} ID'li gönderi getirilirken bir hata oluştu", id, e);
            return new ErrorDataResult<>(null, "Gönderi getirilirken bir hata oluştu");
        }
    }

    @Override
    public DataResult<List<PostDTO>> getPostsByUserId(Long userId) {
        logger.info("{} ID'li kullanıcının gönderileri getiriliyor", userId);
        try {
            List<Post> posts = postRepository.findByUserId(userId);
            List<PostDTO> postDTOs = posts.stream()
                    .map(postMapper::toDTO)
                    .collect(Collectors.toList());
            logger.info("{} ID'li kullanıcının {} gönderisi başarıyla getirildi", userId, postDTOs.size());
            return new SuccessDataResult<>(postDTOs, "Kullanıcı gönderileri başarıyla getirildi");
        } catch (Exception e) {
            logger.error("{} ID'li kullanıcının gönderileri getirilirken bir hata oluştu", userId, e);
            return new ErrorDataResult<>(null, "Kullanıcı gönderileri getirilirken bir hata oluştu");
        }
    }

    @Override
    public DataResult<PostDTO> createPost(PostDTO postDTO) {
        logger.info("Yeni gönderi oluşturuluyor");
        try {
            Post post = postMapper.toEntity(postDTO);
            Post savedPost = postRepository.save(post);
            PostDTO savedPostDTO = postMapper.toDTO(savedPost);
            logger.info("{} ID'li yeni gönderi başarıyla oluşturuldu", savedPostDTO.getId());
            return new SuccessDataResult<>(savedPostDTO, "Gönderi başarıyla oluşturuldu");
        } catch (Exception e) {
            logger.error("Gönderi oluşturulurken bir hata oluştu", e);
            return new ErrorDataResult<>(null, "Gönderi oluşturulurken bir hata oluştu");
        }
    }

    @Override
    public DataResult<PostDTO> updatePost(Long id, PostDTO postDTO) {
        logger.info("{} ID'li gönderi güncelleniyor", id);
        try {
            Post existingPost = postRepository.findById(id).orElse(null);
            if (existingPost != null) {
                Post updatedPost = postMapper.toEntity(postDTO);
                updatedPost.setId(id);
                Post savedPost = postRepository.save(updatedPost);
                PostDTO savedPostDTO = postMapper.toDTO(savedPost);
                logger.info("{} ID'li gönderi başarıyla güncellendi", id);
                return new SuccessDataResult<>(savedPostDTO, "Gönderi başarıyla güncellendi");
            } else {
                logger.warn("{} ID'li gönderi bulunamadı", id);
                return new ErrorDataResult<>(null, "Güncellenecek gönderi bulunamadı");
            }
        } catch (Exception e) {
            logger.error("{} ID'li gönderi güncellenirken bir hata oluştu", id, e);
            return new ErrorDataResult<>(null, "Gönderi güncellenirken bir hata oluştu");
        }
    }

    @Override
    public Result deletePost(Long id) {
        logger.info("{} ID'li gönderi siliniyor", id);
        try {
            if (postRepository.existsById(id)) {
                postRepository.deleteById(id);
                logger.info("{} ID'li gönderi başarıyla silindi", id);
                return new SuccessResult("Gönderi başarıyla silindi");
            } else {
                logger.warn("{} ID'li gönderi bulunamadı", id);
                return new ErrorResult("Silinecek gönderi bulunamadı");
            }
        } catch (Exception e) {
            logger.error("{} ID'li gönderi silinirken bir hata oluştu", id, e);
            return new ErrorResult("Gönderi silinirken bir hata oluştu");
        }
    }
}