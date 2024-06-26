package com.dominest.dominestbackend.api.post.image.request;

import com.dominest.dominestbackend.domain.post.component.category.entity.Category;
import com.dominest.dominestbackend.domain.post.image.entity.ImageType;
import com.dominest.dominestbackend.domain.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@NoArgsConstructor
public class SaveImageTypeRequest {
    @NotBlank(message = "제목은 비어있을 수 없습니다")
    String title;
    @NotNull(message = "이미지는 비어있을 수 없습니다")
    List<MultipartFile> postImages;

    public ImageType toEntity(List<String> imageUrlList, User writer, Category category){
        return ImageType.builder()
                .title(title)
                .writer(writer)
                .category(category)
                .imageUrls(imageUrlList)
                .build();
    }
}

