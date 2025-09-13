package com.dontgojunbao.bossoverhere.domain.post.controller.docs;


import com.dontgojunbao.bossoverhere.domain.post.dto.PostDto;
import com.dontgojunbao.bossoverhere.domain.post.dto.PostSaveDto;
import com.dontgojunbao.bossoverhere.domain.post.dto.PostSimpleDto;
import com.dontgojunbao.bossoverhere.domain.post.dto.PostUpdateDto;
import com.dontgojunbao.bossoverhere.global.common.dto.CommonResponse;
import com.dontgojunbao.bossoverhere.global.error.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public interface PostControllerDocs {

    // -------------------------------
    // POST /api/posts (multipart)
    // -------------------------------
    @Operation(
            summary = "게시글 생성",
            description = "새로운 게시글을 작성합니다. 멀티파트 업로드 지원(이미지 선택).",
            tags = "Post"
    )
    @RequestBody(
            required = true,
            content = @Content(
                    mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                    schema = @Schema(implementation = PostSaveDto.class),
                    examples = @ExampleObject(name = "multipart-form",
                            value = """
                            # multipart/form-data 예시 (키=값)
                            spotId=7
                            startAt=2025-07-30T09:00:00
                            endAt=2025-07-30T17:00:00
                            revenue=100000
                            expense=50000
                            memo=하루 매출 기록
                            uploadFile=(이미지 파일)
                            """)
            )
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "생성 성공 (신규 postId 반환)",
                    content = @Content(
                            schema = @Schema(implementation = CommonResponse.class),
                            examples = @ExampleObject(name = "success",
                                    value = """
                                    { "success": true, "data": 123 }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "유효성 실패/비속어 포함/잘못된 파라미터",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(name = "INVALID_PARAMETER",
                                            value = """
                                            { "code": "INVALID_PARAMETER", "message": "유효하지 않은 요청 파라미터가 포함되어 있습니다." }
                                            """),
                                    @ExampleObject(name = "BAD_WORD_DETECTED",
                                            value = """
                                            { "code": "BAD_WORD_DETECTED", "message": "비속어는 문구에 포함할 수 없습니다." }
                                            """)
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 실패(토큰 문제)",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(name = "INVALID_ACCESS_TOKEN",
                                            value = """
                                            { "code": "INVALID_ACCESS_TOKEN", "message": "유효하지 않은 액세스 토큰입니다." }
                                            """),
                                    @ExampleObject(name = "EXPIRED_ACCESS_TOKEN",
                                            value = """
                                            { "code": "EXPIRED_ACCESS_TOKEN", "message": "액세스 토큰이 만료되었습니다." }
                                            """)
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "스팟 없음",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "NOTFOUND_SPOT",
                                    value = """
                                    { "code": "NOTFOUND_SPOT", "message": "해당 스팟를 찾을 수 없습니다." }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "INTERNAL_SERVER_ERROR",
                                    value = """
                                    { "code": "INTERNAL_SERVER_ERROR", "message": "서버 오류가 발생했습니다." }
                                    """)
                    )
            )
    })
    ResponseEntity<CommonResponse<Long>> createPost(
            @Parameter(hidden = true, description = "인증된 사용자 ID") Long userId,
            @Parameter(description = "게시글 생성 폼데이터")
            PostSaveDto dto
    );

    // -------------------------------
    // GET /api/posts/{postId}
    // -------------------------------
    @Operation(
            summary = "단일 게시글 조회",
            description = "postId로 게시글 한 건을 조회합니다.",
            tags = "Post"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "성공",
                    content = @Content(
                            schema = @Schema(implementation = CommonResponse.class),
                            examples = @ExampleObject(name = "success",
                                    value = """
                                    {
                                      "success": true,
                                      "data": {
                                        "postId": 123,
                                        "writerId": 42,
                                        "spot": {
                                          "spotId": 7,
                                          "spotName": "왕송호수공원",
                                          "spotAddress": "경기도 의왕시 초평동 278-9",
                                          "spotLatitude": 37.3098252097,
                                          "spotLongitude": 126.9441405769
                                        },
                                        "startAt": "2025-07-30T09:00:00",
                                        "endAt": "2025-07-30T17:00:00",
                                        "revenue": 100000,
                                        "expense": 50000,
                                        "profit": 50000,
                                        "memo": "하루 매출 기록",
                                        "filePath": "https://example-bucket.s3.amazonaws.com/image.png",
                                        "createdAt": "2025-07-30T18:00:00",
                                        "updatedAt": "2025-07-30T18:30:00"
                                      }
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 실패(토큰 문제)",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(name = "INVALID_ACCESS_TOKEN",
                                            value = """
                                            { "code": "INVALID_ACCESS_TOKEN", "message": "유효하지 않은 액세스 토큰입니다." }
                                            """),
                                    @ExampleObject(name = "EXPIRED_ACCESS_TOKEN",
                                            value = """
                                            { "code": "EXPIRED_ACCESS_TOKEN", "message": "액세스 토큰이 만료되었습니다." }
                                            """)
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "게시글 없음",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "NOT_FOUND_POST",
                                    value = """
                                    { "code": "NOT_FOUND_POST", "message": "기록을 찾을 수 없습니다." }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "INTERNAL_SERVER_ERROR",
                                    value = """
                                    { "code": "INTERNAL_SERVER_ERROR", "message": "서버 오류가 발생했습니다." }
                                    """)
                    )
            )
    })
    ResponseEntity<CommonResponse<PostDto>> getPost(
            @Parameter(hidden = true, description = "인증된 사용자 ID") Long userId,
            @Parameter(description = "게시글 ID", example = "123") Long postId
    );

    // -------------------------------
    // GET /api/posts (Page<PostSimpleDto>)
    // -------------------------------
    @Operation(
            summary = "전체 게시글 조회",
            description = "페이징된 게시글 목록을 조회합니다. 목록은 ID 중심의 Simple DTO로 반환됩니다.",
            tags = "Post"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "성공 (Spring Page 구조)",
                    content = @Content(
                            schema = @Schema(implementation = CommonResponse.class),
                            examples = @ExampleObject(name = "success-page",
                                    value = """
                                    {
                                      "success": true,
                                      "data": {
                                        "content": [
                                          {
                                            "postId": 101,
                                            "writerId": 42,
                                            "spotId": 7,
                                            "startAt": "2025-07-30T09:00:00",
                                            "endAt": "2025-07-30T17:00:00",
                                            "revenue": 120000,
                                            "expense": 60000,
                                            "profit": 60000,
                                            "memo": "점심 특수",
                                            "filePath": null,
                                            "createdAt": "2025-07-30T18:00:00",
                                            "updatedAt": "2025-07-30T18:30:00"
                                          },
                                          {
                                            "postId": 102,
                                            "writerId": 77,
                                            "spotId": 5,
                                            "startAt": "2025-07-29T10:00:00",
                                            "endAt": "2025-07-29T16:00:00",
                                            "revenue": 90000,
                                            "expense": 30000,
                                            "profit": 60000,
                                            "memo": "비와서 조용",
                                            "filePath": "https://example-bucket.s3.amazonaws.com/post102.png",
                                            "createdAt": "2025-07-29T17:00:00",
                                            "updatedAt": "2025-07-29T17:10:00"
                                          }
                                        ],
                                        "pageable": { "pageNumber": 0, "pageSize": 20, "sort": { "sorted": false } },
                                        "totalElements": 500,
                                        "totalPages": 25,
                                        "last": false,
                                        "size": 20,
                                        "number": 0,
                                        "first": true,
                                        "numberOfElements": 20,
                                        "empty": false
                                      }
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 실패(토큰 문제)",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(name = "INVALID_ACCESS_TOKEN",
                                            value = """
                                            { "code": "INVALID_ACCESS_TOKEN", "message": "유효하지 않은 액세스 토큰입니다." }
                                            """),
                                    @ExampleObject(name = "EXPIRED_ACCESS_TOKEN",
                                            value = """
                                            { "code": "EXPIRED_ACCESS_TOKEN", "message": "액세스 토큰이 만료되었습니다." }
                                            """)
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "INTERNAL_SERVER_ERROR",
                                    value = """
                                    { "code": "INTERNAL_SERVER_ERROR", "message": "서버 오류가 발생했습니다." }
                                    """)
                    )
            )
    })
    ResponseEntity<CommonResponse<Page<PostSimpleDto>>> getPosts(
            @Parameter(hidden = true, description = "인증된 사용자 ID") Long userId,
            @ParameterObject Pageable pageable
    );

    // -------------------------------
    // PUT /api/posts/{postId} (multipart)
    // -------------------------------
    @Operation(
            summary = "게시글 수정",
            description = "postId에 해당하는 게시글을 수정합니다. (멀티파트 업로드 지원)",
            tags = "Post"
    )
    @RequestBody(
            required = true,
            content = @Content(
                    mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                    schema = @Schema(implementation = PostUpdateDto.class),
                    examples = @ExampleObject(name = "multipart-form",
                            value = """
                            # multipart/form-data 예시 (필드별 Optional)
                            spotId=5
                            startAt=2025-08-01T09:00:00
                            endAt=2025-08-01T17:00:00
                            revenue=150000
                            expense=40000
                            memo=메모 수정
                            uploadFile=(새 이미지 파일)
                            """)
            )
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "수정 성공 (콘텐츠 없음)",
                    content = @Content(
                            schema = @Schema(implementation = CommonResponse.class),
                            examples = @ExampleObject(name = "success-no-content",
                                    value = """
                                    { "success": true }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "유효성 실패/비속어 포함/잘못된 파라미터",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(name = "INVALID_PARAMETER",
                                            value = """
                                            { "code": "INVALID_PARAMETER", "message": "유효하지 않은 요청 파라미터가 포함되어 있습니다." }
                                            """),
                                    @ExampleObject(name = "BAD_WORD_DETECTED",
                                            value = """
                                            { "code": "BAD_WORD_DETECTED", "message": "비속어는 문구에 포함할 수 없습니다." }
                                            """)
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 실패(토큰 문제)",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(name = "INVALID_ACCESS_TOKEN",
                                            value = """
                                            { "code": "INVALID_ACCESS_TOKEN", "message": "유효하지 않은 액세스 토큰입니다." }
                                            """),
                                    @ExampleObject(name = "EXPIRED_ACCESS_TOKEN",
                                            value = """
                                            { "code": "EXPIRED_ACCESS_TOKEN", "message": "액세스 토큰이 만료되었습니다." }
                                            """)
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "작성자 아님(권한 없음)",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "FORBIDDEN_POST",
                                    value = """
                                    { "code": "FORBIDDEN_POST", "message": "해당 기록에 대한 변경 권한이 없습니다." }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "게시글/스팟 없음",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(name = "NOT_FOUND_POST",
                                            value = """
                                            { "code": "NOT_FOUND_POST", "message": "기록을 찾을 수 없습니다." }
                                            """),
                                    @ExampleObject(name = "NOTFOUND_SPOT",
                                            value = """
                                            { "code": "NOTFOUND_SPOT", "message": "해당 스팟를 찾을 수 없습니다." }
                                            """)
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "INTERNAL_SERVER_ERROR",
                                    value = """
                                    { "code": "INTERNAL_SERVER_ERROR", "message": "서버 오류가 발생했습니다." }
                                    """)
                    )
            )
    })
    ResponseEntity<CommonResponse<Void>> updatePost(
            @Parameter(hidden = true, description = "인증된 사용자 ID") Long userId,
            @Parameter(description = "게시글 ID", example = "123") Long postId,
            @Parameter(description = "게시글 수정 폼데이터") PostUpdateDto dto
    );

    // -------------------------------
    // DELETE /api/posts/{postId}
    // -------------------------------
    @Operation(
            summary = "게시글 삭제",
            description = "postId에 해당하는 게시글을 삭제합니다.",
            tags = "Post"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "삭제 성공 (콘텐츠 없음)",
                    content = @Content(
                            schema = @Schema(implementation = CommonResponse.class),
                            examples = @ExampleObject(name = "success-no-content",
                                    value = """
                                    { "success": true }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 실패(토큰 문제)",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(name = "INVALID_ACCESS_TOKEN",
                                            value = """
                                            { "code": "INVALID_ACCESS_TOKEN", "message": "유효하지 않은 액세스 토큰입니다." }
                                            """),
                                    @ExampleObject(name = "EXPIRED_ACCESS_TOKEN",
                                            value = """
                                            { "code": "EXPIRED_ACCESS_TOKEN", "message": "액세스 토큰이 만료되었습니다." }
                                            """)
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "작성자 아님(권한 없음)",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "FORBIDDEN_POST",
                                    value = """
                                    { "code": "FORBIDDEN_POST", "message": "해당 기록에 대한 변경 권한이 없습니다." }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "게시글 없음",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "NOT_FOUND_POST",
                                    value = """
                                    { "code": "NOT_FOUND_POST", "message": "기록을 찾을 수 없습니다." }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "INTERNAL_SERVER_ERROR",
                                    value = """
                                    { "code": "INTERNAL_SERVER_ERROR", "message": "서버 오류가 발생했습니다." }
                                    """)
                    )
            )
    })
    ResponseEntity<CommonResponse<Void>> deletePost(
            @Parameter(hidden = true, description = "인증된 사용자 ID") Long userId,
            @Parameter(description = "게시글 ID", example = "123") Long postId
    );
}
