package com.miniproject2.bookcafe.dto;

import com.miniproject2.bookcafe.domain.Comment;
import com.miniproject2.bookcafe.domain.Moim;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
public class CommentResponseDto {

    private Long commentId;
//    private Long moimId;
    private String nickname;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Moim moim;

    public CommentResponseDto(Comment comment, Moim moim) {
        this.commentId = comment.getCommentId();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
        this.nickname = comment.getNickname();
        this.comment = comment.getComment();
        this.moim = moim;
    }
}
