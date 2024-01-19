package com.greengram.greengram4.feed.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Value;

@Data
public class FeedCommentInsDto {
    @JsonIgnore
    private int ifeedComment;
    @JsonIgnore
    private int iuser;

    @Min(value = 1, message ="ifeed 값은 1이상입니다")
    private int ifeed;

    @NotEmpty(message = "댓글을 입력해주세요")
    @Size(min=2, message = "댓글 내용은 2글자 이상이여야 합니다")
    private String comment;


}
