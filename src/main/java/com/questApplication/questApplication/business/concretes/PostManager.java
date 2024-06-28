package com.questApplication.questApplication.business.concretes;

import com.questApplication.questApplication.business.abstracts.PostService;
import com.questApplication.questApplication.core.utilities.result.*;
import com.questApplication.questApplication.entity.Post;
import com.questApplication.questApplication.entity.dto.PostDTO;
import com.questApplication.questApplication.mapper.PostMapper;
import com.questApplication.questApplication.repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public DataResult<Page<PostDTO>> getAllPosts(Pageable pageable) {
        logger.info("Tüm gönderiler getiriliyor. Sayfa: {}, Boyut: {}", pageable.getPageNumber(), pageable.getPageSize());
        try {
            Page<Post> posts = postRepository.findAllByStatusNot("D", pageable);
            Page<PostDTO> postDTOs = posts.map(postMapper::toDTO);
            logger.info("Toplam {} gönderi başarıyla getirildi", postDTOs.getTotalElements());
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
            Post post = postRepository.findByIdAndStatusNot(id, "D").orElse(null);
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
    public DataResult<Page<PostDTO>> getPostsByUserId(Long userId, Pageable pageable) {
        logger.info("{} ID'li kullanıcının gönderileri getiriliyor. Sayfa: {}, Boyut: {}", userId, pageable.getPageNumber(), pageable.getPageSize());
        try {
            Page<Post> posts = postRepository.findByUserIdAndStatusNot(userId, "D", pageable);
            Page<PostDTO> postDTOs = posts.map(postMapper::toDTO);
            logger.info("{} ID'li kullanıcının {} gönderisi başarıyla getirildi", userId, postDTOs.getTotalElements());
            return new SuccessDataResult<>(postDTOs, "Kullanıcı gönderileri başarıyla getirildi");
        } catch (Exception e) {
            logger.error("{} ID'li kullanıcının gönderileri getirilirken bir hata oluştu", userId, e);
            return new ErrorDataResult<>(null, "Kullanıcı gönderileri getirilirken bir hata oluştu");
        }
    }

    @Override
    @Transactional
    public DataResult<PostDTO> createPost(PostDTO postDTO) {
        logger.info("Yeni gönderi oluşturuluyor");
        try {
            Post post = postMapper.toEntity(postDTO);
            post.setStatus("A"); // Active
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
    @Transactional
    public DataResult<PostDTO> updatePost(Long id, PostDTO postDTO) {
        logger.info("{} ID'li gönderi güncelleniyor", id);
        try {
            Post existingPost = postRepository.findByIdAndStatusNot(id, "D").orElse(null);
            if (existingPost != null) {
                Post updatedPost = postMapper.toEntity(postDTO);
                updatedPost.setId(id);
                updatedPost.setStatus("U"); // Updated
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
    @Transactional
    public Result deletePost(Long id) {
        logger.info("{} ID'li gönderi siliniyor (soft delete)", id);
        try {
            Post post = postRepository.findByIdAndStatusNot(id, "D").orElse(null);
            if (post != null) {
                post.setStatus("D"); // Deleted
                postRepository.save(post);
                logger.info("{} ID'li gönderi başarıyla silindi (soft delete)", id);
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

    @Override
    @Transactional
    public DataResult<PostDTO> activatePost(Long id) {
        logger.info("{} ID'li gönderi aktifleştiriliyor", id);
        try {
            Post post = postRepository.findById(id).orElse(null);
            if (post != null) {
                if (!post.getStatus().equals("A")) {
                    post.setStatus("A"); // Active
                    Post savedPost = postRepository.save(post);
                    PostDTO activatedPostDTO = postMapper.toDTO(savedPost);
                    logger.info("{} ID'li gönderi başarıyla aktifleştirildi", id);
                    return new SuccessDataResult<>(activatedPostDTO, "Gönderi başarıyla aktifleştirildi");
                } else {
                    logger.warn("{} ID'li gönderi zaten aktif", id);
                    return new ErrorDataResult<>(null, "Gönderi zaten aktif");
                }
            } else {
                logger.warn("{} ID'li gönderi bulunamadı", id);
                return new ErrorDataResult<>(null, "Aktifleştirilecek gönderi bulunamadı");
            }
        } catch (Exception e) {
            logger.error("{} ID'li gönderi aktifleştirilirken bir hata oluştu", id, e);
            return new ErrorDataResult<>(null, "Gönderi aktifleştirilirken bir hata oluştu");
        }
    }
}